package io.kotlinovsky.filegrapher.inject.modules;

import dagger.Binds;
import dagger.Module;
import io.kotlinovsky.filegrapher.inject.AppScope;
import io.kotlinovsky.filegrapher.interactors.IConcatFilesInteractor;
import io.kotlinovsky.filegrapher.interactors.ICycleCheckInteractor;
import io.kotlinovsky.filegrapher.interactors.IGraphBuildInteractor;
import io.kotlinovsky.filegrapher.interactors.impl.ConcatFilesInteractorImpl;
import io.kotlinovsky.filegrapher.interactors.impl.CycleCheckInteractorImpl;
import io.kotlinovsky.filegrapher.interactors.impl.GraphBuildInteractorImpl;

/**
 * Модуль для предоставления интеракторов в DI-граф.
 */
@Module
public interface InteractorsModule {

    @Binds
    @AppScope
    IConcatFilesInteractor bindConcatFilesInteractor(ConcatFilesInteractorImpl impl);

    @Binds
    @AppScope
    ICycleCheckInteractor bindCycleCheckInteractor(CycleCheckInteractorImpl impl);

    @Binds
    @AppScope
    IGraphBuildInteractor bindGraphBuildInteractor(GraphBuildInteractorImpl impl);
}
