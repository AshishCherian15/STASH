package com.ashish.stash;

import androidx.hilt.work.HiltWorkerFactory;
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
public final class StashApplication_MembersInjector implements MembersInjector<StashApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public StashApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<StashApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new StashApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(StashApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.ashish.stash.StashApplication.workerFactory")
  public static void injectWorkerFactory(StashApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
