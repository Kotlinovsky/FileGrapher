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
public record FileModel(@Nonnull Path path, @Nonnull Set<Path> dependsFrom) {
}
