package com.ashish.stash.domain.usecase.folder;

import com.ashish.stash.core.database.repository.DocumentRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SetFolderLockedUseCase_Factory implements Factory<SetFolderLockedUseCase> {
  private final Provider<DocumentRepository> repositoryProvider;

  public SetFolderLockedUseCase_Factory(Provider<DocumentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SetFolderLockedUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SetFolderLockedUseCase_Factory create(
      Provider<DocumentRepository> repositoryProvider) {
    return new SetFolderLockedUseCase_Factory(repositoryProvider);
  }

  public static SetFolderLockedUseCase newInstance(DocumentRepository repository) {
    return new SetFolderLockedUseCase(repository);
  }
}
