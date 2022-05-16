package orm;

import org.jooq.UpdatableRecord;
import orm.models.PersistableModel1;
import orm.models.PersistableModel2;
import orm.models.PersistableModel3;
import orm.models.PersistableModel4;
import orm.models.PersistableModel5;
import orm.models.ValidatableModel;

final class ModelPersistorFactory {

  private ModelPersistorFactory() { /* No-Op, utility class. */ }

  static <V, R extends UpdatableRecord<R>,
             P extends PersistableModel1<R> & ValidatableModel<V>> ModelPersistor1<V, R, P> of(
      final P persistableModel) {
    return new ModelPersistor1<>(persistableModel);
  }

  static <V, R1 extends UpdatableRecord<R1>,
             R2 extends UpdatableRecord<R2>,
             P extends PersistableModel2<R1, R2> & ValidatableModel<V>> ModelPersistor2<V, R1, R2, P> of(
      final P persistableModel) {
    return new ModelPersistor2<>(persistableModel);
  }

  static <V, R1 extends UpdatableRecord<R1>,
             R2 extends UpdatableRecord<R2>,
             R3 extends UpdatableRecord<R3>,
             P extends PersistableModel3<R1, R2, R3> & ValidatableModel<V>> ModelPersistor3<V, R1, R2, R3, P> of(
      final P persistableModel) {
    return new ModelPersistor3<>(persistableModel);
  }

  static <V, R1 extends UpdatableRecord<R1>,
             R2 extends UpdatableRecord<R2>,
             R3 extends UpdatableRecord<R3>,
             R4 extends UpdatableRecord<R4>,
             P extends PersistableModel4<R1, R2, R3, R4> & ValidatableModel<V>> ModelPersistor4<V, R1,
                                                                                                   R2,
                                                                                                   R3,
                                                                                                   R4,
                                                                                                   P> of(
      final P persistableModel) {
    return new ModelPersistor4<>(persistableModel);
  }

  static <V, R1 extends UpdatableRecord<R1>,
             R2 extends UpdatableRecord<R2>,
             R3 extends UpdatableRecord<R3>,
             R4 extends UpdatableRecord<R4>,
             R5 extends UpdatableRecord<R5>,
             P extends PersistableModel5<R1, R2, R3, R4, R5> & ValidatableModel<V>> ModelPersistor5<V, R1,
                                                                                                       R2,
                                                                                                       R3,
                                                                                                       R4,
                                                                                                       R5,
                                                                                                       P> of(
      final P persistableModel) {
    return new ModelPersistor5<>(persistableModel);
  }

}
