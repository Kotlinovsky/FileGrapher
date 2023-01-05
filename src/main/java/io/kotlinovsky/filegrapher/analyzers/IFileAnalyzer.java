package io.kotlinovsky.filegrapher.analyzers;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * Интерфейс анализатора файлов.
 * Реализует обработку синтаксиса определенного языка.
 */
public interface IFileAnalyzer {
    /**
     * Возвращает пути до зависимостей файла.
     *
     * @param path Путь до анализируемого файла.
     * @return Множество путей зависимостей анализируемого файла.
     */
    @Nonnull
    Set<Path> getDependencies(Path path) throws IOException;
}
