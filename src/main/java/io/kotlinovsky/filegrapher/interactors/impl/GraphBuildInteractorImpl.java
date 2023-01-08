package io.kotlinovsky.filegrapher.interactors.impl;

import io.kotlinovsky.filegrapher.analyzers.IFileAnalyzer;
import io.kotlinovsky.filegrapher.interactors.IGraphBuildInteractor;
import io.kotlinovsky.filegrapher.models.FileModel;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Реализация [IGraphBuildInteractor].
 * Обходит директорию и ее поддиректории, анализируя файлы с помощью [IFileAnalyzer].
 */
public class GraphBuildInteractorImpl implements IGraphBuildInteractor {

    private final IFileAnalyzer fileAnalyzer;

    @Inject
    public GraphBuildInteractorImpl(@Nonnull IFileAnalyzer fileAnalyzer) {
        this.fileAnalyzer = fileAnalyzer;
    }

    @Nonnull
    @Override
    public List<FileModel> buildGraph(@Nonnull Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new NoSuchFileException(path.toString());
        }

        if (!Files.isDirectory(path)) {
            throw new NotDirectoryException(path.toString());
        }

        try (Stream<Path> pathsStream = Files.walk(path)) {
            List<FileModel> fileModels = new ArrayList<>();
            Iterator<Path> iterator = pathsStream
                    .filter(Files::isRegularFile)
                    .iterator();

            while (iterator.hasNext()) {
                Path current = iterator.next();
                Set<Path> dependsFrom = fileAnalyzer.getDependencies(current);
                FileModel fileModel = new FileModel(current, dependsFrom);
                fileModels.add(fileModel);
            }

            return fileModels;
        }
    }
}
