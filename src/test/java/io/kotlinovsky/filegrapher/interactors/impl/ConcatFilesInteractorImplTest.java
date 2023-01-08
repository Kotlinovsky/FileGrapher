package io.kotlinovsky.filegrapher.interactors.impl;

import io.kotlinovsky.filegrapher.interactors.IConcatFilesInteractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для [ConcatFilesInteractorImpl].
 */
public class ConcatFilesInteractorImplTest {

    private IConcatFilesInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new ConcatFilesInteractorImpl();
    }

    @Test
    public void testNormalFilesConcat() throws IOException {
        Path tempDirectoryPath = Files.createTempDirectory("test-analyzer");
        Path tempFirstFilePath = Files.createFile(tempDirectoryPath.resolve("first-file"));
        Path tempSecondFilePath = Files.createFile(tempDirectoryPath.resolve("second-file"));
        Files.writeString(tempFirstFilePath, "SECOND");
        Files.writeString(tempSecondFilePath, "FIRST");

        List<String> resultLines = new ArrayList<>();
        interactor.concatFiles(List.of(tempFirstFilePath, tempSecondFilePath), resultLines::add);
        assertEquals(2, resultLines.size());
        assertEquals("SECOND", resultLines.get(0));
        assertEquals("FIRST", resultLines.get(1));
    }

    @Test
    public void testWhenExceptionOccurred() throws IOException {
        Path tempDirectoryPath = Files.createTempDirectory("test-analyzer");
        Path tempFirstFilePath = Files.createFile(tempDirectoryPath.resolve("first-file"));
        Path tempSecondFilePath = tempDirectoryPath.resolve("second-file");
        Files.writeString(tempFirstFilePath, "FIRST");

        // Должны успеть прочитать первый файл и на втором выкинуть ошибку.
        List<String> resultLines = new ArrayList<>();
        assertThrows(IOException.class, () -> interactor.concatFiles(List.of(tempFirstFilePath, tempSecondFilePath), resultLines::add));
        assertEquals(1, resultLines.size());
        assertEquals("FIRST", resultLines.get(0));
    }
}
