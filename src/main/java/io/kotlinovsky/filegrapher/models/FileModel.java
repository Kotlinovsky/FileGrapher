package io.kotlinovsky.filegrapher.models;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Set;

/**
 * Модель файла с информацией о его зависимостях.
 *
 * @param path        Путь до файла.
 * @param dependsFrom Пути до файлов-зависимостей.
 */
public record FileModel(@Nonnull Path path, @Nonnull Set<Path> dependsFrom) implements Comparable<FileModel> {

    @Override
    public int compareTo(FileModel model) {
        if (dependsFrom.contains(model.path) && !model.dependsFrom.contains(path)) {
            return -1;
        } else if (!dependsFrom.contains(model.path) && model.dependsFrom.contains(path)) {
            return 1;
        }

        return Math.max(Math.min(path.compareTo(model.path), 1), -1);
    }
}
