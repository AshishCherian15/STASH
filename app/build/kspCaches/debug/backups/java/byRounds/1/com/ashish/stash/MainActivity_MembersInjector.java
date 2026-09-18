package com.ashish.stash;

import com.ashish.stash.core.preferences.PreferencesManager;
import com.ashish.stash.core.security.BiometricLockManager;
import com.ashish.stash.core.security.SecuritySessionManager;
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
    "cast",
    "deprecation"
})
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<BiometricLockManager> biometricLockManagerProvider;

  private final Provider<SecuritySessionManager> securitySessionManagerProvider;

  public MainActivity_MembersInjector(Provider<PreferencesManager> preferencesManagerProvider,
      Provider<BiometricLockManager> biometricLockManagerProvider,
      Provider<SecuritySessionManager> securitySessionManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.biometricLockManagerProvider = biometricLockManagerProvider;
    this.securitySessionManagerProvider = securitySessionManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<BiometricLockManager> biometricLockManagerProvider,
      Provider<SecuritySessionManager> securitySessionManagerProvider) {
    return new MainActivity_MembersInjector(preferencesManagerProvider, biometricLockManagerProvider, securitySessionManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPreferencesManager(instance, preferencesManagerProvider.get());
    injectBiometricLockManager(instance, biometricLockManagerProvider.get());
    injectSecuritySessionManager(instance, securitySessionManagerProvider.get());
  }

  @InjectedFieldSignature("com.ashish.stash.MainActivity.preferencesManager")
  public static void injectPreferencesManager(MainActivity instance,
      PreferencesManager preferencesManager) {
    instance.preferencesManager = preferencesManager;
  }

  @InjectedFieldSignature("com.ashish.stash.MainActivity.biometricLockManager")
  public static void injectBiometricLockManager(MainActivity instance,
      BiometricLockManager biometricLockManager) {
    instance.biometricLockManager = biometricLockManager;
  }

  @InjectedFieldSignature("com.ashish.stash.MainActivity.securitySessionManager")
  public static void injectSecuritySessionManager(MainActivity instance,
      SecuritySessionManager securitySessionManager) {
    instance.securitySessionManager = securitySessionManager;
  }
}
