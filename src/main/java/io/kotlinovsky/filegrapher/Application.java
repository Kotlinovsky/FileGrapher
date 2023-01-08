package io.kotlinovsky.filegrapher;

import io.kotlinovsky.filegrapher.inject.AppComponent;
import io.kotlinovsky.filegrapher.inject.DaggerAppComponent;
import io.kotlinovsky.filegrapher.models.FileModel;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.List;

/**
 * Основной класс приложения.
 * Осуществляет взаимодействие с компонентами логики и пользователем.
 */
public class Application {

    private final Path path;
    private final AppComponent component = DaggerAppComponent
            .builder()
            .build();

    public Application(Path path) {
        this.path = path;
    }

    public void run() {
        List<FileModel> fileModels;

        // 1) Построим граф файлов в директории.
        try {
            fileModels = component.graphBuildInteractor().buildGraph(path);
        } catch (NoSuchFileException e) {
            System.err.println("No such file: " + e.getMessage());
            return;
        } catch (NotDirectoryException e) {
            System.err.println("Not a directory: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("Unexpected error during graph building: " + e.getMessage());
            return;
        }

        // 2) Теперь проверим граф на наличие циклов.
        if (component.cycleCheckInteractor().checkCircularDependencies(fileModels)) {
            System.err.println("Cycle found! Please recheck you graph!");
            return;
        }

        // 3) Если же циклов нет, то произведем конкатенацию файлов и выведем результат в консоль.
        // Предварительно также отсортируем список файлов в нужном нам порядке - т.е. по убыванию зависимости.
        List<Path> paths = fileModels.stream()
                .sorted((first, second) -> -first.compareTo(second))
                .map(FileModel::path)
                .toList();

        try {
            component.concatFilesInteractor().concatFiles(paths, System.out::println);
        } catch (IOException e) {
            System.err.println("Error during files concat: " + e.getMessage());
        }
    }
}
