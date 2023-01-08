package io.kotlinovsky.filegrapher.models;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для [FileModel].
 */
public class FileModelTest {

    @Test
    public void testThatCyclesNotCausesStackOverflowExceptionDuringSort() {
        Path firstPath = Path.of("1");
        Path secondPath = Path.of("2");
        Collections.sort(new ArrayList<>(List.of(
                new FileModel(firstPath, Set.of(secondPath)),
                new FileModel(secondPath, Set.of(firstPath))
        )));
    }

    @Test
    public void checkThatSortsValid() {
        Path zeroPath = Path.of("0");
        Path firstPath = Path.of("1");
        Path secondPath = Path.of("2");
        Path thirdPath = Path.of("3");
        Path fourthPath = Path.of("4");

        // В пределах 1-й компоненты смежности
        assertEquals(-1, new FileModel(thirdPath, Set.of(secondPath))
                .compareTo(new FileModel(secondPath, Set.of(zeroPath, firstPath))));
        assertEquals(1, new FileModel(secondPath, Set.of(zeroPath, firstPath))
                .compareTo(new FileModel(thirdPath, Set.of(secondPath))));

        // Если компоненты смежности разные.
        assertEquals(-1, new FileModel(firstPath, Set.of(secondPath, thirdPath))
                .compareTo(new FileModel(fourthPath, Set.of())));
        assertEquals(1, new FileModel(fourthPath, Set.of())
                .compareTo(new FileModel(firstPath, Set.of(secondPath, thirdPath))));
        assertEquals(-1, new FileModel(zeroPath, Set.of(fourthPath))
                .compareTo(new FileModel(firstPath, Set.of(secondPath, thirdPath))));
        assertEquals(1, new FileModel(firstPath, Set.of(secondPath, thirdPath))
                .compareTo(new FileModel(zeroPath, Set.of(fourthPath))));
        assertEquals(1, new FileModel(secondPath, Set.of())
                .compareTo(new FileModel(firstPath, Set.of(secondPath, thirdPath))));
        assertEquals(1, new FileModel(secondPath, Set.of())
                .compareTo(new FileModel(zeroPath, Set.of(fourthPath))));
        assertEquals(-1, new FileModel(zeroPath, Set.of(fourthPath))
                .compareTo(new FileModel(secondPath, Set.of())));
    }
}
