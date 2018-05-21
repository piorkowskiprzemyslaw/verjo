package pl.ppiorkowski.verjo.model;

import lombok.RequiredArgsConstructor;
import org.jooq.SQLDialect;
import pl.ppiorkowski.verjo.model.db_engine.DbEngineConverter;
import pl.ppiorkowski.verjo.xsd.DatabaseEngine;
import pl.ppiorkowski.verjo.xsd.DatabaseModel;

@RequiredArgsConstructor
public class VertabeloDbModel {
    private final DatabaseModel databaseModel;

    public SQLDialect getDialect() {
        DatabaseEngine dbEngine = databaseModel.getDatabaseEngine();
        return DbEngineConverter.asSQLDialect(dbEngine);
    }

}
