package pl.ppiorkowski.verjo.model_provider.xml_file_reader;

import java.nio.file.Path;

class VerJoXMLFileNotReadableException extends RuntimeException {
    private static final String MSG_TEMPLATE = "Missing read permission for file: %s";

    VerJoXMLFileNotReadableException(Path path) {
        super(String.format(MSG_TEMPLATE, path.toAbsolutePath().toString()));
    }
}
