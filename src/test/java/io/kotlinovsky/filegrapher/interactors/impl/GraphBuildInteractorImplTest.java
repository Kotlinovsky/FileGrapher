package io.kotlinovsky.filegrapher.interactors.impl;

import io.kotlinovsky.filegrapher.analyzers.IFileAnalyzer;
import io.kotlinovsky.filegrapher.interactors.IGraphBuildInteractor;
import io.kotlinovsky.filegrapher.models.FileModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для [GraphBuildInteractorImpl].
 */
public class GraphBuildInteractorImplTest {

    private IGraphBuildInteractor interactor;
    private IFileAnalyzer fileAnalyzer;

    @BeforeEach
    void setUp() {
        fileAnalyzer = Mockito.mock(IFileAnalyzer.class);
        interactor = new GraphBuildInteractorImpl(fileAnalyzer);
    }

    @Test
    public void checkWhenDirectoryNotExists() throws IOException {
        // Специально создадим временную папку и удалим ее, чтобы гарантировать себе ее отсутствие.
        Path tempDirectoryPath = Files.createTempDirectory("test-analyzer");
        Files.deleteIfExists(tempDirectoryPath);
        assertThrows(NoSuchFileException.class, () -> interactor.buildGraph(tempDirectoryPath));
    }

    @Test
    public void checkWhenFileIsNotDirectory() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        assertThrows(NotDirectoryException.class, () -> interactor.buildGraph(tempFilePath));
    }

    @Test
    public void checkWhenDirectoryIsEmpty() throws IOException {
        Path tempDirectoryPath = Files.createTempDirectory("test-analyzer");
        assertTrue(interactor.buildGraph(tempDirectoryPath).isEmpty());
        Mockito.verifyNoInteractions(fileAnalyzer);
    }

    @Test
    public void checkWhenFolderContainsTwoFiles() throws IOException {
        Path tempDirectoryPath = Files.createTempDirectory("test-analyzer");
        Path tempFirstFilePath = Files.createFile(tempDirectoryPath.resolve("first-file"));
        Path tempSecondFilePath = Files.createFile(tempDirectoryPath.resolve("second-file"));
        Path tempThirdFilePath = Files.createFile(tempDirectoryPath.resolve("third-file"));

        Mockito.when(fileAnalyzer.getDependencies(tempFirstFilePath)).thenReturn(Set.of(tempSecondFilePath));
        Mockito.when(fileAnalyzer.getDependencies(tempSecondFilePath)).thenReturn(Set.of(tempFirstFilePath));

        List<FileModel> fileModels = interactor.buildGraph(tempDirectoryPath);
        assertEquals(3, fileModels.size());
        assertTrue(fileModels.contains(new FileModel(tempFirstFilePath, Set.of(tempSecondFilePath))));
        assertTrue(fileModels.contains(new FileModel(tempSecondFilePath, Set.of(tempFirstFilePath))));
        assertTrue(fileModels.contains(new FileModel(tempThirdFilePath, Set.of())));
    }

    @Test
    public void checkWhenFolderContainsTwoFilesAndOneEmptyFolder() throws IOException {
        Path tempDirectoryPath = Files.createTempDirectory("test-analyzer");
        Path tempFirstFilePath = Files.createFile(tempDirectoryPath.resolve("first-file"));
        Path tempSecondFilePath = Files.createFile(tempDirectoryPath.resolve("second-file"));
        Path tempThirdFilePath = Files.createFile(tempDirectoryPath.resolve("third-file"));
        Files.createDirectory(tempDirectoryPath.resolve("empty-directory"));

        Mockito.when(fileAnalyzer.getDependencies(tempFirstFilePath)).thenReturn(Set.of(tempSecondFilePath));
        Mockito.when(fileAnalyzer.getDependencies(tempSecondFilePath)).thenReturn(Set.of(tempFirstFilePath));

        List<FileModel> fileModels = interactor.buildGraph(tempDirectoryPath);
        assertEquals(3, fileModels.size());
        assertTrue(fileModels.contains(new FileModel(tempFirstFilePath, Set.of(tempSecondFilePath))));
        assertTrue(fileModels.contains(new FileModel(tempSecondFilePath, Set.of(tempFirstFilePath))));
        assertTrue(fileModels.contains(new FileModel(tempThirdFilePath, Set.of())));
    }

    @Test
    public void checkWhenFolderContainsTwoFilesAndOneNotEmptyFolder() throws IOException {
        Path tempDirectoryPath = Files.createTempDirectory("test-analyzer");
        Path tempFirstFilePath = Files.createFile(tempDirectoryPath.resolve("first-file"));
        Path tempSecondFilePath = Files.createFile(tempDirectoryPath.resolve("second-file"));
        Path tempFirstFolderPath = Files.createDirectory(tempDirectoryPath.resolve("temp-directory"));
        Path tempThirdFilePath = Files.createFile(tempFirstFolderPath.resolve("third-file"));
        Path tempFourthFilePath = Files.createFile(tempFirstFolderPath.resolve("fourth-file"));
        Path tempFifthFilePath = Files.createFile(tempFirstFolderPath.resolve("fifth-file"));

        Mockito.when(fileAnalyzer.getDependencies(tempFirstFilePath)).thenReturn(Set.of(tempSecondFilePath));
        Mockito.when(fileAnalyzer.getDependencies(tempSecondFilePath)).thenReturn(Set.of(tempFirstFilePath));
        Mockito.when(fileAnalyzer.getDependencies(tempThirdFilePath)).thenReturn(Set.of(tempFourthFilePath));
        Mockito.when(fileAnalyzer.getDependencies(tempFourthFilePath)).thenReturn(Set.of(tempThirdFilePath));

        List<FileModel> fileModels = interactor.buildGraph(tempDirectoryPath);
        assertEquals(5, fileModels.size());
        assertTrue(fileModels.contains(new FileModel(tempFirstFilePath, Set.of(tempSecondFilePath))));
        assertTrue(fileModels.contains(new FileModel(tempSecondFilePath, Set.of(tempFirstFilePath))));
        assertTrue(fileModels.contains(new FileModel(tempThirdFilePath, Set.of(tempFourthFilePath))));
        assertTrue(fileModels.contains(new FileModel(tempFourthFilePath, Set.of(tempThirdFilePath))));
        assertTrue(fileModels.contains(new FileModel(tempFifthFilePath, Set.of())));
    }
}
