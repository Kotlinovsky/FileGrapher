package io.kotlinovsky.filegrapher.interactors.impl;

import io.kotlinovsky.filegrapher.interactors.IConcatFilesInteractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Реализация [IConcatFilesInteractor].
 * Осуществляет поочередное чтение файлов и построчную отправку их содержимого в кэллбэк,
 * за счет чего не расходует много ОЗУ на хранение содержимого итогового файла.
 */
public class ConcatFilesInteractorImpl implements IConcatFilesInteractor {

    @Override
    public void concatFiles(List<Path> paths, Consumer<String> onLineRead) throws IOException {
        for (Path path : paths) {
            try (Stream<String> linesStream = Files.lines(path)) {
                linesStream.forEach(onLineRead);
            }
        }
    }
}
