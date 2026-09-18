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
public final class AddLabelToDocumentUseCase_Factory implements Factory<AddLabelToDocumentUseCase> {
  private final Provider<DocumentRepository> repositoryProvider;

  public AddLabelToDocumentUseCase_Factory(Provider<DocumentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddLabelToDocumentUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddLabelToDocumentUseCase_Factory create(
      Provider<DocumentRepository> repositoryProvider) {
    return new AddLabelToDocumentUseCase_Factory(repositoryProvider);
  }

  public static AddLabelToDocumentUseCase newInstance(DocumentRepository repository) {
    return new AddLabelToDocumentUseCase(repository);
  }
}
