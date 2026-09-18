package com.ashish.stash.core.security;

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
    "cast",
    "deprecation"
})
public final class SecuritySessionManager_Factory implements Factory<SecuritySessionManager> {
  @Override
  public SecuritySessionManager get() {
    return newInstance();
  }

  public static SecuritySessionManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SecuritySessionManager newInstance() {
    return new SecuritySessionManager();
  }

  private static final class InstanceHolder {
    private static final SecuritySessionManager_Factory INSTANCE = new SecuritySessionManager_Factory();
  }
}
