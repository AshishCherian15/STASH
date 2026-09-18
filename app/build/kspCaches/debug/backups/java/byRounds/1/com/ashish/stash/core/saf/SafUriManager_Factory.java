package com.ashish.stash.core.saf;

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
    "cast",
    "deprecation"
})
public final class SafUriManager_Factory implements Factory<SafUriManager> {
  private final Provider<Context> contextProvider;

  public SafUriManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SafUriManager get() {
    return newInstance(contextProvider.get());
  }

  public static SafUriManager_Factory create(Provider<Context> contextProvider) {
    return new SafUriManager_Factory(contextProvider);
  }

  public static SafUriManager newInstance(Context context) {
    return new SafUriManager(context);
  }
}
