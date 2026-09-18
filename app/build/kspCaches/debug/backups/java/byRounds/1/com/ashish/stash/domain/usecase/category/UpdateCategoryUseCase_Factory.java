package com.ashish.stash.domain.usecase.category;

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
public final class UpdateCategoryUseCase_Factory implements Factory<UpdateCategoryUseCase> {
  private final Provider<DocumentRepository> repositoryProvider;

  public UpdateCategoryUseCase_Factory(Provider<DocumentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateCategoryUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateCategoryUseCase_Factory create(
      Provider<DocumentRepository> repositoryProvider) {
    return new UpdateCategoryUseCase_Factory(repositoryProvider);
  }

  public static UpdateCategoryUseCase newInstance(DocumentRepository repository) {
    return new UpdateCategoryUseCase(repository);
  }
}
