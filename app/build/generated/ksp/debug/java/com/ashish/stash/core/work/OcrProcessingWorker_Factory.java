package com.ashish.stash.core.work;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.ashish.stash.core.database.repository.DocumentRepository;
import com.ashish.stash.core.ocr.OcrService;
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
public final class OcrProcessingWorker_Factory {
  private final Provider<DocumentRepository> repositoryProvider;

  private final Provider<OcrService> ocrServiceProvider;

  public OcrProcessingWorker_Factory(Provider<DocumentRepository> repositoryProvider,
      Provider<OcrService> ocrServiceProvider) {
    this.repositoryProvider = repositoryProvider;
    this.ocrServiceProvider = ocrServiceProvider;
  }

  public OcrProcessingWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get(), ocrServiceProvider.get());
  }

  public static OcrProcessingWorker_Factory create(Provider<DocumentRepository> repositoryProvider,
      Provider<OcrService> ocrServiceProvider) {
    return new OcrProcessingWorker_Factory(repositoryProvider, ocrServiceProvider);
  }

  public static OcrProcessingWorker newInstance(Context context, WorkerParameters workerParams,
      DocumentRepository repository, OcrService ocrService) {
    return new OcrProcessingWorker(context, workerParams, repository, ocrService);
  }
}
