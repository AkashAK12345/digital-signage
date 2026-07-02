package com.digitalsignage.player.domain.orchestrator;

import android.content.Context;
import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.core.kiosk.KioskManager;
import com.digitalsignage.player.core.kiosk.MaintenanceSessionManager;
import com.digitalsignage.player.core.logging.Logger;
import com.digitalsignage.player.core.network.NetworkMonitor;
import com.digitalsignage.player.core.recovery.CrashRecoveryManager;
import com.digitalsignage.player.core.recovery.StartupValidator;
import com.digitalsignage.player.data.repository.DeviceRepositoryImpl;
import com.digitalsignage.player.domain.playback.PlaylistExecutor;
import com.digitalsignage.player.domain.repository.PlaylistRepository;
import com.digitalsignage.player.domain.state.PlayerStateMachine;
import com.digitalsignage.player.workers.download.DownloadManager;
import com.digitalsignage.player.workers.heartbeat.HeartbeatManager;
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
public final class PlayerOrchestratorImpl_Factory implements Factory<PlayerOrchestratorImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<PlayerStateMachine> stateMachineProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<Logger> loggerProvider;

  private final Provider<DeviceRepositoryImpl> deviceRepositoryProvider;

  private final Provider<PlaylistRepository> playlistRepositoryProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  private final Provider<DownloadManager> downloadManagerProvider;

  private final Provider<PlaylistExecutor> playlistExecutorProvider;

  private final Provider<HeartbeatManager> heartbeatManagerProvider;

  private final Provider<StartupValidator> startupValidatorProvider;

  private final Provider<CrashRecoveryManager> crashRecoveryManagerProvider;

  private final Provider<KioskManager> kioskManagerProvider;

  private final Provider<MaintenanceSessionManager> maintenanceSessionManagerProvider;

  public PlayerOrchestratorImpl_Factory(Provider<Context> contextProvider,
      Provider<PlayerStateMachine> stateMachineProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<Logger> loggerProvider, Provider<DeviceRepositoryImpl> deviceRepositoryProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<DownloadManager> downloadManagerProvider,
      Provider<PlaylistExecutor> playlistExecutorProvider,
      Provider<HeartbeatManager> heartbeatManagerProvider,
      Provider<StartupValidator> startupValidatorProvider,
      Provider<CrashRecoveryManager> crashRecoveryManagerProvider,
      Provider<KioskManager> kioskManagerProvider,
      Provider<MaintenanceSessionManager> maintenanceSessionManagerProvider) {
    this.contextProvider = contextProvider;
    this.stateMachineProvider = stateMachineProvider;
    this.eventBusProvider = eventBusProvider;
    this.loggerProvider = loggerProvider;
    this.deviceRepositoryProvider = deviceRepositoryProvider;
    this.playlistRepositoryProvider = playlistRepositoryProvider;
    this.networkMonitorProvider = networkMonitorProvider;
    this.downloadManagerProvider = downloadManagerProvider;
    this.playlistExecutorProvider = playlistExecutorProvider;
    this.heartbeatManagerProvider = heartbeatManagerProvider;
    this.startupValidatorProvider = startupValidatorProvider;
    this.crashRecoveryManagerProvider = crashRecoveryManagerProvider;
    this.kioskManagerProvider = kioskManagerProvider;
    this.maintenanceSessionManagerProvider = maintenanceSessionManagerProvider;
  }

  @Override
  public PlayerOrchestratorImpl get() {
    return newInstance(contextProvider.get(), stateMachineProvider.get(), eventBusProvider.get(), loggerProvider.get(), deviceRepositoryProvider.get(), playlistRepositoryProvider.get(), networkMonitorProvider.get(), downloadManagerProvider.get(), playlistExecutorProvider.get(), heartbeatManagerProvider.get(), startupValidatorProvider.get(), crashRecoveryManagerProvider.get(), kioskManagerProvider.get(), maintenanceSessionManagerProvider.get());
  }

  public static PlayerOrchestratorImpl_Factory create(Provider<Context> contextProvider,
      Provider<PlayerStateMachine> stateMachineProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<Logger> loggerProvider, Provider<DeviceRepositoryImpl> deviceRepositoryProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<DownloadManager> downloadManagerProvider,
      Provider<PlaylistExecutor> playlistExecutorProvider,
      Provider<HeartbeatManager> heartbeatManagerProvider,
      Provider<StartupValidator> startupValidatorProvider,
      Provider<CrashRecoveryManager> crashRecoveryManagerProvider,
      Provider<KioskManager> kioskManagerProvider,
      Provider<MaintenanceSessionManager> maintenanceSessionManagerProvider) {
    return new PlayerOrchestratorImpl_Factory(contextProvider, stateMachineProvider, eventBusProvider, loggerProvider, deviceRepositoryProvider, playlistRepositoryProvider, networkMonitorProvider, downloadManagerProvider, playlistExecutorProvider, heartbeatManagerProvider, startupValidatorProvider, crashRecoveryManagerProvider, kioskManagerProvider, maintenanceSessionManagerProvider);
  }

  public static PlayerOrchestratorImpl newInstance(Context context, PlayerStateMachine stateMachine,
      PlayerEventBus eventBus, Logger logger, DeviceRepositoryImpl deviceRepository,
      PlaylistRepository playlistRepository, NetworkMonitor networkMonitor,
      DownloadManager downloadManager, PlaylistExecutor playlistExecutor,
      HeartbeatManager heartbeatManager, StartupValidator startupValidator,
      CrashRecoveryManager crashRecoveryManager, KioskManager kioskManager,
      MaintenanceSessionManager maintenanceSessionManager) {
    return new PlayerOrchestratorImpl(context, stateMachine, eventBus, logger, deviceRepository, playlistRepository, networkMonitor, downloadManager, playlistExecutor, heartbeatManager, startupValidator, crashRecoveryManager, kioskManager, maintenanceSessionManager);
  }
}
