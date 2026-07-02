package com.digitalsignage.player.core.health;

import android.content.Context;
import com.digitalsignage.player.data.local.AppDatabase;
import com.digitalsignage.player.domain.playback.PlaybackEngine;
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
public final class DeviceHealthCollector_Factory implements Factory<DeviceHealthCollector> {
  private final Provider<Context> contextProvider;

  private final Provider<AppDatabase> databaseProvider;

  private final Provider<PlaybackEngine> playbackEngineProvider;

  public DeviceHealthCollector_Factory(Provider<Context> contextProvider,
      Provider<AppDatabase> databaseProvider, Provider<PlaybackEngine> playbackEngineProvider) {
    this.contextProvider = contextProvider;
    this.databaseProvider = databaseProvider;
    this.playbackEngineProvider = playbackEngineProvider;
  }

  @Override
  public DeviceHealthCollector get() {
    return newInstance(contextProvider.get(), databaseProvider.get(), playbackEngineProvider.get());
  }

  public static DeviceHealthCollector_Factory create(Provider<Context> contextProvider,
      Provider<AppDatabase> databaseProvider, Provider<PlaybackEngine> playbackEngineProvider) {
    return new DeviceHealthCollector_Factory(contextProvider, databaseProvider, playbackEngineProvider);
  }

  public static DeviceHealthCollector newInstance(Context context, AppDatabase database,
      PlaybackEngine playbackEngine) {
    return new DeviceHealthCollector(context, database, playbackEngine);
  }
}
