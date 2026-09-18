package com.ashish.stash.di;

import com.ashish.stash.core.database.StashDatabase;
import com.ashish.stash.core.database.dao.ResourceLinkDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "cast",
    "deprecation"
})
public final class DatabaseModule_ProvideResourceLinkDaoFactory implements Factory<ResourceLinkDao> {
  private final Provider<StashDatabase> databaseProvider;

  public DatabaseModule_ProvideResourceLinkDaoFactory(Provider<StashDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ResourceLinkDao get() {
    return provideResourceLinkDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideResourceLinkDaoFactory create(
      Provider<StashDatabase> databaseProvider) {
    return new DatabaseModule_ProvideResourceLinkDaoFactory(databaseProvider);
  }

  public static ResourceLinkDao provideResourceLinkDao(StashDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideResourceLinkDao(database));
  }
}
