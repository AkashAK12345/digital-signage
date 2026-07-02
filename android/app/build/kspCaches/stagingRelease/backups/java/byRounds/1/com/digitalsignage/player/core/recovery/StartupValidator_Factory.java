package com.digitalsignage.player.core.recovery;

import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.core.logging.Logger;
import com.digitalsignage.player.core.storage.StorageManager;
import com.digitalsignage.player.core.utils.FileValidator;
import com.digitalsignage.player.data.local.AppDatabase;
import com.digitalsignage.player.workers.download.DownloadManager;
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
public final class StartupValidator_Factory implements Factory<StartupValidator> {
  private final Provider<AppDatabase> databaseProvider;

  private final Provider<StorageManager> storageManagerProvider;

  private final Provider<FileValidator> fileValidatorProvider;

  private final Provider<DownloadManager> downloadManagerProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<Logger> loggerProvider;

  public StartupValidator_Factory(Provider<AppDatabase> databaseProvider,
      Provider<StorageManager> storageManagerProvider,
      Provider<FileValidator> fileValidatorProvider,
      Provider<DownloadManager> downloadManagerProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<Logger> loggerProvider) {
    this.databaseProvider = databaseProvider;
    this.storageManagerProvider = storageManagerProvider;
    this.fileValidatorProvider = fileValidatorProvider;
    this.downloadManagerProvider = downloadManagerProvider;
    this.eventBusProvider = eventBusProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public StartupValidator get() {
    return newInstance(databaseProvider.get(), storageManagerProvider.get(), fileValidatorProvider.get(), downloadManagerProvider.get(), eventBusProvider.get(), loggerProvider.get());
  }

  public static StartupValidator_Factory create(Provider<AppDatabase> databaseProvider,
      Provider<StorageManager> storageManagerProvider,
      Provider<FileValidator> fileValidatorProvider,
      Provider<DownloadManager> downloadManagerProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<Logger> loggerProvider) {
    return new StartupValidator_Factory(databaseProvider, storageManagerProvider, fileValidatorProvider, downloadManagerProvider, eventBusProvider, loggerProvider);
  }

  public static StartupValidator newInstance(AppDatabase database, StorageManager storageManager,
      FileValidator fileValidator, DownloadManager downloadManager, PlayerEventBus eventBus,
      Logger logger) {
    return new StartupValidator(database, storageManager, fileValidator, downloadManager, eventBus, logger);
  }
}
