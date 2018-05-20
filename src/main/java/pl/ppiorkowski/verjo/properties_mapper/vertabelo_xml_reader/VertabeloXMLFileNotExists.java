package pl.ppiorkowski.verjo.properties_mapper.vertabelo_xml_reader;

import java.nio.file.Path;

class VertabeloXMLFileNotExists extends RuntimeException {
    private static final String MSG_TEMPLATE = "File not found: %s";

    VertabeloXMLFileNotExists(Path path) {
        super(String.format(MSG_TEMPLATE, path.toAbsolutePath().toString()));
    }

    VertabeloXMLFileNotExists(Path path, Exception ex) {
        super(String.format(MSG_TEMPLATE, path.toAbsolutePath().toString()), ex);
    }
}
