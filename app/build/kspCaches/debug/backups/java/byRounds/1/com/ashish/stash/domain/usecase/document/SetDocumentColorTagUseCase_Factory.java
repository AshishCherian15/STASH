package com.ashish.stash.domain.usecase.document;

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
public final class SetDocumentColorTagUseCase_Factory implements Factory<SetDocumentColorTagUseCase> {
  private final Provider<DocumentRepository> repositoryProvider;

  public SetDocumentColorTagUseCase_Factory(Provider<DocumentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SetDocumentColorTagUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SetDocumentColorTagUseCase_Factory create(
      Provider<DocumentRepository> repositoryProvider) {
    return new SetDocumentColorTagUseCase_Factory(repositoryProvider);
  }

  public static SetDocumentColorTagUseCase newInstance(DocumentRepository repository) {
    return new SetDocumentColorTagUseCase(repository);
  }
}
