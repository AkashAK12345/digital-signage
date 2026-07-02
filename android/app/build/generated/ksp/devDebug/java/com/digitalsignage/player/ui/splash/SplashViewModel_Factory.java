package com.digitalsignage.player.ui.splash;

import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.domain.state.PlayerStateMachine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SplashViewModel_Factory implements Factory<SplashViewModel> {
  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<PlayerStateMachine> stateMachineProvider;

  public SplashViewModel_Factory(Provider<PlayerEventBus> eventBusProvider,
      Provider<PlayerStateMachine> stateMachineProvider) {
    this.eventBusProvider = eventBusProvider;
    this.stateMachineProvider = stateMachineProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(eventBusProvider.get(), stateMachineProvider.get());
  }

  public static SplashViewModel_Factory create(Provider<PlayerEventBus> eventBusProvider,
      Provider<PlayerStateMachine> stateMachineProvider) {
    return new SplashViewModel_Factory(eventBusProvider, stateMachineProvider);
  }

  public static SplashViewModel newInstance(PlayerEventBus eventBus,
      PlayerStateMachine stateMachine) {
    return new SplashViewModel(eventBus, stateMachine);
  }
}
