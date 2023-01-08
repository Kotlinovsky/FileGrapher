package io.kotlinovsky.filegrapher.analyzers.impl;

import io.kotlinovsky.filegrapher.analyzers.IFileAnalyzer;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация [IFileAnalyzer].
 * Осуществляет поиск зависимостей, указанных с помощью директивы require.
 */
public class FileAnalyzerImpl implements IFileAnalyzer {

    private static final String REQUIRE_PATH_GROUP = "Path";
    private static final String REQUIRE_PATTERN_STRING = "^require\\s'(?<" + REQUIRE_PATH_GROUP + ">[^']+)'\\s*$";
    private static final Pattern REQUIRE_PATTERN = Pattern.compile(REQUIRE_PATTERN_STRING);

    @Inject
    public FileAnalyzerImpl() {
    }

    @Nonnull
    @Override
    public Set<Path> getDependencies(Path rootDir, Path path) throws IOException {
        try (Stream<String> linesStream = Files.lines(path)) {
            return linesStream
                    .map(REQUIRE_PATTERN::matcher)
                    .filter(Matcher::matches)
                    .map(matcher -> matcher.group(REQUIRE_PATH_GROUP))
                    .map(dependency -> dependency.replaceAll("\\\\", File.separator))
                    .map(dependency -> dependency.replaceAll("/", File.separator))
                    .map(rootDir::resolve)
                    .filter(Files::exists)
                    .collect(Collectors.toSet());
        }
    }
}
