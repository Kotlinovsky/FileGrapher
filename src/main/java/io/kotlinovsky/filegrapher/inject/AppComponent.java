package io.kotlinovsky.filegrapher.inject;

import dagger.Component;
import io.kotlinovsky.filegrapher.inject.modules.AnalyzersModule;
import io.kotlinovsky.filegrapher.inject.modules.InteractorsModule;
import io.kotlinovsky.filegrapher.interactors.IConcatFilesInteractor;
import io.kotlinovsky.filegrapher.interactors.ICycleCheckInteractor;
import io.kotlinovsky.filegrapher.interactors.IGraphBuildInteractor;

/**
 * Компонент с зависимостями приложения.
 */
@AppScope
@Component(modules = {AnalyzersModule.class, InteractorsModule.class})
public interface AppComponent {
    IConcatFilesInteractor concatFilesInteractor();
    ICycleCheckInteractor cycleCheckInteractor();
    IGraphBuildInteractor graphBuildInteractor();
}
