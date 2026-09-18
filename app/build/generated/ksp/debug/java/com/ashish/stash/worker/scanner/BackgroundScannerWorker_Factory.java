package com.ashish.stash.worker.scanner;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.ashish.stash.core.database.repository.DocumentRepository;
import com.ashish.stash.core.notification.NotificationHelper;
import dagger.internal.DaggerGenerated;
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
public final class BackgroundScannerWorker_Factory {
  private final Provider<DocumentRepository> repositoryProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  public BackgroundScannerWorker_Factory(Provider<DocumentRepository> repositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.repositoryProvider = repositoryProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public BackgroundScannerWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get(), notificationHelperProvider.get());
  }

  public static BackgroundScannerWorker_Factory create(
      Provider<DocumentRepository> repositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new BackgroundScannerWorker_Factory(repositoryProvider, notificationHelperProvider);
  }

  public static BackgroundScannerWorker newInstance(Context context, WorkerParameters workerParams,
      DocumentRepository repository, NotificationHelper notificationHelper) {
    return new BackgroundScannerWorker(context, workerParams, repository, notificationHelper);
  }
}
