package io.kotlinovsky.filegrapher.interactors;

import io.kotlinovsky.filegrapher.models.FileModel;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Интерфейс интерактора - построителя графа.
 * Осуществляет обход директории и соответственно построение графа файлов
 * в ней (в том числе и поддиректорий).
 */
public interface IGraphBuildInteractor {
    /**
     * Строит граф зависимостей файлов.
     * Возвращает список смежностей вершин - файлов.
     * Реализация может возвращать вершины в любом порядке.
     *
     * @param path Путь до папки, для которой будем строить граф.
     * @return Список смежностей вершин - файлов.
     * @throws IOException В случае если не удалось совершить IO-операцию.
     */
    @Nonnull
    List<FileModel> buildGraph(@Nonnull Path path) throws IOException;
}
