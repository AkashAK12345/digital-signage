package com.digitalsignage.player.data.repository;

import com.digitalsignage.player.core.identity.DeviceIdentityManager;
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
public final class DeviceRepositoryImpl_Factory implements Factory<DeviceRepositoryImpl> {
  private final Provider<RuntimeConfigStoreImpl> runtimeConfigStoreProvider;

  private final Provider<DeviceIdentityManager> identityManagerProvider;

  private final Provider<Logger> loggerProvider;

  public DeviceRepositoryImpl_Factory(Provider<RuntimeConfigStoreImpl> runtimeConfigStoreProvider,
      Provider<DeviceIdentityManager> identityManagerProvider, Provider<Logger> loggerProvider) {
    this.runtimeConfigStoreProvider = runtimeConfigStoreProvider;
    this.identityManagerProvider = identityManagerProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public DeviceRepositoryImpl get() {
    return newInstance(runtimeConfigStoreProvider.get(), identityManagerProvider.get(), loggerProvider.get());
  }

  public static DeviceRepositoryImpl_Factory create(
      Provider<RuntimeConfigStoreImpl> runtimeConfigStoreProvider,
      Provider<DeviceIdentityManager> identityManagerProvider, Provider<Logger> loggerProvider) {
    return new DeviceRepositoryImpl_Factory(runtimeConfigStoreProvider, identityManagerProvider, loggerProvider);
  }

  public static DeviceRepositoryImpl newInstance(RuntimeConfigStoreImpl runtimeConfigStore,
      DeviceIdentityManager identityManager, Logger logger) {
    return new DeviceRepositoryImpl(runtimeConfigStore, identityManager, logger);
  }
}
