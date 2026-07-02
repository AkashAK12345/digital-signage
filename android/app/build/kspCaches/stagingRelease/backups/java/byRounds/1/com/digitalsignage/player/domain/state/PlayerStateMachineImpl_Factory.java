package com.digitalsignage.player.domain.state;

import com.digitalsignage.player.core.logging.Logger;
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
public final class PlayerStateMachineImpl_Factory implements Factory<PlayerStateMachineImpl> {
  private final Provider<Logger> loggerProvider;

  public PlayerStateMachineImpl_Factory(Provider<Logger> loggerProvider) {
    this.loggerProvider = loggerProvider;
  }

  @Override
  public PlayerStateMachineImpl get() {
    return newInstance(loggerProvider.get());
  }

  public static PlayerStateMachineImpl_Factory create(Provider<Logger> loggerProvider) {
    return new PlayerStateMachineImpl_Factory(loggerProvider);
  }

  public static PlayerStateMachineImpl newInstance(Logger logger) {
    return new PlayerStateMachineImpl(logger);
  }
}
