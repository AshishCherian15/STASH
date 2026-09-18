package com.ashish.stash.domain.usecase;

import com.ashish.stash.core.database.repository.DocumentRepository;
import com.ashish.stash.core.saf.SafUriManager;
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
public final class ImportDocumentUseCase_Factory implements Factory<ImportDocumentUseCase> {
  private final Provider<DocumentRepository> repositoryProvider;

  private final Provider<SafUriManager> safUriManagerProvider;

  public ImportDocumentUseCase_Factory(Provider<DocumentRepository> repositoryProvider,
      Provider<SafUriManager> safUriManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.safUriManagerProvider = safUriManagerProvider;
  }

  @Override
  public ImportDocumentUseCase get() {
    return newInstance(repositoryProvider.get(), safUriManagerProvider.get());
  }

  public static ImportDocumentUseCase_Factory create(
      Provider<DocumentRepository> repositoryProvider,
      Provider<SafUriManager> safUriManagerProvider) {
    return new ImportDocumentUseCase_Factory(repositoryProvider, safUriManagerProvider);
  }

  public static ImportDocumentUseCase newInstance(DocumentRepository repository,
      SafUriManager safUriManager) {
    return new ImportDocumentUseCase(repository, safUriManager);
  }
}
