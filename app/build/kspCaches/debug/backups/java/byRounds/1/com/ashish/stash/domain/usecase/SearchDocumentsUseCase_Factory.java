package com.ashish.stash.domain.usecase;

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
public final class SearchDocumentsUseCase_Factory implements Factory<SearchDocumentsUseCase> {
  private final Provider<DocumentRepository> repositoryProvider;

  public SearchDocumentsUseCase_Factory(Provider<DocumentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SearchDocumentsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SearchDocumentsUseCase_Factory create(
      Provider<DocumentRepository> repositoryProvider) {
    return new SearchDocumentsUseCase_Factory(repositoryProvider);
  }

  public static SearchDocumentsUseCase newInstance(DocumentRepository repository) {
    return new SearchDocumentsUseCase(repository);
  }
}
