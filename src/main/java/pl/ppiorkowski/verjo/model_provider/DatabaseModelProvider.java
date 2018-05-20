package pl.ppiorkowski.verjo.model_provider;

import pl.ppiorkowski.verjo.model_provider.xml_file_reader.VerJoXMLReader;
import pl.ppiorkowski.verjo.xsd.DatabaseModel;

import javax.xml.bind.JAXB;
import java.io.InputStream;

public class DatabaseModelProvider {

    private final String vertabeloXMLFilePath;
    private final VerJoXMLReader reader;
    private DatabaseModel model;

    public DatabaseModelProvider(String vertabeloXMLFilePath) {
        this(vertabeloXMLFilePath, new VerJoXMLReader());
    }

    DatabaseModelProvider(String vertabeloXMLFilePath, VerJoXMLReader reader) {
        this.vertabeloXMLFilePath = vertabeloXMLFilePath;
        this.reader = reader;
    }

    public DatabaseModel getModel() {
        if (model == null) {
            initializeDbModel();
        }
        return model;
    }

    private void initializeDbModel() {
        InputStream is = reader.readFromFile(vertabeloXMLFilePath);
        model = JAXB.unmarshal(is, DatabaseModel.class);
    }
}
