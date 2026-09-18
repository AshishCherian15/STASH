package com.ashish.stash.di;

import com.ashish.stash.core.database.StashDatabase;
import com.ashish.stash.core.database.dao.DocumentSearchDao;
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
public final class DatabaseModule_ProvideDocumentSearchDaoFactory implements Factory<DocumentSearchDao> {
  private final Provider<StashDatabase> databaseProvider;

  public DatabaseModule_ProvideDocumentSearchDaoFactory(Provider<StashDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DocumentSearchDao get() {
    return provideDocumentSearchDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDocumentSearchDaoFactory create(
      Provider<StashDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDocumentSearchDaoFactory(databaseProvider);
  }

  public static DocumentSearchDao provideDocumentSearchDao(StashDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDocumentSearchDao(database));
  }
}
