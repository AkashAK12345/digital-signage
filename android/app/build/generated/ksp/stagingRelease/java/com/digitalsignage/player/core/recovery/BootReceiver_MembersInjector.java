package com.digitalsignage.player.core.recovery;

import com.digitalsignage.player.core.event.PlayerEventBus;
import com.digitalsignage.player.core.logging.Logger;
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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<Logger> loggerProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  private final Provider<StartupValidator> startupValidatorProvider;

  public BootReceiver_MembersInjector(Provider<Logger> loggerProvider,
      Provider<PlayerEventBus> eventBusProvider,
      Provider<StartupValidator> startupValidatorProvider) {
    this.loggerProvider = loggerProvider;
    this.eventBusProvider = eventBusProvider;
    this.startupValidatorProvider = startupValidatorProvider;
  }

  public static MembersInjector<BootReceiver> create(Provider<Logger> loggerProvider,
      Provider<PlayerEventBus> eventBusProvider,
      Provider<StartupValidator> startupValidatorProvider) {
    return new BootReceiver_MembersInjector(loggerProvider, eventBusProvider, startupValidatorProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectLogger(instance, loggerProvider.get());
    injectEventBus(instance, eventBusProvider.get());
    injectStartupValidator(instance, startupValidatorProvider.get());
  }

  @InjectedFieldSignature("com.digitalsignage.player.core.recovery.BootReceiver.logger")
  public static void injectLogger(BootReceiver instance, Logger logger) {
    instance.logger = logger;
  }

  @InjectedFieldSignature("com.digitalsignage.player.core.recovery.BootReceiver.eventBus")
  public static void injectEventBus(BootReceiver instance, PlayerEventBus eventBus) {
    instance.eventBus = eventBus;
  }

  @InjectedFieldSignature("com.digitalsignage.player.core.recovery.BootReceiver.startupValidator")
  public static void injectStartupValidator(BootReceiver instance,
      StartupValidator startupValidator) {
    instance.startupValidator = startupValidator;
  }
}
