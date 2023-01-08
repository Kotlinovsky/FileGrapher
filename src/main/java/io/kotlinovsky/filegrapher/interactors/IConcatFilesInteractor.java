package io.kotlinovsky.filegrapher.interactors;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * Интерактор, выполняющий функцию объединения файлов.
 */
public interface IConcatFilesInteractor {
    /**
     * Объединяет указанные файлы в переданном порядке.
     *
     * @param paths      Пути до файлов, которые нужно объединить.
     * @param onLineRead Кэллбэк, возвращающий строки результирующего файла.
     * @throws IOException Если не удалось совершить IO-операцию с файлом.
     */
    void concatFiles(List<Path> paths, Consumer<String> onLineRead) throws IOException;
}
