package com.github.piorkowskiprzemyslaw.verjo.model.provider;

class VerJoXMLFilePathNullOrEmptyException extends RuntimeException {
    VerJoXMLFilePathNullOrEmptyException() {
        super("File path is null or empty!");
    }
}
