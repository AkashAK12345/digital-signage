package com.digitalsignage.player.core.utils;

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
public final class FileValidator_Factory implements Factory<FileValidator> {
  @Override
  public FileValidator get() {
    return newInstance();
  }

  public static FileValidator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FileValidator newInstance() {
    return new FileValidator();
  }

  private static final class InstanceHolder {
    private static final FileValidator_Factory INSTANCE = new FileValidator_Factory();
  }
}
