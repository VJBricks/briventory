package junit5;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public final class J5Shutdown implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {
  private static final Lock LOCK = new ReentrantLock();
  private static volatile boolean started = false;

  @Override
  public void beforeAll(final ExtensionContext context) {
    LOCK.lock();
    try {
      if (!started) {
        started = true;
        ApplicationHandler.getInstance().startApplication();
        context.getRoot().getStore(GLOBAL).put("Application Shutdown", this);
      }
    } finally {
      LOCK.unlock();
    }

  }

  @Override
  public void close() throws Throwable {
    ApplicationHandler.getInstance().stopApplication();
  }

}
