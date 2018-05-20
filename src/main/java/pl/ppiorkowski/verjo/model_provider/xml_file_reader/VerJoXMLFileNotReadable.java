package pl.ppiorkowski.verjo.model_provider.xml_file_reader;

import java.nio.file.Path;

class VerJoXMLFileNotReadable extends RuntimeException {
    private static final String MSG_TEMPLATE = "Missing read permission for file: %s";

    VerJoXMLFileNotReadable(Path path) {
        super(String.format(MSG_TEMPLATE, path.toAbsolutePath().toString()));
    }
}
