package com.digitalsignage.player.core.kiosk;

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
public final class SignageDeviceAdminReceiver_MembersInjector implements MembersInjector<SignageDeviceAdminReceiver> {
  private final Provider<Logger> loggerProvider;

  public SignageDeviceAdminReceiver_MembersInjector(Provider<Logger> loggerProvider) {
    this.loggerProvider = loggerProvider;
  }

  public static MembersInjector<SignageDeviceAdminReceiver> create(
      Provider<Logger> loggerProvider) {
    return new SignageDeviceAdminReceiver_MembersInjector(loggerProvider);
  }

  @Override
  public void injectMembers(SignageDeviceAdminReceiver instance) {
    injectLogger(instance, loggerProvider.get());
  }

  @InjectedFieldSignature("com.digitalsignage.player.core.kiosk.SignageDeviceAdminReceiver.logger")
  public static void injectLogger(SignageDeviceAdminReceiver instance, Logger logger) {
    instance.logger = logger;
  }
}
