package orm;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code RepositoryHandler} acts as a dictionary of {@link Repository} instances. {@code RepositoryHandler} is also the
 * main entry point to build any {@link Repository} instances.
 *
 * <pre><code>
 *   public final FoosRepository extends Repository<Foo> { ... }
 *
 *   PersistenceContext persistenceContext = ...
 *   FoosRepository foosRepository = RepositoryHandler.of(FoosRepository.class);
 * </code></pre>
 *
 * <p>{@link RepositoriesHandler#of(Class)} throws a {@link RepositoryException} if the repository does not provide a
 * constructor accepting a {@link PersistenceContext} argument.</p>
 */
@Singleton
public final class RepositoriesHandler {

  // *******************************************************************************************************************
  // Singleton
  // *******************************************************************************************************************
  /**
   * The unique instance of {@link RepositoriesHandler}. Even if set as {@link Singleton}, this will ensure that only
   * one instance exists.
   */
  private static RepositoriesHandler uniqueInstance = null;

  /**
   * Retrieves the {@link Repository} instance, of specific type {@link R}. If no repository corresponding to the type
   * given has been found in this handler, it will be injected into the dictionary and returned to the caller.
   *
   * @param repositoryType the specific type of {@link Repository} to retrieve.
   * @param <R> the specific type of {@link Repository}.
   *
   * @return a {@link Repository} instance, corresponding to {@link R}.
   *
   * @throws RepositoryException if {@link R} does not provide a constructor that take a {@link PersistenceContext}
   * argument.
   */
  public static <R extends Repository<?>> R of(final Class<R> repositoryType) {
    if (uniqueInstance == null) {
      uniqueInstance = Guice.createInjector().getInstance(RepositoriesHandler.class);
    }
    return uniqueInstance.injectAndReturn(repositoryType);
  }

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link Injector} to create instances of {@link Repository}. */
  private final Injector injector;
  /** The dictionary of repositories. */
  private final Map<Class<? extends Repository<?>>, Repository<?>> repositories;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link RepositoriesHandler}. This constructor is private, avoiding the instance creation
   * outside the single instance.
   *
   * @param injector the {@link Injector} to create instances of {@link Repository}.
   */
  @Inject
  private RepositoriesHandler(final Injector injector) {
    repositories = new HashMap<>();
    this.injector = injector;
  }

  // *******************************************************************************************************************
  // Repositories Dictionary Matters
  // *******************************************************************************************************************

  /** The error message, if an instantiation error occurs. */
  private static final String INSTANTIATION_ERROR =
      "The repository \"%s\" cannot be instanced. The implementation must have a constructor that takes a " +
      "PersistenceContext argument";

  /**
   * Retrieves the {@link Repository} instance, of specific type {@link R}, that is linked with the
   * {@link PersistenceContext} given.
   * <p>If the {@link PersistenceContext} is met for the first time, a new entry will be created in the dictionary.</p>
   * <p>If no {@link Repository} instance exists, a new instance will be created.</p>
   *
   * @param repositoryType the specific type of {@link Repository} to retrieve.
   * @param <R> the specific type of {@link Repository}.
   *
   * @return a {@link Repository} instance, corresponding to {@link R}.
   *
   * @throws RepositoryException if {@link R} does not provide a constructor that take a {@link PersistenceContext}
   * argument.
   */
  @SuppressWarnings("unchecked")
  private <R extends Repository<?>> R injectAndReturn(final Class<R> repositoryType) {
    synchronized (repositories) {
      try {
        repositories.computeIfAbsent(repositoryType, injector::getInstance);
        /* Return the existing repository, if it exists. */
        if (repositories.containsKey(repositoryType)) return (R) repositories.get(repositoryType);
      } catch (Exception e) {
        throw new RepositoryException(String.format(INSTANTIATION_ERROR, repositoryType.getName()), e);
      }
    }
    throw new RepositoryException(String.format(INSTANTIATION_ERROR, repositoryType.getName()));
  }

}
