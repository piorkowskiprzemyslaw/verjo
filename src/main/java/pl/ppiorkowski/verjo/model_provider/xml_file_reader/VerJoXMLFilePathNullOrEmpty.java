package pl.ppiorkowski.verjo.model_provider.xml_file_reader;

public class VerJoXMLFilePathNullOrEmpty extends RuntimeException {
    public VerJoXMLFilePathNullOrEmpty() {
        super("File path is null or empty!");
    }
}
