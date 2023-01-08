package io.kotlinovsky.filegrapher.interactors;

import io.kotlinovsky.filegrapher.models.FileModel;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Интерфейс интерактора, проверяющего файлы в графе на циклические зависимости.
 * Осуществляет обход графа и проверку его вершин на циклы между ними.
 */
public interface ICycleCheckInteractor {
    /**
     * Проверяет граф на циклические зависимости между файлами.
     *
     * @param graph Граф в виде списка смежной вершин - файлов.
     * @return True - если найдена циклическая зависимость, False - если не найдена.
     */
    boolean checkCircularDependencies(@Nonnull List<FileModel> graph);
}
