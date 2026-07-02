package com.digitalsignage.player.ui;

import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.data.local.datastore.RuntimeConfigStoreImpl;
import com.digitalsignage.player.domain.orchestrator.PlayerOrchestrator;
import com.digitalsignage.player.domain.playback.PlaybackEngine;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PlaybackActivity_MembersInjector implements MembersInjector<PlaybackActivity> {
  private final Provider<PlayerOrchestrator> playerOrchestratorProvider;

  private final Provider<PlaybackEngine> playbackEngineProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<RuntimeConfigStoreImpl> runtimeConfigStoreProvider;

  public PlaybackActivity_MembersInjector(Provider<PlayerOrchestrator> playerOrchestratorProvider,
      Provider<PlaybackEngine> playbackEngineProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<RuntimeConfigStoreImpl> runtimeConfigStoreProvider) {
    this.playerOrchestratorProvider = playerOrchestratorProvider;
    this.playbackEngineProvider = playbackEngineProvider;
    this.eventBusProvider = eventBusProvider;
    this.runtimeConfigStoreProvider = runtimeConfigStoreProvider;
  }

  public static MembersInjector<PlaybackActivity> create(
      Provider<PlayerOrchestrator> playerOrchestratorProvider,
      Provider<PlaybackEngine> playbackEngineProvider, Provider<PlayerEventBus> eventBusProvider,
      Provider<RuntimeConfigStoreImpl> runtimeConfigStoreProvider) {
    return new PlaybackActivity_MembersInjector(playerOrchestratorProvider, playbackEngineProvider, eventBusProvider, runtimeConfigStoreProvider);
  }

  @Override
  public void injectMembers(PlaybackActivity instance) {
    injectPlayerOrchestrator(instance, playerOrchestratorProvider.get());
    injectPlaybackEngine(instance, playbackEngineProvider.get());
    injectEventBus(instance, eventBusProvider.get());
    injectRuntimeConfigStore(instance, runtimeConfigStoreProvider.get());
  }

  @InjectedFieldSignature("com.digitalsignage.player.ui.PlaybackActivity.playerOrchestrator")
  public static void injectPlayerOrchestrator(PlaybackActivity instance,
      PlayerOrchestrator playerOrchestrator) {
    instance.playerOrchestrator = playerOrchestrator;
  }

  @InjectedFieldSignature("com.digitalsignage.player.ui.PlaybackActivity.playbackEngine")
  public static void injectPlaybackEngine(PlaybackActivity instance,
      PlaybackEngine playbackEngine) {
    instance.playbackEngine = playbackEngine;
  }

  @InjectedFieldSignature("com.digitalsignage.player.ui.PlaybackActivity.eventBus")
  public static void injectEventBus(PlaybackActivity instance, PlayerEventBus eventBus) {
    instance.eventBus = eventBus;
  }

  @InjectedFieldSignature("com.digitalsignage.player.ui.PlaybackActivity.runtimeConfigStore")
  public static void injectRuntimeConfigStore(PlaybackActivity instance,
      RuntimeConfigStoreImpl runtimeConfigStore) {
    instance.runtimeConfigStore = runtimeConfigStore;
  }
}
