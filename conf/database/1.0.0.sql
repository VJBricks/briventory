/* SQL STATEMENT */ create schema if not exists "public";
/* SQL STATEMENT */ create schema if not exists "private";
/* SQL STATEMENT */ create extension if not exists unaccent;

/* SQL STATEMENT */ create domain domain_revision as varchar(16);
/* SQL STATEMENT */ create domain domain_name as varchar(1024);
/* SQL STATEMENT */ create domain domain_password as varchar(1024);
/* SQL STATEMENT */ create domain domain_email as varchar(1024);
/* SQL STATEMENT */ create domain domain_url as varchar(4096);
/* SQL STATEMENT */ create domain domain_color_component as smallint constraint domain_color_component_check check ((value >= 0) and (value <= 255));
/* SQL STATEMENT */ create domain domain_alpha_component as real constraint domain_alpha_component_check check ((value >= (0)::double precision) and (value <= (1)::double precision));
/* SQL STATEMENT */ create domain domain_token as varchar(64);
/* SQL STATEMENT */ create domain domain_username as varchar(1024);
/* SQL STATEMENT */ create domain domain_format_string as varchar(256);
/* SQL STATEMENT */ create domain domain_dimension as double precision;

/* SQL STATEMENT */
create table constants (
    stud_to_mm_factor smallint default 8,
    cm_to_mm_factor smallint default 10,
    stud_height_mm float default 1.7
);

/* SQL STATEMENT */ comment on column constants.stud_to_mm_factor is 'The factor to convert a stud dimension to millimeter.';
/* SQL STATEMENT */ comment on column constants.cm_to_mm_factor is 'The factor to convert a centimeter dimension to millimeter.';
/* SQL STATEMENT */ comment on column constants.stud_height_mm is 'The height of a stud in millimeter.';

/* SQL STATEMENT */
create table configuration (
    database_revision domain_revision not null
);

/* SQL STATEMENT */ comment on column configuration.database_revision is 'The database revision follow the semantic versioning rules (https://semver.org).';

/* SQL STATEMENT */
insert into configuration(database_revision) values ('1.0.0');

/* SQL STATEMENT */
create table color_source (
    id bigserial constraint pkey_color_source primary key,
    name domain_name not null,
    url domain_url  not null
);

/* SQL STATEMENT */
create table account (
    id bigserial constraint pk_account primary key,
    id_color_source bigint constraint fk_account_to_color_source references color_source(id) on update restrict on delete restrict,
    firstname domain_name not null,
    lastname domain_name not null,
    email domain_email not null,
    password domain_password not null,
    constraint unique_account_email unique (email)
);

/* SQL STATEMENT */
create table administrator (
    id_account bigint not null
        constraint pk_administrator primary key
        constraint fk_administrator_to_account references account(id) on update restrict on delete cascade,
    constraint unique_administrator_id_account unique(id_account)
);

/* SQL STATEMENT */
create table locked_account (
    id_account bigint not null
        constraint pk_locked_account primary key
        constraint fk_locked_account_to_account references account(id) on update restrict on delete cascade
);

create function private.trgfunc_has_administrator() returns trigger language plpgsql as
$$
declare
    hasAdmin boolean;
    idAccount bigint;
begin

    select id_account
    from coalesce(new, old)
    into idaccount;

    select exists(
        select *
        from administrator
            left join locked_account on administrator.id_account = locked_account.id_account
        where
            administrator.id_account != idAccount and
            locked_account.id_account is null)
    into hasAdmin;

    if hasAdmin then
        return coalesce(new, old);;
    end if;

    raise exception 'Must have at least 1 non-locked admin';
end;
$$;

/* SQL STATEMENT */
create trigger trg_has_administrator_on_locked_account
    after insert ON locked_account
    for each row
execute procedure private.trgfunc_has_administrator();

/* SQL STATEMENT */
create trigger trg_has_administrator_on_locked_account
    after delete ON administrator
    for each row
execute procedure private.trgfunc_has_administrator();

/* SQL STATEMENT */
create table color (
    id bigserial constraint pk_color primary key
);

/* SQL STATEMENT */
create table rgba (
    id bigserial not null constraint pk_rgpa primary key,
    red domain_color_component not null,
    green domain_color_component not null,
    blue domain_color_component not null,
    alpha domain_alpha_component not null,
    constraint unique_rgba_on_components unique (red, green, blue, alpha)
);

/* SQL STATEMENT */
create table color_info (
    id_color bigint not null constraint fk_color_info_to_color references color(id) on update restrict on delete restrict,
    id_color_source bigint not null constraint fk_color_info_to_color_source references color_source(id) on update restrict on delete restrict,
    id_rgba bigint not null constraint fk_color_info_to_rgba references rgba(id) on update restrict on delete restrict,
    name domain_name not null,
    color_id smallint not null,
    constraint pk_color_info primary key (id_color, id_color_source),
    constraint unique_color_info_on_color_source_and_rgba unique (id_color_source, id_rgba)
);

/* SQL STATEMENT */
create unique index unique_color_info_on_color_source_and_name on color_info(id_color_source, upper(name));

/* SQL STATEMENT */
create table bricklink_tokens (
    id_account bigint
        constraint pk_bricklink_tokens primary key
        constraint fk_bricklink_tokens_to_account references account(id) on update cascade on delete cascade,
    consumer_key domain_token not null,
    consumer_secret domain_token not null,
    token_value domain_token not null,
    token_secret domain_token not null,
    valid_until date default (now() + '1 year'::interval) not null
);

/* SQL STATEMENT */
create table brickset_tokens (
    id_account bigint
        constraint pk_brickset_tokens primary key
        constraint fk_brickset_tokens_to_account references account(id) on update cascade on delete cascade,
    api_key domain_token not null,
    username domain_username not null,
    password domain_password not null,
    valid_until date default (now() + '1 year'::interval) not null
);

/* SQL STATEMENT */
create table rebrickable_tokens (
    id_account bigint
        constraint pk_rebrickable_tokens primary key
        constraint fk_rebrickable_tokens_to_account references account(id) on update cascade on delete cascade,
    key domain_token not null,
    valid_until date default (now() + '1 year'::interval) not null
);

/* SQL STATEMENT */
create table container_type (
    id bigserial constraint pk_container_type primary key,
    name domain_name not null,
    min_lockers smallint not null,
    max_lockers smallint not null,
    number_formatting domain_format_string default '%d'::character varying not null
);

/* SQL STATEMENT */
create table item_type (
    id bigint constraint pk_item_type primary key,
    name domain_name not null,
    code char not null,
    dimension_in_stud boolean not null default true,
    constraint unique_item_type_on_code unique(code)
);
/* SQL STATEMENT */ comment on table item_type is 'item_type is considered as a table of constants, there is not sequence.';

/* SQL STATEMENT */
create table item_sub_type (
    id_base_item_type bigint not null,
    id_sub_item_type bigint not null,
    constraint pk_item_sub_type primary key (id_base_item_type, id_sub_item_type)
);

/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (1, 'Set', 'S', false);
/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (2, 'Part', 'P', true);
/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (3, 'Minifigure', 'M', false);
/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (4, 'Book', 'B', false);
/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (5, 'Gear', 'G', false);
/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (6, 'Catalog', 'C', false);
/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (7, 'Instruction', 'I', false);
/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (8, 'Original Box', 'O', false);
/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (9, 'Unsorted Lot', 'U', false);

/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (10, 'Stickers Sheet', 'K', false);
/* SQL STATEMENT */ insert into item_sub_type (id_base_item_type, id_sub_item_type) values (2, 10);

/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (11, 'Stickered Assembly', 'A', true);
/* SQL STATEMENT */ insert into item_sub_type (id_base_item_type, id_sub_item_type) values (2, 11);

/* SQL STATEMENT */ insert into item_type (id, name, code, dimension_in_stud) values (12, 'Decorated Assembly', 'D', true);
/* SQL STATEMENT */ insert into item_sub_type (id_base_item_type, id_sub_item_type) values (2, 12);

/* SQL STATEMENT */
create table allowed_item_types (
    id_container_type bigint constraint fk_allowed_item_types_to_container_type references container_type(id) on update restrict on delete restrict,
    id_item_type bigint constraint fk_allowed_item_types_to_item_type references item_type(id) on update restrict on delete restrict,
    constraint pk_allowed_item_types primary key (id_container_type, id_item_type)
);

/* SQL STATEMENT */
create table container (
    id bigserial constraint pk_container primary key,
    id_container_type bigint constraint fk_container_to_container_type references container_type on update restrict on delete restrict
);

/* SQL STATEMENT */
create table private_container (
    id_container bigint
        constraint pk_private_container primary key
        constraint fk_private_container_to_container references container(id) on update cascade on delete cascade,
    id_account bigint constraint fk_private_container_to_account references account(id) on update restrict on delete restrict
);

/* SQL STATEMENT */
create table shared_container (
    id_container bigint
        constraint pk_shared_container primary key
        constraint fk_shared_container_to_container references container(id) on update cascade on delete cascade
);

/* SQL STATEMENT */
create table locker_size (
    id bigserial constraint pk_locker_size primary key,
    name domain_name not null,
    length domain_dimension not null,
    width domain_dimension not null,
    height domain_dimension not null,
    constraint unique_dimension_on_dimensions unique (length, width, height)
);

/* SQL STATEMENT */
create table locker (
    id bigserial constraint pk_locker primary key,
    id_locker_size bigint constraint fk_locker_to_container references locker_size on update restrict on delete restrict
);

/* SQL STATEMENT */
create table container_composition (
    id_container bigint constraint fk_container_composition_to_container references container on update restrict on delete restrict,
    id_locker bigint constraint fk_container_composition_to_locker references locker on update restrict on delete restrict,
    position smallint default 0 not null,
    constraint pk_container_composition primary key (id_container, id_locker),
    constraint unique_container_composition_on_locker unique(id_locker)
);
