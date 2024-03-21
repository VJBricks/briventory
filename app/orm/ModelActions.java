package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

import java.util.List;

public final class ModelActions {

  private ModelActions() { /* Utility class. */ }

  /*public static <V, R extends UpdatableRecord<R>,
                    P extends PersistableModel1<R>
                          & ValidatableModel<V>
                          & DeletableModel<V, R>> void fromPersistableAndDeletable(final List<ModelAction> actions,
                                                                                   final P oldModel,
                                                                                   final P newModel) {
    if (newModel == null && oldModel != null)
      actions.add(new DeleteAction<>(oldModel));
    if (oldModel != null)
      actions.add(new PersistAction1<>(newModel));
  }*/

  public static <V, R extends UpdatableRecord<R>, M extends Model> void from(final M oldModel, final M newModel) {

  }

  /*public static <V, R extends UpdatableRecord<R>,
                    P extends PersistableModel1<R> & ValidatableModel<V>> void fromPersistable(
      final List<ModelAction> actions, final P newModel) {
    if (newModel != null)
      actions.add(new PersistAction1<>(newModel));
  }*/

  /*public static <V, R extends UpdatableRecord<R>, P extends DeletableModel<V, R>> void fromDeletable(
      final List<ModelAction> actions, final P oldModel, final P newModel) {
    if (newModel == null && oldModel != null)
      actions.add(new DeleteAction<>(oldModel));
  }*/

  public static <K, V> List<Action> fromLazyLoader(final DSLContext dslContext,
                                                   final LazyLoader<K, V> lazyLoader) {
    return lazyLoader.createActions(dslContext);
  }

}
