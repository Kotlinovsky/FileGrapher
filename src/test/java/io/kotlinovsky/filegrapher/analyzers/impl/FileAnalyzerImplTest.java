package io.kotlinovsky.filegrapher.analyzers.impl;

import io.kotlinovsky.filegrapher.analyzers.IFileAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для [FileAnalyzerImpl].
 */
public class FileAnalyzerImplTest {

    private IFileAnalyzer fileAnalyzer;

    @BeforeEach
    void setUp() {
        fileAnalyzer = new FileAnalyzerImpl();
    }

    @Test
    public void testWhenRequireOperatorPrintedNotInBegin() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "f require '" + tempDependencyFilePath + "'");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(0, dependencies.size());
    }

    @Test
    public void testWhenRequireOperatorNameNotFullyPrinted() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "req '" + tempDependencyFilePath + "'");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(0, dependencies.size());
    }

    @Test
    public void testWhenSpaceNotPrintedAfterRequireOperator() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require'" + tempDependencyFilePath + "'");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(0, dependencies.size());
    }

    @Test
    public void testWhenBeginStapleNotPrintedInRequireOperator() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require " + tempDependencyFilePath + "'");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(0, dependencies.size());
    }

    @Test
    public void testWhenEndStapleNotPrintedInRequireOperator() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require '" + tempDependencyFilePath);

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(0, dependencies.size());
    }

    @Test
    public void testWhenFileNameNotPrintedInRequireOperator() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require ''");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(0, dependencies.size());
    }

    @Test
    public void testWhenFileNotExistsInRequireOperator() throws IOException {
        // Специально создадим временный файл и удалим его, чтобы гарантировать себе его отсутствие.
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.deleteIfExists(tempDependencyFilePath);
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require '" + tempDependencyFilePath + "'");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(0, dependencies.size());
    }

    @Test
    public void testWhenRequireOperatorIsNotEndOfLine() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require '" + tempDependencyFilePath + "' fff");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(0, dependencies.size());
    }

    @Test
    public void testWhenLineNotContainsSpacesAfterRequireOperator() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require '" + tempDependencyFilePath + "'");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(1, dependencies.size());
        assertTrue(dependencies.contains(tempDependencyFilePath));
    }

    @Test
    public void testWhenLineContainsSpacesAfterRequireOperator() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require '" + tempDependencyFilePath + "'  ");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(1, dependencies.size());
        assertTrue(dependencies.contains(tempDependencyFilePath));
    }

    @Test
    public void testWhenLineContainsAnotherSymbolsAfterRequireOperator() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require '" + tempDependencyFilePath + "' ff");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(0, dependencies.size());
    }

    @Test
    public void testRequireOperatorWithLeftSlashes() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require '" + tempDependencyFilePath.toString().replaceAll("/", "\\\\") + "'");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(1, dependencies.size());
        assertTrue(dependencies.contains(tempDependencyFilePath));
    }

    @Test
    public void testRequireOperatorWithRightSlashes() throws IOException {
        Path tempFilePath = Files.createTempFile("test", "analyzer");
        Path tempDependencyFilePath = Files.createTempFile("test", "analyzer");
        Files.writeString(tempFilePath, "require '" + tempDependencyFilePath.toString().replaceAll("\\\\", "/") + "'");

        Set<Path> dependencies = fileAnalyzer.getDependencies(Paths.get(""), tempFilePath);
        assertEquals(1, dependencies.size());
        assertTrue(dependencies.contains(tempDependencyFilePath));
    }
}
