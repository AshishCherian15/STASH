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
public final class CreateLabelUseCase_Factory implements Factory<CreateLabelUseCase> {
  private final Provider<DocumentRepository> repositoryProvider;

  public CreateLabelUseCase_Factory(Provider<DocumentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CreateLabelUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static CreateLabelUseCase_Factory create(Provider<DocumentRepository> repositoryProvider) {
    return new CreateLabelUseCase_Factory(repositoryProvider);
  }

  public static CreateLabelUseCase newInstance(DocumentRepository repository) {
    return new CreateLabelUseCase(repository);
  }
}
