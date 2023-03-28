package com.github.piorkowskiprzemyslaw.verjo.model.db_engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.SQLDialect;
import org.jooq.tools.JooqLogger;

import com.github.piorkowskiprzemyslaw.verjo.xsd.DatabaseEngine;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import static org.jooq.SQLDialect.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DbEngineConverter {
    private static final JooqLogger log = JooqLogger.getLogger(DbEngineConverter.class);

    private static final Map<DatabaseEngine, SQLDialect> supportedEngines = new HashMap<>();

    static {
        supportedEngines.put(engineFrom("hsqldb", "2.3.x"), HSQLDB);
        supportedEngines.put(engineFrom("ibm_db2", "9.7"), DEFAULT);
        supportedEngines.put(engineFrom("mysql", "5.x"), MYSQL);
        supportedEngines.put(engineFrom("oracle", "11g/12c"), DEFAULT);
        supportedEngines.put(engineFrom("sqlite", "3.x"), SQLITE);
        supportedEngines.put(engineFrom("sql_server", "2012/2014/2016"), DEFAULT);
        supportedEngines.putAll(enginesFrom("postgresql", "9.x", "11", "13").apply(POSTGRES));
    }

    public static SQLDialect asSQLDialect(DatabaseEngine dbEngine) {
        validateEngine(dbEngine);

        if (isSupportedEngine(dbEngine)) {
            return convertSupportedEngine(dbEngine);
        }

        return convertToDefaultEngine(dbEngine);
    }

    private static SQLDialect convertToDefaultEngine(DatabaseEngine dbEngine) {
        log.info("SQLDialect mapping",
                 "Database engine " + dbEngine.getName() + ":" + dbEngine.getVersion() + " converted to DEFAULT " +
                         "sqlDialect. Engine type not recognized as supported database.");
        return DEFAULT;
    }

    private static SQLDialect convertSupportedEngine(DatabaseEngine dbEngine) {
        SQLDialect sqlDialect = supportedEngines.get(dbEngine);
        if (sqlDialect == DEFAULT) {
            log.info("SQLDialect mapping",
                     "Database engine " + dbEngine.getName() + ":" + dbEngine.getVersion() + " converted to DEFAULT " +
                             "sqlDialect. Database supported by vertabelo but not available in open source jooq license");
        }
        return sqlDialect;
    }

    private static boolean isSupportedEngine(DatabaseEngine dbEngine) {
        return supportedEngines.containsKey(dbEngine);
    }

    private static void validateEngine(DatabaseEngine dbEngine) {
        if (dbEngine == null) {
            throw new NullDatabaseEngineDefinitionException();
        }
    }

    private static DatabaseEngine engineFrom(String name, String version) {
        return new DatabaseEngine()
                .withName(name)
                .withVersion(version);
    }

    private static Function<SQLDialect, Map<DatabaseEngine, SQLDialect>> enginesFrom(String name, String... versions) {
        return dialect -> Arrays.stream(versions)
                                .map(version -> engineFrom(name, version))
                                .collect(Collectors.toMap(Function.identity(), ignore -> dialect));
    }
}
