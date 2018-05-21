package pl.ppiorkowski.verjo.model_provider.xml_file_reader;

import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class VerJoXMLReader {

    private final FileSystem fileSystem;

    public VerJoXMLReader() {
        fileSystem = FileSystems.getDefault();
    }

    public InputStream readFromFile(String pathToFile) {
        validatePathToFile(pathToFile);
        Path path = fileSystem.getPath(pathToFile);
        checkFileExists(path);
        checkFileReadable(path);

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void validatePathToFile(String pathToFile) {
        if (pathToFile == null || pathToFile.isEmpty()) {
            throw new VerJoXMLFilePathNullOrEmptyException();
        }
    }

    private static void checkFileReadable(Path path) {
        if (!Files.isReadable(path)) {
            throw new VerJoXMLFileNotReadableException(path);
        }
    }

    private static void checkFileExists(Path path) {
        if (!Files.exists(path)) {
            throw new VerJoXMLFileNotExistsException(path);
        }
    }
}
