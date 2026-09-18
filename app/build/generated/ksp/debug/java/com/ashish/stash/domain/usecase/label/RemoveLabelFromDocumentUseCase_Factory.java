package com.ashish.stash.domain.usecase.label;

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
public final class RemoveLabelFromDocumentUseCase_Factory implements Factory<RemoveLabelFromDocumentUseCase> {
  private final Provider<DocumentRepository> repositoryProvider;

  public RemoveLabelFromDocumentUseCase_Factory(Provider<DocumentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RemoveLabelFromDocumentUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static RemoveLabelFromDocumentUseCase_Factory create(
      Provider<DocumentRepository> repositoryProvider) {
    return new RemoveLabelFromDocumentUseCase_Factory(repositoryProvider);
  }

  public static RemoveLabelFromDocumentUseCase newInstance(DocumentRepository repository) {
    return new RemoveLabelFromDocumentUseCase(repository);
  }
}
