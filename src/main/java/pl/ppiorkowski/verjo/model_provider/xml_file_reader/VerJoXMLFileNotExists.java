package pl.ppiorkowski.verjo.model_provider.xml_file_reader;

import java.nio.file.Path;

class VerJoXMLFileNotExists extends RuntimeException {
    private static final String MSG_TEMPLATE = "File not found: %s";

    VerJoXMLFileNotExists(Path path) {
        super(String.format(MSG_TEMPLATE, path.toAbsolutePath().toString()));
    }
}
