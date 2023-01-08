package io.kotlinovsky.filegrapher.interactors.impl;

import io.kotlinovsky.filegrapher.models.FileModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для [CycleCheckInteractorImpl].
 */
public class CycleCheckInteractorImplTest {

    private CycleCheckInteractorImpl interactor;

    @BeforeEach
    void setUp() {
        interactor = new CycleCheckInteractorImpl();
    }

    @Test
    public void testCycleDetectingWhen1ComponentExists() {
        Path firstFilePath = Path.of("1");
        Path secondFilePath = Path.of("2");
        Path thirdFilePath = Path.of("3");

        assertTrue(interactor.checkCircularDependencies(List.of(
                new FileModel(firstFilePath, Set.of(secondFilePath)),
                new FileModel(secondFilePath, Set.of(thirdFilePath)),
                new FileModel(thirdFilePath, Set.of(firstFilePath))
        )));
    }

    @Test
    public void testInnerCycleDetectingWhen1ComponentExists() {
        Path zeroFilePath = Path.of("0");
        Path firstFilePath = Path.of("1");
        Path secondFilePath = Path.of("2");
        Path thirdFilePath = Path.of("3");

        assertTrue(interactor.checkCircularDependencies(List.of(
                new FileModel(zeroFilePath, Set.of(firstFilePath)),
                new FileModel(firstFilePath, Set.of(secondFilePath)),
                new FileModel(secondFilePath, Set.of(thirdFilePath)),
                new FileModel(thirdFilePath, Set.of(firstFilePath))
        )));
    }

    @Test
    public void testCycleDetectingWhen2ComponentExists() {
        Path zeroFilePath = Path.of("0");
        Path firstFilePath = Path.of("1");
        Path secondFilePath = Path.of("2");
        Path thirdFilePath = Path.of("3");
        Path fourthFilePath = Path.of("4");

        assertTrue(interactor.checkCircularDependencies(List.of(
                new FileModel(zeroFilePath, Set.of(firstFilePath)),
                new FileModel(fourthFilePath, Set.of(firstFilePath)),
                new FileModel(firstFilePath, Set.of(secondFilePath)),
                new FileModel(secondFilePath, Set.of(thirdFilePath)),
                new FileModel(thirdFilePath, Set.of(firstFilePath))
        )));
    }

    @Test
    public void testSimpleCycleDetecting() {
        Path zeroFilePath = Path.of("0");
        Path firstFilePath = Path.of("1");

        assertTrue(interactor.checkCircularDependencies(List.of(
                new FileModel(zeroFilePath, Set.of(firstFilePath)),
                new FileModel(firstFilePath, Set.of(zeroFilePath))
        )));
    }

    @Test
    public void checkThatFalseWhenCycleNotExistsWhenContains1Component() {
        Path zeroFilePath = Path.of("0");
        Path firstFilePath = Path.of("1");
        Path secondFilePath = Path.of("2");
        Path thirdFilePath = Path.of("3");
        Path fourthFilePath = Path.of("4");

        assertFalse(interactor.checkCircularDependencies(List.of(
                new FileModel(zeroFilePath, Set.of(firstFilePath, secondFilePath)),
                new FileModel(firstFilePath, Set.of(thirdFilePath, fourthFilePath)),
                new FileModel(secondFilePath, Set.of()),
                new FileModel(thirdFilePath, Set.of()),
                new FileModel(fourthFilePath, Set.of())
        )));
    }

    @Test
    public void checkThatFalseWhenCycleNotExistsWhenContains2Component() {
        Path zeroFilePath = Path.of("0");
        Path firstFilePath = Path.of("1");
        Path secondFilePath = Path.of("2");
        Path thirdFilePath = Path.of("3");
        Path fourthFilePath = Path.of("4");
        Path fifthFilePath = Path.of("5");

        assertFalse(interactor.checkCircularDependencies(List.of(
                new FileModel(zeroFilePath, Set.of(firstFilePath, secondFilePath)),
                new FileModel(firstFilePath, Set.of(thirdFilePath, fourthFilePath)),
                new FileModel(secondFilePath, Set.of()),
                new FileModel(thirdFilePath, Set.of()),
                new FileModel(fourthFilePath, Set.of()),
                new FileModel(fifthFilePath, Set.of(firstFilePath))
        )));
    }

    @Test
    public void checkThatTrueWhenFirstNotContainsCycleButSecondContains() {
        Path zeroFilePath = Path.of("0");
        Path firstFilePath = Path.of("1");
        Path secondFilePath = Path.of("2");
        Path thirdFilePath = Path.of("3");
        Path fourthFilePath = Path.of("4");
        Path fifthFilePath = Path.of("5");

        assertTrue(interactor.checkCircularDependencies(List.of(
                new FileModel(zeroFilePath, Set.of(firstFilePath, secondFilePath)),
                new FileModel(firstFilePath, Set.of(thirdFilePath)),
                new FileModel(secondFilePath, Set.of(thirdFilePath)),
                new FileModel(thirdFilePath, Set.of()),
                new FileModel(fourthFilePath, Set.of(fifthFilePath)),
                new FileModel(fifthFilePath, Set.of(fourthFilePath))
        )));
    }

    @Test
    public void checkThatFalseWhenGraphEmpty() {
        assertFalse(interactor.checkCircularDependencies(List.of()));
    }
}
