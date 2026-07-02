package com.digitalsignage.player.core.network;

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
public final class NetworkMonitorImpl_Factory implements Factory<NetworkMonitorImpl> {
  @Override
  public NetworkMonitorImpl get() {
    return newInstance();
  }

  public static NetworkMonitorImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NetworkMonitorImpl newInstance() {
    return new NetworkMonitorImpl();
  }

  private static final class InstanceHolder {
    private static final NetworkMonitorImpl_Factory INSTANCE = new NetworkMonitorImpl_Factory();
  }
}
