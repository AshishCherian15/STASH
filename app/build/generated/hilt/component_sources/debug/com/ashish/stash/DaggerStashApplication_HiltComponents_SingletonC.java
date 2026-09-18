package com.ashish.stash;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.ashish.stash.core.backup.BackupManager;
import com.ashish.stash.core.database.StashDatabase;
import com.ashish.stash.core.database.dao.CategoryDao;
import com.ashish.stash.core.database.dao.DocumentDao;
import com.ashish.stash.core.database.dao.DocumentSearchDao;
import com.ashish.stash.core.database.dao.FolderDao;
import com.ashish.stash.core.database.dao.LabelDao;
import com.ashish.stash.core.database.dao.ResourceLinkDao;
import com.ashish.stash.core.database.repository.DocumentRepository;
import com.ashish.stash.core.database.repository.DocumentRepositoryImpl;
import com.ashish.stash.core.hash.HashService;
import com.ashish.stash.core.notification.NotificationHelper;
import com.ashish.stash.core.ocr.OcrService;
import com.ashish.stash.core.preferences.PreferencesManager;
import com.ashish.stash.core.saf.SafUriManager;
import com.ashish.stash.core.security.SecuritySessionManager;
import com.ashish.stash.core.work.OcrProcessingWorker;
import com.ashish.stash.core.work.OcrProcessingWorker_AssistedFactory;
import com.ashish.stash.di.DatabaseModule_ProvideCategoryDaoFactory;
import com.ashish.stash.di.DatabaseModule_ProvideDocumentDaoFactory;
import com.ashish.stash.di.DatabaseModule_ProvideDocumentSearchDaoFactory;
import com.ashish.stash.di.DatabaseModule_ProvideFolderDaoFactory;
import com.ashish.stash.di.DatabaseModule_ProvideLabelDaoFactory;
import com.ashish.stash.di.DatabaseModule_ProvideResourceLinkDaoFactory;
import com.ashish.stash.di.DatabaseModule_ProvideStashDatabaseFactory;
import com.ashish.stash.domain.usecase.ImportDocumentUseCase;
import com.ashish.stash.ui.feature.document.DocumentDetailViewModel;
import com.ashish.stash.ui.feature.document.DocumentDetailViewModel_HiltModules;
import com.ashish.stash.ui.feature.home.HomeViewModel;
import com.ashish.stash.ui.feature.home.HomeViewModel_HiltModules;
import com.ashish.stash.ui.feature.onboarding.OnboardingViewModel;
import com.ashish.stash.ui.feature.onboarding.OnboardingViewModel_HiltModules;
import com.ashish.stash.ui.feature.priority.PriorityModeViewModel;
import com.ashish.stash.ui.feature.priority.PriorityModeViewModel_HiltModules;
import com.ashish.stash.ui.feature.quickadd.QuickAddActivity;
import com.ashish.stash.ui.feature.quickadd.QuickAddActivity_MembersInjector;
import com.ashish.stash.ui.feature.search.SearchViewModel;
import com.ashish.stash.ui.feature.search.SearchViewModel_HiltModules;
import com.ashish.stash.ui.feature.settings.SettingsViewModel;
import com.ashish.stash.ui.feature.settings.SettingsViewModel_HiltModules;
import com.ashish.stash.ui.feature.splash.SplashViewModel;
import com.ashish.stash.ui.feature.splash.SplashViewModel_HiltModules;
import com.ashish.stash.ui.feature.viewer.ViewerViewModel;
import com.ashish.stash.ui.feature.viewer.ViewerViewModel_HiltModules;
import com.ashish.stash.ui.theme.ThemeManager;
import com.ashish.stash.worker.scanner.BackgroundScannerWorker;
import com.ashish.stash.worker.scanner.BackgroundScannerWorker_AssistedFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerStashApplication_HiltComponents_SingletonC {
  private DaggerStashApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public StashApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements StashApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public StashApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements StashApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public StashApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements StashApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public StashApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements StashApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public StashApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements StashApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public StashApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements StashApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public StashApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements StashApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public StashApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends StashApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends StashApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends StashApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends StashApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public void injectQuickAddActivity(QuickAddActivity quickAddActivity) {
      injectQuickAddActivity2(quickAddActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(8).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_document_DocumentDetailViewModel, DocumentDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_onboarding_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_priority_PriorityModeViewModel, PriorityModeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_search_SearchViewModel, SearchViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_splash_SplashViewModel, SplashViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_viewer_ViewerViewModel, ViewerViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private QuickAddActivity injectQuickAddActivity2(QuickAddActivity instance) {
      QuickAddActivity_MembersInjector.injectDocumentRepository(instance, singletonCImpl.documentRepositoryImplProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_ashish_stash_ui_feature_document_DocumentDetailViewModel = "com.ashish.stash.ui.feature.document.DocumentDetailViewModel";

      static String com_ashish_stash_ui_feature_priority_PriorityModeViewModel = "com.ashish.stash.ui.feature.priority.PriorityModeViewModel";

      static String com_ashish_stash_ui_feature_settings_SettingsViewModel = "com.ashish.stash.ui.feature.settings.SettingsViewModel";

      static String com_ashish_stash_ui_feature_onboarding_OnboardingViewModel = "com.ashish.stash.ui.feature.onboarding.OnboardingViewModel";

      static String com_ashish_stash_ui_feature_search_SearchViewModel = "com.ashish.stash.ui.feature.search.SearchViewModel";

      static String com_ashish_stash_ui_feature_splash_SplashViewModel = "com.ashish.stash.ui.feature.splash.SplashViewModel";

      static String com_ashish_stash_ui_feature_viewer_ViewerViewModel = "com.ashish.stash.ui.feature.viewer.ViewerViewModel";

      static String com_ashish_stash_ui_feature_home_HomeViewModel = "com.ashish.stash.ui.feature.home.HomeViewModel";

      @KeepFieldType
      DocumentDetailViewModel com_ashish_stash_ui_feature_document_DocumentDetailViewModel2;

      @KeepFieldType
      PriorityModeViewModel com_ashish_stash_ui_feature_priority_PriorityModeViewModel2;

      @KeepFieldType
      SettingsViewModel com_ashish_stash_ui_feature_settings_SettingsViewModel2;

      @KeepFieldType
      OnboardingViewModel com_ashish_stash_ui_feature_onboarding_OnboardingViewModel2;

      @KeepFieldType
      SearchViewModel com_ashish_stash_ui_feature_search_SearchViewModel2;

      @KeepFieldType
      SplashViewModel com_ashish_stash_ui_feature_splash_SplashViewModel2;

      @KeepFieldType
      ViewerViewModel com_ashish_stash_ui_feature_viewer_ViewerViewModel2;

      @KeepFieldType
      HomeViewModel com_ashish_stash_ui_feature_home_HomeViewModel2;
    }
  }

  private static final class ViewModelCImpl extends StashApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<DocumentDetailViewModel> documentDetailViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<PriorityModeViewModel> priorityModeViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<SplashViewModel> splashViewModelProvider;

    private Provider<ViewerViewModel> viewerViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private ImportDocumentUseCase importDocumentUseCase() {
      return new ImportDocumentUseCase(singletonCImpl.documentRepositoryImplProvider.get(), singletonCImpl.safUriManagerProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.documentDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.priorityModeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.splashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.viewerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(8).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_document_DocumentDetailViewModel, ((Provider) documentDetailViewModelProvider)).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_onboarding_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_priority_PriorityModeViewModel, ((Provider) priorityModeViewModelProvider)).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_search_SearchViewModel, ((Provider) searchViewModelProvider)).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_splash_SplashViewModel, ((Provider) splashViewModelProvider)).put(LazyClassKeyProvider.com_ashish_stash_ui_feature_viewer_ViewerViewModel, ((Provider) viewerViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_ashish_stash_ui_feature_search_SearchViewModel = "com.ashish.stash.ui.feature.search.SearchViewModel";

      static String com_ashish_stash_ui_feature_document_DocumentDetailViewModel = "com.ashish.stash.ui.feature.document.DocumentDetailViewModel";

      static String com_ashish_stash_ui_feature_home_HomeViewModel = "com.ashish.stash.ui.feature.home.HomeViewModel";

      static String com_ashish_stash_ui_feature_settings_SettingsViewModel = "com.ashish.stash.ui.feature.settings.SettingsViewModel";

      static String com_ashish_stash_ui_feature_priority_PriorityModeViewModel = "com.ashish.stash.ui.feature.priority.PriorityModeViewModel";

      static String com_ashish_stash_ui_feature_viewer_ViewerViewModel = "com.ashish.stash.ui.feature.viewer.ViewerViewModel";

      static String com_ashish_stash_ui_feature_onboarding_OnboardingViewModel = "com.ashish.stash.ui.feature.onboarding.OnboardingViewModel";

      static String com_ashish_stash_ui_feature_splash_SplashViewModel = "com.ashish.stash.ui.feature.splash.SplashViewModel";

      @KeepFieldType
      SearchViewModel com_ashish_stash_ui_feature_search_SearchViewModel2;

      @KeepFieldType
      DocumentDetailViewModel com_ashish_stash_ui_feature_document_DocumentDetailViewModel2;

      @KeepFieldType
      HomeViewModel com_ashish_stash_ui_feature_home_HomeViewModel2;

      @KeepFieldType
      SettingsViewModel com_ashish_stash_ui_feature_settings_SettingsViewModel2;

      @KeepFieldType
      PriorityModeViewModel com_ashish_stash_ui_feature_priority_PriorityModeViewModel2;

      @KeepFieldType
      ViewerViewModel com_ashish_stash_ui_feature_viewer_ViewerViewModel2;

      @KeepFieldType
      OnboardingViewModel com_ashish_stash_ui_feature_onboarding_OnboardingViewModel2;

      @KeepFieldType
      SplashViewModel com_ashish_stash_ui_feature_splash_SplashViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.ashish.stash.ui.feature.document.DocumentDetailViewModel 
          return (T) new DocumentDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.documentRepositoryImplProvider.get());

          case 1: // com.ashish.stash.ui.feature.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.documentRepositoryImplProvider.get(), viewModelCImpl.importDocumentUseCase(), singletonCImpl.safUriManagerProvider.get(), singletonCImpl.securitySessionManagerProvider.get());

          case 2: // com.ashish.stash.ui.feature.onboarding.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.preferencesManagerProvider.get());

          case 3: // com.ashish.stash.ui.feature.priority.PriorityModeViewModel 
          return (T) new PriorityModeViewModel(singletonCImpl.documentRepositoryImplProvider.get(), singletonCImpl.securitySessionManagerProvider.get());

          case 4: // com.ashish.stash.ui.feature.search.SearchViewModel 
          return (T) new SearchViewModel(singletonCImpl.documentRepositoryImplProvider.get(), singletonCImpl.securitySessionManagerProvider.get());

          case 5: // com.ashish.stash.ui.feature.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.documentRepositoryImplProvider.get(), singletonCImpl.preferencesManagerProvider.get(), singletonCImpl.themeManagerProvider.get(), singletonCImpl.backupManagerProvider.get());

          case 6: // com.ashish.stash.ui.feature.splash.SplashViewModel 
          return (T) new SplashViewModel(singletonCImpl.preferencesManagerProvider.get(), singletonCImpl.documentRepositoryImplProvider.get());

          case 7: // com.ashish.stash.ui.feature.viewer.ViewerViewModel 
          return (T) new ViewerViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.documentRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends StashApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends StashApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends StashApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<StashDatabase> provideStashDatabaseProvider;

    private Provider<SafUriManager> safUriManagerProvider;

    private Provider<HashService> hashServiceProvider;

    private Provider<DocumentRepositoryImpl> documentRepositoryImplProvider;

    private Provider<NotificationHelper> notificationHelperProvider;

    private Provider<BackgroundScannerWorker_AssistedFactory> backgroundScannerWorker_AssistedFactoryProvider;

    private Provider<OcrService> ocrServiceProvider;

    private Provider<OcrProcessingWorker_AssistedFactory> ocrProcessingWorker_AssistedFactoryProvider;

    private Provider<SecuritySessionManager> securitySessionManagerProvider;

    private Provider<PreferencesManager> preferencesManagerProvider;

    private Provider<ThemeManager> themeManagerProvider;

    private Provider<BackupManager> backupManagerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private DocumentDao documentDao() {
      return DatabaseModule_ProvideDocumentDaoFactory.provideDocumentDao(provideStashDatabaseProvider.get());
    }

    private CategoryDao categoryDao() {
      return DatabaseModule_ProvideCategoryDaoFactory.provideCategoryDao(provideStashDatabaseProvider.get());
    }

    private FolderDao folderDao() {
      return DatabaseModule_ProvideFolderDaoFactory.provideFolderDao(provideStashDatabaseProvider.get());
    }

    private LabelDao labelDao() {
      return DatabaseModule_ProvideLabelDaoFactory.provideLabelDao(provideStashDatabaseProvider.get());
    }

    private DocumentSearchDao documentSearchDao() {
      return DatabaseModule_ProvideDocumentSearchDaoFactory.provideDocumentSearchDao(provideStashDatabaseProvider.get());
    }

    private ResourceLinkDao resourceLinkDao() {
      return DatabaseModule_ProvideResourceLinkDaoFactory.provideResourceLinkDao(provideStashDatabaseProvider.get());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return MapBuilder.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>newMapBuilder(2).put("com.ashish.stash.worker.scanner.BackgroundScannerWorker", ((Provider) backgroundScannerWorker_AssistedFactoryProvider)).put("com.ashish.stash.core.work.OcrProcessingWorker", ((Provider) ocrProcessingWorker_AssistedFactoryProvider)).build();
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideStashDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<StashDatabase>(singletonCImpl, 2));
      this.safUriManagerProvider = DoubleCheck.provider(new SwitchingProvider<SafUriManager>(singletonCImpl, 3));
      this.hashServiceProvider = DoubleCheck.provider(new SwitchingProvider<HashService>(singletonCImpl, 4));
      this.documentRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<DocumentRepositoryImpl>(singletonCImpl, 1));
      this.notificationHelperProvider = DoubleCheck.provider(new SwitchingProvider<NotificationHelper>(singletonCImpl, 5));
      this.backgroundScannerWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<BackgroundScannerWorker_AssistedFactory>(singletonCImpl, 0));
      this.ocrServiceProvider = DoubleCheck.provider(new SwitchingProvider<OcrService>(singletonCImpl, 7));
      this.ocrProcessingWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<OcrProcessingWorker_AssistedFactory>(singletonCImpl, 6));
      this.securitySessionManagerProvider = DoubleCheck.provider(new SwitchingProvider<SecuritySessionManager>(singletonCImpl, 8));
      this.preferencesManagerProvider = DoubleCheck.provider(new SwitchingProvider<PreferencesManager>(singletonCImpl, 9));
      this.themeManagerProvider = DoubleCheck.provider(new SwitchingProvider<ThemeManager>(singletonCImpl, 10));
      this.backupManagerProvider = DoubleCheck.provider(new SwitchingProvider<BackupManager>(singletonCImpl, 11));
    }

    @Override
    public void injectStashApplication(StashApplication stashApplication) {
      injectStashApplication2(stashApplication);
    }

    @Override
    public DocumentRepository documentRepository() {
      return documentRepositoryImplProvider.get();
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private StashApplication injectStashApplication2(StashApplication instance) {
      StashApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.ashish.stash.worker.scanner.BackgroundScannerWorker_AssistedFactory 
          return (T) new BackgroundScannerWorker_AssistedFactory() {
            @Override
            public BackgroundScannerWorker create(Context context, WorkerParameters workerParams) {
              return new BackgroundScannerWorker(context, workerParams, singletonCImpl.documentRepositoryImplProvider.get(), singletonCImpl.notificationHelperProvider.get());
            }
          };

          case 1: // com.ashish.stash.core.database.repository.DocumentRepositoryImpl 
          return (T) new DocumentRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.documentDao(), singletonCImpl.categoryDao(), singletonCImpl.folderDao(), singletonCImpl.labelDao(), singletonCImpl.documentSearchDao(), singletonCImpl.resourceLinkDao(), singletonCImpl.safUriManagerProvider.get(), singletonCImpl.hashServiceProvider.get());

          case 2: // com.ashish.stash.core.database.StashDatabase 
          return (T) DatabaseModule_ProvideStashDatabaseFactory.provideStashDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.ashish.stash.core.saf.SafUriManager 
          return (T) new SafUriManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.ashish.stash.core.hash.HashService 
          return (T) new HashService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.ashish.stash.core.notification.NotificationHelper 
          return (T) new NotificationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.ashish.stash.core.work.OcrProcessingWorker_AssistedFactory 
          return (T) new OcrProcessingWorker_AssistedFactory() {
            @Override
            public OcrProcessingWorker create(Context context2, WorkerParameters workerParams2) {
              return new OcrProcessingWorker(context2, workerParams2, singletonCImpl.documentRepositoryImplProvider.get(), singletonCImpl.ocrServiceProvider.get());
            }
          };

          case 7: // com.ashish.stash.core.ocr.OcrService 
          return (T) new OcrService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 8: // com.ashish.stash.core.security.SecuritySessionManager 
          return (T) new SecuritySessionManager();

          case 9: // com.ashish.stash.core.preferences.PreferencesManager 
          return (T) new PreferencesManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 10: // com.ashish.stash.ui.theme.ThemeManager 
          return (T) new ThemeManager();

          case 11: // com.ashish.stash.core.backup.BackupManager 
          return (T) new BackupManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.documentRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
