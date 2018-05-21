package pl.ppiorkowski.verjo.model.provider;

import java.nio.file.Path;

class VerJoXMLFileNotExistsException extends RuntimeException {
    private static final String MSG_TEMPLATE = "File not found: %s";

    VerJoXMLFileNotExistsException(Path path) {
        super(String.format(MSG_TEMPLATE, path.toAbsolutePath().toString()));
    }
}
