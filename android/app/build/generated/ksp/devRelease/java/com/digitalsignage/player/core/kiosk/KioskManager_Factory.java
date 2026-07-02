package com.digitalsignage.player.core.kiosk;

import android.content.Context;
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
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class KioskManager_Factory implements Factory<KioskManager> {
  private final Provider<Context> contextProvider;

  private final Provider<RuntimeConfigStoreImpl> dataStoreProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<Logger> loggerProvider;

  public KioskManager_Factory(Provider<Context> contextProvider,
      Provider<RuntimeConfigStoreImpl> dataStoreProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<Logger> loggerProvider) {
    this.contextProvider = contextProvider;
    this.dataStoreProvider = dataStoreProvider;
    this.eventBusProvider = eventBusProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public KioskManager get() {
    return newInstance(contextProvider.get(), dataStoreProvider.get(), eventBusProvider.get(), loggerProvider.get());
  }

  public static KioskManager_Factory create(Provider<Context> contextProvider,
      Provider<RuntimeConfigStoreImpl> dataStoreProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<Logger> loggerProvider) {
    return new KioskManager_Factory(contextProvider, dataStoreProvider, eventBusProvider, loggerProvider);
  }

  public static KioskManager newInstance(Context context, RuntimeConfigStoreImpl dataStore,
      PlayerEventBus eventBus, Logger logger) {
    return new KioskManager(context, dataStore, eventBus, logger);
  }
}
