package com.ashish.stash.di;

import android.content.Context;
import com.ashish.stash.core.database.StashDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideStashDatabaseFactory implements Factory<StashDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideStashDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public StashDatabase get() {
    return provideStashDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideStashDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideStashDatabaseFactory(contextProvider);
  }

  public static StashDatabase provideStashDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideStashDatabase(context));
  }
}
