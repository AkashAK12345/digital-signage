package com.digitalsignage.player.domain.playback.executor;

import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.core.logging.Logger;
import com.digitalsignage.player.domain.playback.PlaybackEngine;
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
public final class PlaylistExecutorImpl_Factory implements Factory<PlaylistExecutorImpl> {
  private final Provider<PlaybackEngine> playbackEngineProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<Logger> loggerProvider;

  public PlaylistExecutorImpl_Factory(Provider<PlaybackEngine> playbackEngineProvider,
      Provider<PlayerEventBus> eventBusProvider, Provider<Logger> loggerProvider) {
    this.playbackEngineProvider = playbackEngineProvider;
    this.eventBusProvider = eventBusProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public PlaylistExecutorImpl get() {
    return newInstance(playbackEngineProvider.get(), eventBusProvider.get(), loggerProvider.get());
  }

  public static PlaylistExecutorImpl_Factory create(Provider<PlaybackEngine> playbackEngineProvider,
      Provider<PlayerEventBus> eventBusProvider, Provider<Logger> loggerProvider) {
    return new PlaylistExecutorImpl_Factory(playbackEngineProvider, eventBusProvider, loggerProvider);
  }

  public static PlaylistExecutorImpl newInstance(PlaybackEngine playbackEngine,
      PlayerEventBus eventBus, Logger logger) {
    return new PlaylistExecutorImpl(playbackEngine, eventBus, logger);
  }
}
