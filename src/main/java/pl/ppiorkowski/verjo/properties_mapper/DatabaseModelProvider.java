package pl.ppiorkowski.verjo.properties_mapper;

import pl.ppiorkowski.verjo.properties_mapper.vertabelo_xml_reader.VertabeloXMLReader;
import pl.ppiorkowski.verjo.xsd.DatabaseModel;

import javax.xml.bind.JAXB;
import java.io.InputStream;

public class DatabaseModelProvider {

    private final String vertabeloXMLFilePath;
    private final VertabeloXMLReader reader;
    private DatabaseModel model;

    public DatabaseModelProvider(String vertabeloXMLFilePath, VertabeloXMLReader reader) {
        this.vertabeloXMLFilePath = vertabeloXMLFilePath;
        this.reader = reader;
    }

    public DatabaseModelProvider(String vertabeloXMLFilePath) {
        this.vertabeloXMLFilePath = vertabeloXMLFilePath;
        reader = new VertabeloXMLReader();
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
