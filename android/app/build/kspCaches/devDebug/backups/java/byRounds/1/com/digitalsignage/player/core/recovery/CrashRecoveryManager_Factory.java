package com.digitalsignage.player.core.recovery;

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
public final class CrashRecoveryManager_Factory implements Factory<CrashRecoveryManager> {
  private final Provider<Context> contextProvider;

  private final Provider<Logger> loggerProvider;

  private final Provider<PlayerEventBus> eventBusProvider;

  public CrashRecoveryManager_Factory(Provider<Context> contextProvider,
      Provider<Logger> loggerProvider, Provider<PlayerEventBus> eventBusProvider) {
    this.contextProvider = contextProvider;
    this.loggerProvider = loggerProvider;
    this.eventBusProvider = eventBusProvider;
  }

  @Override
  public CrashRecoveryManager get() {
    return newInstance(contextProvider.get(), loggerProvider.get(), eventBusProvider.get());
  }

  public static CrashRecoveryManager_Factory create(Provider<Context> contextProvider,
      Provider<Logger> loggerProvider, Provider<PlayerEventBus> eventBusProvider) {
    return new CrashRecoveryManager_Factory(contextProvider, loggerProvider, eventBusProvider);
  }

  public static CrashRecoveryManager newInstance(Context context, Logger logger,
      PlayerEventBus eventBus) {
    return new CrashRecoveryManager(context, logger, eventBus);
  }
}
