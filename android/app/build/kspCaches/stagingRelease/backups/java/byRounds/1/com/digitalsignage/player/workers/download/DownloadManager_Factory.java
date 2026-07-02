package com.digitalsignage.player.workers.download;

import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.core.logging.Logger;
import com.digitalsignage.player.core.storage.StorageManager;
import com.digitalsignage.player.core.utils.FileValidator;
import com.digitalsignage.player.data.local.AppDatabase;
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
public final class DownloadManager_Factory implements Factory<DownloadManager> {
  private final Provider<AppDatabase> databaseProvider;

  private final Provider<StorageManager> storageManagerProvider;

  private final Provider<FileValidator> fileValidatorProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<Logger> loggerProvider;

  public DownloadManager_Factory(Provider<AppDatabase> databaseProvider,
      Provider<StorageManager> storageManagerProvider,
      Provider<FileValidator> fileValidatorProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<Logger> loggerProvider) {
    this.databaseProvider = databaseProvider;
    this.storageManagerProvider = storageManagerProvider;
    this.fileValidatorProvider = fileValidatorProvider;
    this.eventBusProvider = eventBusProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public DownloadManager get() {
    return newInstance(databaseProvider.get(), storageManagerProvider.get(), fileValidatorProvider.get(), eventBusProvider.get(), loggerProvider.get());
  }

  public static DownloadManager_Factory create(Provider<AppDatabase> databaseProvider,
      Provider<StorageManager> storageManagerProvider,
      Provider<FileValidator> fileValidatorProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<Logger> loggerProvider) {
    return new DownloadManager_Factory(databaseProvider, storageManagerProvider, fileValidatorProvider, eventBusProvider, loggerProvider);
  }

  public static DownloadManager newInstance(AppDatabase database, StorageManager storageManager,
      FileValidator fileValidator, PlayerEventBus eventBus, Logger logger) {
    return new DownloadManager(database, storageManager, fileValidator, eventBus, logger);
  }
}
