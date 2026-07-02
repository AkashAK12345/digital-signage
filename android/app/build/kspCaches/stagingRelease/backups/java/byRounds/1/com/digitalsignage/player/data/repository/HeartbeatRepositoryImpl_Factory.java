package com.digitalsignage.player.data.repository;

import com.digitalsignage.player.core.health.DeviceHealthCollector;
import com.digitalsignage.player.core.logging.Logger;
import com.digitalsignage.player.data.local.datastore.RuntimeConfigStoreImpl;
import com.digitalsignage.player.data.remote.ApiService;
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
public final class HeartbeatRepositoryImpl_Factory implements Factory<HeartbeatRepositoryImpl> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<DeviceHealthCollector> healthCollectorProvider;

  private final Provider<RuntimeConfigStoreImpl> configStoreProvider;

  private final Provider<Logger> loggerProvider;

  public HeartbeatRepositoryImpl_Factory(Provider<ApiService> apiServiceProvider,
      Provider<DeviceHealthCollector> healthCollectorProvider,
      Provider<RuntimeConfigStoreImpl> configStoreProvider, Provider<Logger> loggerProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.healthCollectorProvider = healthCollectorProvider;
    this.configStoreProvider = configStoreProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public HeartbeatRepositoryImpl get() {
    return newInstance(apiServiceProvider.get(), healthCollectorProvider.get(), configStoreProvider.get(), loggerProvider.get());
  }

  public static HeartbeatRepositoryImpl_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<DeviceHealthCollector> healthCollectorProvider,
      Provider<RuntimeConfigStoreImpl> configStoreProvider, Provider<Logger> loggerProvider) {
    return new HeartbeatRepositoryImpl_Factory(apiServiceProvider, healthCollectorProvider, configStoreProvider, loggerProvider);
  }

  public static HeartbeatRepositoryImpl newInstance(ApiService apiService,
      DeviceHealthCollector healthCollector, RuntimeConfigStoreImpl configStore, Logger logger) {
    return new HeartbeatRepositoryImpl(apiService, healthCollector, configStore, logger);
  }
}
