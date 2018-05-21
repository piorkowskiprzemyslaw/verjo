package pl.ppiorkowski.verjo.model_provider.xml_file_reader;

public class VerJoXMLFilePathNullOrEmptyException extends RuntimeException {
    public VerJoXMLFilePathNullOrEmptyException() {
        super("File path is null or empty!");
    }
}
