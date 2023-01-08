package io.kotlinovsky.filegrapher;

import java.nio.file.Path;

/**
 * Класс - стартер приложения.
 */
public final class Runner {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Example: java -jar program.jar <path>");
            return;
        }

        new Application(Path.of(args[0])).run();
    }
}
