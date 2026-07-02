package com.digitalsignage.player.core.event;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class PlayerEventBus_Factory implements Factory<PlayerEventBus> {
  @Override
  public PlayerEventBus get() {
    return newInstance();
  }

  public static PlayerEventBus_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PlayerEventBus newInstance() {
    return new PlayerEventBus();
  }

  private static final class InstanceHolder {
    private static final PlayerEventBus_Factory INSTANCE = new PlayerEventBus_Factory();
  }
}
