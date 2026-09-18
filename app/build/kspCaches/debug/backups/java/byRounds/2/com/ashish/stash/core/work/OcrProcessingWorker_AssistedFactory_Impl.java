package com.ashish.stash.core.work;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class OcrProcessingWorker_AssistedFactory_Impl implements OcrProcessingWorker_AssistedFactory {
  private final OcrProcessingWorker_Factory delegateFactory;

  OcrProcessingWorker_AssistedFactory_Impl(OcrProcessingWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public OcrProcessingWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<OcrProcessingWorker_AssistedFactory> create(
      OcrProcessingWorker_Factory delegateFactory) {
    return InstanceFactory.create(new OcrProcessingWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<OcrProcessingWorker_AssistedFactory> createFactoryProvider(
      OcrProcessingWorker_Factory delegateFactory) {
    return InstanceFactory.create(new OcrProcessingWorker_AssistedFactory_Impl(delegateFactory));
  }
}
