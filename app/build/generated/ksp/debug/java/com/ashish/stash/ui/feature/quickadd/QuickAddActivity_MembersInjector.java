package com.ashish.stash.ui.feature.quickadd;

import com.ashish.stash.core.database.repository.DocumentRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class QuickAddActivity_MembersInjector implements MembersInjector<QuickAddActivity> {
  private final Provider<DocumentRepository> documentRepositoryProvider;

  public QuickAddActivity_MembersInjector(Provider<DocumentRepository> documentRepositoryProvider) {
    this.documentRepositoryProvider = documentRepositoryProvider;
  }

  public static MembersInjector<QuickAddActivity> create(
      Provider<DocumentRepository> documentRepositoryProvider) {
    return new QuickAddActivity_MembersInjector(documentRepositoryProvider);
  }

  @Override
  public void injectMembers(QuickAddActivity instance) {
    injectDocumentRepository(instance, documentRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.ashish.stash.ui.feature.quickadd.QuickAddActivity.documentRepository")
  public static void injectDocumentRepository(QuickAddActivity instance,
      DocumentRepository documentRepository) {
    instance.documentRepository = documentRepository;
  }
}
