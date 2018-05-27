package com.github.piorkowskiprzemyslaw.verjo.model.db_engine;

class NullDatabaseEngineDefinitionException extends RuntimeException {
    NullDatabaseEngineDefinitionException() {
        super("Provided database engine object is null!");
    }
}
