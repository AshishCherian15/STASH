package com.ashish.stash.core.thumbnail;

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
public final class ThumbnailService_Factory implements Factory<ThumbnailService> {
  private final Provider<Context> contextProvider;

  public ThumbnailService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ThumbnailService get() {
    return newInstance(contextProvider.get());
  }

  public static ThumbnailService_Factory create(Provider<Context> contextProvider) {
    return new ThumbnailService_Factory(contextProvider);
  }

  public static ThumbnailService newInstance(Context context) {
    return new ThumbnailService(context);
  }
}
