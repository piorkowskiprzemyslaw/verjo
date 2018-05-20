package pl.ppiorkowski.verjo.properties_mapper.vertabelo_xml_reader;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VertabeloXMLReaderTest {

    private FileSystem fs;
    private VertabeloXMLReader xmlReader;

    @BeforeEach
    void prepareFS() {
        Configuration configuration = Configuration.unix().toBuilder()
                .setAttributeViews("basic", "posix")
                .build();
        fs = Jimfs.newFileSystem(configuration);
        xmlReader = new VertabeloXMLReader(fs);
    }

    @Test
    void shouldReadExistingFile() throws IOException {
        // given
        Path path = fs.getPath("bar.xml");
        Files.createFile(path);
        Files.write(path, "sample content".getBytes());

        // when
        InputStream inputStream = xmlReader.readFromFile("bar.xml");

        // then
        String fileContent = read(inputStream);
        assertEquals(fileContent, "sample content");
    }

    @Test
    void shouldThrowExceptionWhenFileDoesNotExist() {
        // when & then
        assertThrows(VertabeloXMLFileNotExists.class,
                () -> xmlReader.readFromFile("notExists.xml"),
                "File not found: notExists.xml");
    }

    @Test
    @Disabled("right now jimfs does not support testing file access scenarios")
    void shouldThrowExceptionForMissingReadPermission() throws IOException {
        // given
        Path path = fs.getPath("noReadPerm.xml");
        FileAttribute<Set<PosixFilePermission>> permissions = PosixFilePermissions.asFileAttribute(emptySet());
        Files.createFile(path, permissions);
        Files.write(path, "restricted content".getBytes());

        // when & then
        assertThrows(VertabeloXMLFileNotReadable.class,
                () -> xmlReader.readFromFile("noReadPerm.xml"),
                "Missing read permission for file: noReadPerm.xml");
    }

    private String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    @AfterEach
    void tearDownFS() throws IOException {
        fs.close();
    }

}