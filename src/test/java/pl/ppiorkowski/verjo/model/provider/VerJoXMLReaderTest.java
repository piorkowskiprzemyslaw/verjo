package pl.ppiorkowski.verjo.model.provider;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.*;

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

@DisplayName("VerJoXML reader should")
class VerJoXMLReaderTest {

    private FileSystem fs;
    private VerJoXMLReader xmlReader;

    @BeforeEach
    void prepareFS() {
        Configuration configuration = Configuration.unix().toBuilder()
                .setAttributeViews("basic", "posix")
                .build();
        fs = Jimfs.newFileSystem(configuration);
        xmlReader = new VerJoXMLReader(fs);
    }

    @Test
    @DisplayName("throw exception when file path is empty")
    void shouldThrowExceptionOnInvalidFilePath1() {
        // when & then
        assertThrows(VerJoXMLFilePathNullOrEmptyException.class,
                () -> xmlReader.readFromFile(""),
                "File path is null or empty!");
    }

    @Test
    @DisplayName("throw exception when file path is null")
    void shouldThrowExceptionOnInvalidFilePath2() {
        // when & then
        assertThrows(VerJoXMLFilePathNullOrEmptyException.class,
                () -> xmlReader.readFromFile(null),
                "File path is null or empty!");
    }

    @Test
    @DisplayName("read existing file content")
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
    @DisplayName("throw exception when file does not exists")
    void shouldThrowExceptionWhenFileDoesNotExist() {
        // when & then
        assertThrows(VerJoXMLFileNotExistsException.class,
                () -> xmlReader.readFromFile("notExists.xml"),
                "File not found: notExists.xml");
    }

    @Test
    @DisplayName("throw exception when existing file lacks read permission")
    @Disabled("right now jimfs does not support testing file access scenarios")
    void shouldThrowExceptionForMissingReadPermission() throws IOException {
        // given
        Path path = fs.getPath("noReadPerm.xml");
        FileAttribute<Set<PosixFilePermission>> permissions = PosixFilePermissions.asFileAttribute(emptySet());
        Files.createFile(path, permissions);
        Files.write(path, "restricted content".getBytes());

        // when & then
        assertThrows(VerJoXMLFileNotReadableException.class,
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