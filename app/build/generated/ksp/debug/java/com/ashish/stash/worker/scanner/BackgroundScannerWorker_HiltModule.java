package com.ashish.stash.worker.scanner;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = BackgroundScannerWorker.class
)
public interface BackgroundScannerWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.ashish.stash.worker.scanner.BackgroundScannerWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(
      BackgroundScannerWorker_AssistedFactory factory);
}
