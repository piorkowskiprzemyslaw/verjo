package pl.ppiorkowski.verjo.model.provider;

class VerJoXMLFilePathNullOrEmptyException extends RuntimeException {
    VerJoXMLFilePathNullOrEmptyException() {
        super("File path is null or empty!");
    }
}
