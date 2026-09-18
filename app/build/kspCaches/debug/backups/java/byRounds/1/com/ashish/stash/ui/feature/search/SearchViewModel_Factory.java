package com.ashish.stash.ui.feature.search;

import com.ashish.stash.core.database.repository.DocumentRepository;
import com.ashish.stash.core.security.SecuritySessionManager;
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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<DocumentRepository> documentRepositoryProvider;

  private final Provider<SecuritySessionManager> securitySessionManagerProvider;

  public SearchViewModel_Factory(Provider<DocumentRepository> documentRepositoryProvider,
      Provider<SecuritySessionManager> securitySessionManagerProvider) {
    this.documentRepositoryProvider = documentRepositoryProvider;
    this.securitySessionManagerProvider = securitySessionManagerProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(documentRepositoryProvider.get(), securitySessionManagerProvider.get());
  }

  public static SearchViewModel_Factory create(
      Provider<DocumentRepository> documentRepositoryProvider,
      Provider<SecuritySessionManager> securitySessionManagerProvider) {
    return new SearchViewModel_Factory(documentRepositoryProvider, securitySessionManagerProvider);
  }

  public static SearchViewModel newInstance(DocumentRepository documentRepository,
      SecuritySessionManager securitySessionManager) {
    return new SearchViewModel(documentRepository, securitySessionManager);
  }
}
