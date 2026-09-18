package com.ashish.stash.ui.theme;

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
public final class ThemeManager_Factory implements Factory<ThemeManager> {
  @Override
  public ThemeManager get() {
    return newInstance();
  }

  public static ThemeManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ThemeManager newInstance() {
    return new ThemeManager();
  }

  private static final class InstanceHolder {
    private static final ThemeManager_Factory INSTANCE = new ThemeManager_Factory();
  }
}
