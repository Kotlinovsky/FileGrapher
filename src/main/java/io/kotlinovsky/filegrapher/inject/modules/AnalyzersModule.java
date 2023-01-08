package io.kotlinovsky.filegrapher.inject.modules;

import dagger.Binds;
import dagger.Module;
import io.kotlinovsky.filegrapher.analyzers.IFileAnalyzer;
import io.kotlinovsky.filegrapher.analyzers.impl.FileAnalyzerImpl;
import io.kotlinovsky.filegrapher.inject.AppScope;

/**
 * Модуль для предоставления анализаторов в DI-граф.
 */
@Module
public interface AnalyzersModule {

    @Binds
    @AppScope
    IFileAnalyzer bindFileAnalyzer(FileAnalyzerImpl impl);
}
