package pl.ppiorkowski.verjo.properties_mapper.vertabelo_xml_reader;

import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class VertabeloXMLReader {

    private final FileSystem fileSystem;

    public VertabeloXMLReader() {
        fileSystem = FileSystems.getDefault();
    }

    public InputStream readFromFile(String pathToFile) {
        Path path = fileSystem.getPath(pathToFile);
        checkFileExists(path);
        checkFileReadable(path);

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void checkFileReadable(Path path) {
        if (!Files.isReadable(path)) {
            throw new VertabeloXMLFileNotReadable(path);
        }
    }

    private static void checkFileExists(Path path) {
        if (!Files.exists(path)) {
            throw new VertabeloXMLFileNotExists(path);
        }
    }
}
