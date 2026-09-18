package com.ashish.stash.ui.feature.settings;

import com.ashish.stash.core.backup.BackupManager;
import com.ashish.stash.core.database.repository.DocumentRepository;
import com.ashish.stash.core.preferences.PreferencesManager;
import com.ashish.stash.ui.theme.ThemeManager;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<DocumentRepository> documentRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<ThemeManager> themeManagerProvider;

  private final Provider<BackupManager> backupManagerProvider;

  public SettingsViewModel_Factory(Provider<DocumentRepository> documentRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<ThemeManager> themeManagerProvider, Provider<BackupManager> backupManagerProvider) {
    this.documentRepositoryProvider = documentRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.themeManagerProvider = themeManagerProvider;
    this.backupManagerProvider = backupManagerProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(documentRepositoryProvider.get(), preferencesManagerProvider.get(), themeManagerProvider.get(), backupManagerProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<DocumentRepository> documentRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<ThemeManager> themeManagerProvider, Provider<BackupManager> backupManagerProvider) {
    return new SettingsViewModel_Factory(documentRepositoryProvider, preferencesManagerProvider, themeManagerProvider, backupManagerProvider);
  }

  public static SettingsViewModel newInstance(DocumentRepository documentRepository,
      PreferencesManager preferencesManager, ThemeManager themeManager,
      BackupManager backupManager) {
    return new SettingsViewModel(documentRepository, preferencesManager, themeManager, backupManager);
  }
}
