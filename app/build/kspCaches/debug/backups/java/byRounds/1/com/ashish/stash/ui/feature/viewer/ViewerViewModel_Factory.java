package com.ashish.stash.ui.feature.viewer;

import androidx.lifecycle.SavedStateHandle;
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
public final class ViewerViewModel_Factory implements Factory<ViewerViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<DocumentRepository> documentRepositoryProvider;

  public ViewerViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<DocumentRepository> documentRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.documentRepositoryProvider = documentRepositoryProvider;
  }

  @Override
  public ViewerViewModel get() {
    return newInstance(savedStateHandleProvider.get(), documentRepositoryProvider.get());
  }

  public static ViewerViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<DocumentRepository> documentRepositoryProvider) {
    return new ViewerViewModel_Factory(savedStateHandleProvider, documentRepositoryProvider);
  }

  public static ViewerViewModel newInstance(SavedStateHandle savedStateHandle,
      DocumentRepository documentRepository) {
    return new ViewerViewModel(savedStateHandle, documentRepository);
  }
}
