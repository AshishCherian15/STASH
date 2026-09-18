package com.ashish.stash.ui.feature.splash;

import com.ashish.stash.core.database.repository.DocumentRepository;
import com.ashish.stash.core.preferences.PreferencesManager;
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
public final class SplashViewModel_Factory implements Factory<SplashViewModel> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<DocumentRepository> repositoryProvider;

  public SplashViewModel_Factory(Provider<PreferencesManager> preferencesManagerProvider,
      Provider<DocumentRepository> repositoryProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(preferencesManagerProvider.get(), repositoryProvider.get());
  }

  public static SplashViewModel_Factory create(
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<DocumentRepository> repositoryProvider) {
    return new SplashViewModel_Factory(preferencesManagerProvider, repositoryProvider);
  }

  public static SplashViewModel newInstance(PreferencesManager preferencesManager,
      DocumentRepository repository) {
    return new SplashViewModel(preferencesManager, repository);
  }
}
