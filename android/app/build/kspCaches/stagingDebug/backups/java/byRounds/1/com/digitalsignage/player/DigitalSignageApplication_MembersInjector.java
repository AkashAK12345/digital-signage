package com.digitalsignage.player;

import androidx.hilt.work.HiltWorkerFactory;
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
public final class DigitalSignageApplication_MembersInjector implements MembersInjector<DigitalSignageApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public DigitalSignageApplication_MembersInjector(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<DigitalSignageApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new DigitalSignageApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(DigitalSignageApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.digitalsignage.player.DigitalSignageApplication.workerFactory")
  public static void injectWorkerFactory(DigitalSignageApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
