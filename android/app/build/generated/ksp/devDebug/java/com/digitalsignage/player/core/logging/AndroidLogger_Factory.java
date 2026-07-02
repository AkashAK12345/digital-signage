package com.digitalsignage.player.core.logging;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AndroidLogger_Factory implements Factory<AndroidLogger> {
  @Override
  public AndroidLogger get() {
    return newInstance();
  }

  public static AndroidLogger_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AndroidLogger newInstance() {
    return new AndroidLogger();
  }

  private static final class InstanceHolder {
    private static final AndroidLogger_Factory INSTANCE = new AndroidLogger_Factory();
  }
}
