package com.ashish.stash.core.work;

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
    topLevelClass = OcrProcessingWorker.class
)
public interface OcrProcessingWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.ashish.stash.core.work.OcrProcessingWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(
      OcrProcessingWorker_AssistedFactory factory);
}
