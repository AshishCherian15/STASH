package com.ashish.stash.ui.feature.home;

import com.ashish.stash.core.database.repository.DocumentRepository;
import com.ashish.stash.core.saf.SafUriManager;
import com.ashish.stash.core.security.SecuritySessionManager;
import com.ashish.stash.domain.usecase.ImportDocumentUseCase;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<DocumentRepository> repositoryProvider;

  private final Provider<ImportDocumentUseCase> importDocumentUseCaseProvider;

  private final Provider<SafUriManager> safUriManagerProvider;

  private final Provider<SecuritySessionManager> securitySessionManagerProvider;

  public HomeViewModel_Factory(Provider<DocumentRepository> repositoryProvider,
      Provider<ImportDocumentUseCase> importDocumentUseCaseProvider,
      Provider<SafUriManager> safUriManagerProvider,
      Provider<SecuritySessionManager> securitySessionManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.importDocumentUseCaseProvider = importDocumentUseCaseProvider;
    this.safUriManagerProvider = safUriManagerProvider;
    this.securitySessionManagerProvider = securitySessionManagerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(repositoryProvider.get(), importDocumentUseCaseProvider.get(), safUriManagerProvider.get(), securitySessionManagerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<DocumentRepository> repositoryProvider,
      Provider<ImportDocumentUseCase> importDocumentUseCaseProvider,
      Provider<SafUriManager> safUriManagerProvider,
      Provider<SecuritySessionManager> securitySessionManagerProvider) {
    return new HomeViewModel_Factory(repositoryProvider, importDocumentUseCaseProvider, safUriManagerProvider, securitySessionManagerProvider);
  }

  public static HomeViewModel newInstance(DocumentRepository repository,
      ImportDocumentUseCase importDocumentUseCase, SafUriManager safUriManager,
      SecuritySessionManager securitySessionManager) {
    return new HomeViewModel(repository, importDocumentUseCase, safUriManager, securitySessionManager);
  }
}
