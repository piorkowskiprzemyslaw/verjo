package pl.ppiorkowski.verjo.model.db_engine;

public class NullDatabaseEngineDefinitionException extends RuntimeException {
    public NullDatabaseEngineDefinitionException() {
        super("Provided database engine object is null!");
    }
}
