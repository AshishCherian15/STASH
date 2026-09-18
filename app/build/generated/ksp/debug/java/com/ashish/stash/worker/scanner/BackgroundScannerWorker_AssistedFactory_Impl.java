package com.ashish.stash.worker.scanner;

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
public final class BackgroundScannerWorker_AssistedFactory_Impl implements BackgroundScannerWorker_AssistedFactory {
  private final BackgroundScannerWorker_Factory delegateFactory;

  BackgroundScannerWorker_AssistedFactory_Impl(BackgroundScannerWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public BackgroundScannerWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<BackgroundScannerWorker_AssistedFactory> create(
      BackgroundScannerWorker_Factory delegateFactory) {
    return InstanceFactory.create(new BackgroundScannerWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<BackgroundScannerWorker_AssistedFactory> createFactoryProvider(
      BackgroundScannerWorker_Factory delegateFactory) {
    return InstanceFactory.create(new BackgroundScannerWorker_AssistedFactory_Impl(delegateFactory));
  }
}
