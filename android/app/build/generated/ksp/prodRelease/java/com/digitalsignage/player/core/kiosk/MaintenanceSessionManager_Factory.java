package com.digitalsignage.player.core.kiosk;

import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.core.logging.Logger;
import com.digitalsignage.player.data.local.datastore.RuntimeConfigStoreImpl;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class MaintenanceSessionManager_Factory implements Factory<MaintenanceSessionManager> {
  private final Provider<RuntimeConfigStoreImpl> dataStoreProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<Logger> loggerProvider;

  public MaintenanceSessionManager_Factory(Provider<RuntimeConfigStoreImpl> dataStoreProvider,
      Provider<PlayerEventBus> eventBusProvider, Provider<Logger> loggerProvider) {
    this.dataStoreProvider = dataStoreProvider;
    this.eventBusProvider = eventBusProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public MaintenanceSessionManager get() {
    return newInstance(dataStoreProvider.get(), eventBusProvider.get(), loggerProvider.get());
  }

  public static MaintenanceSessionManager_Factory create(
      Provider<RuntimeConfigStoreImpl> dataStoreProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<Logger> loggerProvider) {
    return new MaintenanceSessionManager_Factory(dataStoreProvider, eventBusProvider, loggerProvider);
  }

  public static MaintenanceSessionManager newInstance(RuntimeConfigStoreImpl dataStore,
      PlayerEventBus eventBus, Logger logger) {
    return new MaintenanceSessionManager(dataStore, eventBus, logger);
  }
}
