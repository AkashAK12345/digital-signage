package com.digitalsignage.player.storage;

import android.content.Context;
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
public final class DeviceDataStore_Factory implements Factory<DeviceDataStore> {
  private final Provider<Context> contextProvider;

  public DeviceDataStore_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DeviceDataStore get() {
    return newInstance(contextProvider.get());
  }

  public static DeviceDataStore_Factory create(Provider<Context> contextProvider) {
    return new DeviceDataStore_Factory(contextProvider);
  }

  public static DeviceDataStore newInstance(Context context) {
    return new DeviceDataStore(context);
  }
}
