package pl.ppiorkowski.verjo.model.provider;

import java.io.InputStream;

import javax.xml.bind.JAXB;

import pl.ppiorkowski.verjo.xsd.DatabaseModel;

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
