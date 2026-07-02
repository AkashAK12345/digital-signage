package com.digitalsignage.player.workers.heartbeat;

import android.content.Context;
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
public final class HeartbeatManager_Factory implements Factory<HeartbeatManager> {
  private final Provider<Context> contextProvider;

  private final Provider<RuntimeConfigStoreImpl> configStoreProvider;

  private final Provider<Logger> loggerProvider;

  public HeartbeatManager_Factory(Provider<Context> contextProvider,
      Provider<RuntimeConfigStoreImpl> configStoreProvider, Provider<Logger> loggerProvider) {
    this.contextProvider = contextProvider;
    this.configStoreProvider = configStoreProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public HeartbeatManager get() {
    return newInstance(contextProvider.get(), configStoreProvider.get(), loggerProvider.get());
  }

  public static HeartbeatManager_Factory create(Provider<Context> contextProvider,
      Provider<RuntimeConfigStoreImpl> configStoreProvider, Provider<Logger> loggerProvider) {
    return new HeartbeatManager_Factory(contextProvider, configStoreProvider, loggerProvider);
  }

  public static HeartbeatManager newInstance(Context context, RuntimeConfigStoreImpl configStore,
      Logger logger) {
    return new HeartbeatManager(context, configStore, logger);
  }
}
