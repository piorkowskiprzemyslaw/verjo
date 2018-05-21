package pl.ppiorkowski.verjo.model.provider;

import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.ppiorkowski.verjo.xsd.DatabaseModel;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.jimfs.Configuration.unix;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("DatbaseModelProvider should")
class DatabaseModelProviderTest {

    private FileSystem fs;
    private DatabaseModelProvider provider;

    @BeforeEach
    void setup() {
        fs = Jimfs.newFileSystem(unix());
        provider = new DatabaseModelProvider("sample.xml", new VerJoXMLReader(fs));
    }

    @AfterEach
    void tearDown() throws IOException {
        fs.close();
    }

    @Test
    @DisplayName("read input stream with correct content")
    void shouldReadInputStreamContent() throws IOException {
        // given
        DatabaseModel dbModel = new DatabaseModel();
        dbModel.setName("database model name");
        createFileWithContent(serializeModel(dbModel));

        // when
        DatabaseModel model = provider.getModel();

        // then
        assertEquals(model.getName(), "database model name");
    }

    @Test
    @DisplayName("throw exception when input stream contains invalid data")
    void shouldThrowWhenFileContentCannotBeParsed() throws IOException {
        // given
        createFileWithContent("dummy file content");

        // when & then
        assertThrows(DataBindingException.class,
                () -> provider.getModel());
    }

    private void createFileWithContent(String fileContent) throws IOException {
        Path path = fs.getPath("sample.xml");
        Files.createFile(path);
        Files.write(path, fileContent.getBytes());
    }

    private String serializeModel(DatabaseModel dbModel) {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(dbModel, stringWriter);
        return stringWriter.toString();
    }

}