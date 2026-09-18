package com.ashish.stash.di;

import com.ashish.stash.core.database.StashDatabase;
import com.ashish.stash.core.database.dao.LabelDao;
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
public final class DatabaseModule_ProvideLabelDaoFactory implements Factory<LabelDao> {
  private final Provider<StashDatabase> databaseProvider;

  public DatabaseModule_ProvideLabelDaoFactory(Provider<StashDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public LabelDao get() {
    return provideLabelDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideLabelDaoFactory create(
      Provider<StashDatabase> databaseProvider) {
    return new DatabaseModule_ProvideLabelDaoFactory(databaseProvider);
  }

  public static LabelDao provideLabelDao(StashDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideLabelDao(database));
  }
}
