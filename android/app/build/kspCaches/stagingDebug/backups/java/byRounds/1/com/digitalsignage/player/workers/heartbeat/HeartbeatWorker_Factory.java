package com.digitalsignage.player.workers.heartbeat;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.core.logging.Logger;
import com.digitalsignage.player.domain.repository.HeartbeatRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class HeartbeatWorker_Factory {
  private final Provider<HeartbeatRepository> heartbeatRepositoryProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<Logger> loggerProvider;

  public HeartbeatWorker_Factory(Provider<HeartbeatRepository> heartbeatRepositoryProvider,
      Provider<PlayerEventBus> eventBusProvider, Provider<Logger> loggerProvider) {
    this.heartbeatRepositoryProvider = heartbeatRepositoryProvider;
    this.eventBusProvider = eventBusProvider;
    this.loggerProvider = loggerProvider;
  }

  public HeartbeatWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, heartbeatRepositoryProvider.get(), eventBusProvider.get(), loggerProvider.get());
  }

  public static HeartbeatWorker_Factory create(
      Provider<HeartbeatRepository> heartbeatRepositoryProvider,
      Provider<PlayerEventBus> eventBusProvider, Provider<Logger> loggerProvider) {
    return new HeartbeatWorker_Factory(heartbeatRepositoryProvider, eventBusProvider, loggerProvider);
  }

  public static HeartbeatWorker newInstance(Context context, WorkerParameters workerParams,
      HeartbeatRepository heartbeatRepository, PlayerEventBus eventBus, Logger logger) {
    return new HeartbeatWorker(context, workerParams, heartbeatRepository, eventBus, logger);
  }
}
