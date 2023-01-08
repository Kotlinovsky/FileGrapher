package io.kotlinovsky.filegrapher.interactors.impl;

import io.kotlinovsky.filegrapher.interactors.ICycleCheckInteractor;
import io.kotlinovsky.filegrapher.models.FileModel;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация [ICycleCheckInteractor].
 * Принцип работы заключается в обходе графа в глубину,
 * запоминании и сверки статусов обработки вершин.
 */
public class CycleCheckInteractorImpl implements ICycleCheckInteractor {

    @Inject
    public CycleCheckInteractorImpl() {
    }

    @Override
    public boolean checkCircularDependencies(@Nonnull List<FileModel> graph) {
        Map<Path, VertexVisitStatus> visitStatuses = new HashMap<>();
        Map<Path, Set<Path>> neighboursMap = graph
                .stream()
                .collect(Collectors.toMap(FileModel::path, FileModel::dependsFrom));

        return graph
                .stream()
                .map(FileModel::path)
                .anyMatch(path -> checkCycle(path, neighboursMap, visitStatuses));
    }

    private boolean checkCycle(Path path, Map<Path, Set<Path>> neighboursMap, Map<Path, VertexVisitStatus> visitStatuses) {
        VertexVisitStatus status = visitStatuses.get(path);

        // Если вершина уже была обработана, то никакого цикла нет.
        // Если же вершина находится в обработке, то значит, что мы нарвались на цикл.
        if (status == VertexVisitStatus.COMPLETE) {
            return false;
        } else if (status == VertexVisitStatus.PENDING) {
            return true;
        }

        // Ставим метку о том, что текущая вершина обрабатывается.
        // Если мы наткнемся на обрабатываемую вершину, то очевидно, что возник цикл.
        visitStatuses.put(path, VertexVisitStatus.PENDING);
        for (Path neighbourPath : neighboursMap.get(path)) {
            if (checkCycle(neighbourPath, neighboursMap, visitStatuses)) {
                return true;
            }
        }
        visitStatuses.put(path, VertexVisitStatus.COMPLETE);
        return false;
    }

    /**
     * Статус посещенности вершины.
     * Используется для обнаружения циклов в графе.
     */
    private enum VertexVisitStatus {
        /**
         * Вершина находится в обработке.
         */
        PENDING,
        /**
         * Вершина уже была обработана.
         */
        COMPLETE
    }
}
