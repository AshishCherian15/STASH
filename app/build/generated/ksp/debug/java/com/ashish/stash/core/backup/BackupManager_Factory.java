package com.ashish.stash.core.backup;

import android.content.Context;
import com.ashish.stash.core.database.repository.DocumentRepository;
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
public final class BackupManager_Factory implements Factory<BackupManager> {
  private final Provider<Context> contextProvider;

  private final Provider<DocumentRepository> repositoryProvider;

  public BackupManager_Factory(Provider<Context> contextProvider,
      Provider<DocumentRepository> repositoryProvider) {
    this.contextProvider = contextProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public BackupManager get() {
    return newInstance(contextProvider.get(), repositoryProvider.get());
  }

  public static BackupManager_Factory create(Provider<Context> contextProvider,
      Provider<DocumentRepository> repositoryProvider) {
    return new BackupManager_Factory(contextProvider, repositoryProvider);
  }

  public static BackupManager newInstance(Context context, DocumentRepository repository) {
    return new BackupManager(context, repository);
  }
}
