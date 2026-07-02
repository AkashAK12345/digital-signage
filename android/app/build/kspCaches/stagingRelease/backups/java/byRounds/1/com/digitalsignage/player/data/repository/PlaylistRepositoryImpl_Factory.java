package com.digitalsignage.player.data.repository;

import com.digitalsignage.player.core.logging.Logger;
import com.digitalsignage.player.data.local.AppDatabase;
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
public final class PlaylistRepositoryImpl_Factory implements Factory<PlaylistRepositoryImpl> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<AppDatabase> databaseProvider;

  private final Provider<RuntimeConfigStoreImpl> configStoreProvider;

  private final Provider<Logger> loggerProvider;

  public PlaylistRepositoryImpl_Factory(Provider<ApiService> apiServiceProvider,
      Provider<AppDatabase> databaseProvider, Provider<RuntimeConfigStoreImpl> configStoreProvider,
      Provider<Logger> loggerProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.databaseProvider = databaseProvider;
    this.configStoreProvider = configStoreProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public PlaylistRepositoryImpl get() {
    return newInstance(apiServiceProvider.get(), databaseProvider.get(), configStoreProvider.get(), loggerProvider.get());
  }

  public static PlaylistRepositoryImpl_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<AppDatabase> databaseProvider, Provider<RuntimeConfigStoreImpl> configStoreProvider,
      Provider<Logger> loggerProvider) {
    return new PlaylistRepositoryImpl_Factory(apiServiceProvider, databaseProvider, configStoreProvider, loggerProvider);
  }

  public static PlaylistRepositoryImpl newInstance(ApiService apiService, AppDatabase database,
      RuntimeConfigStoreImpl configStore, Logger logger) {
    return new PlaylistRepositoryImpl(apiService, database, configStore, logger);
  }
}
