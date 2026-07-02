package com.digitalsignage.player.player.playback;

import android.content.Context;
import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.core.logging.Logger;
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
public final class ExoPlayerEngineImpl_Factory implements Factory<ExoPlayerEngineImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<Logger> loggerProvider;

  public ExoPlayerEngineImpl_Factory(Provider<Context> contextProvider,
      Provider<PlayerEventBus> eventBusProvider, Provider<Logger> loggerProvider) {
    this.contextProvider = contextProvider;
    this.eventBusProvider = eventBusProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public ExoPlayerEngineImpl get() {
    return newInstance(contextProvider.get(), eventBusProvider.get(), loggerProvider.get());
  }

  public static ExoPlayerEngineImpl_Factory create(Provider<Context> contextProvider,
      Provider<PlayerEventBus> eventBusProvider, Provider<Logger> loggerProvider) {
    return new ExoPlayerEngineImpl_Factory(contextProvider, eventBusProvider, loggerProvider);
  }

  public static ExoPlayerEngineImpl newInstance(Context context, PlayerEventBus eventBus,
      Logger logger) {
    return new ExoPlayerEngineImpl(context, eventBus, logger);
  }
}
