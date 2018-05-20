package pl.ppiorkowski.verjo.properties_mapper.vertabelo_xml_reader;

import java.nio.file.Path;

class VertabeloXMLFileNotReadable extends RuntimeException {
    private static final String MSG_TEMPLATE = "Missing read permission for file: %s";

    VertabeloXMLFileNotReadable(Path path) {
        super(String.format(MSG_TEMPLATE, path.toAbsolutePath().toString()));
    }
}
