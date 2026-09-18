package com.ashish.stash.core.database.repository;

import android.content.Context;
import com.ashish.stash.core.database.dao.CategoryDao;
import com.ashish.stash.core.database.dao.DocumentDao;
import com.ashish.stash.core.database.dao.DocumentSearchDao;
import com.ashish.stash.core.database.dao.FolderDao;
import com.ashish.stash.core.database.dao.LabelDao;
import com.ashish.stash.core.database.dao.ResourceLinkDao;
import com.ashish.stash.core.hash.HashService;
import com.ashish.stash.core.saf.SafUriManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DocumentRepositoryImpl_Factory implements Factory<DocumentRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<DocumentDao> documentDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<FolderDao> folderDaoProvider;

  private final Provider<LabelDao> labelDaoProvider;

  private final Provider<DocumentSearchDao> documentSearchDaoProvider;

  private final Provider<ResourceLinkDao> resourceLinkDaoProvider;

  private final Provider<SafUriManager> safUriManagerProvider;

  private final Provider<HashService> hashServiceProvider;

  public DocumentRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<DocumentDao> documentDaoProvider, Provider<CategoryDao> categoryDaoProvider,
      Provider<FolderDao> folderDaoProvider, Provider<LabelDao> labelDaoProvider,
      Provider<DocumentSearchDao> documentSearchDaoProvider,
      Provider<ResourceLinkDao> resourceLinkDaoProvider,
      Provider<SafUriManager> safUriManagerProvider, Provider<HashService> hashServiceProvider) {
    this.contextProvider = contextProvider;
    this.documentDaoProvider = documentDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.folderDaoProvider = folderDaoProvider;
    this.labelDaoProvider = labelDaoProvider;
    this.documentSearchDaoProvider = documentSearchDaoProvider;
    this.resourceLinkDaoProvider = resourceLinkDaoProvider;
    this.safUriManagerProvider = safUriManagerProvider;
    this.hashServiceProvider = hashServiceProvider;
  }

  @Override
  public DocumentRepositoryImpl get() {
    return newInstance(contextProvider.get(), documentDaoProvider.get(), categoryDaoProvider.get(), folderDaoProvider.get(), labelDaoProvider.get(), documentSearchDaoProvider.get(), resourceLinkDaoProvider.get(), safUriManagerProvider.get(), hashServiceProvider.get());
  }

  public static DocumentRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<DocumentDao> documentDaoProvider, Provider<CategoryDao> categoryDaoProvider,
      Provider<FolderDao> folderDaoProvider, Provider<LabelDao> labelDaoProvider,
      Provider<DocumentSearchDao> documentSearchDaoProvider,
      Provider<ResourceLinkDao> resourceLinkDaoProvider,
      Provider<SafUriManager> safUriManagerProvider, Provider<HashService> hashServiceProvider) {
    return new DocumentRepositoryImpl_Factory(contextProvider, documentDaoProvider, categoryDaoProvider, folderDaoProvider, labelDaoProvider, documentSearchDaoProvider, resourceLinkDaoProvider, safUriManagerProvider, hashServiceProvider);
  }

  public static DocumentRepositoryImpl newInstance(Context context, DocumentDao documentDao,
      CategoryDao categoryDao, FolderDao folderDao, LabelDao labelDao,
      DocumentSearchDao documentSearchDao, ResourceLinkDao resourceLinkDao,
      SafUriManager safUriManager, HashService hashService) {
    return new DocumentRepositoryImpl(context, documentDao, categoryDao, folderDao, labelDao, documentSearchDao, resourceLinkDao, safUriManager, hashService);
  }
}
