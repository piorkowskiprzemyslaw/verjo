package pl.ppiorkowski.verjo.model.db_engine;

import org.jooq.SQLDialect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.ppiorkowski.verjo.xsd.DatabaseEngine;

import java.util.stream.Stream;

import static org.jooq.SQLDialect.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Database engine converter should")
class DbEngineConverterTest {

    @Test
    @DisplayName("throw exception when passed database engine is null")
    void shouldThrowExceptionOnNullEngine() {
        // when & then
        assertThrows(NullDatabaseEngineDefinitionException.class,
                () -> DbEngineConverter.asSQLDialect(null),
                "Provided database engine object is null!");
    }

    @ParameterizedTest(name = "{index} => Engine ''{0}:{1}'' should map to ''{2}''")
    @MethodSource("knownEnginesMappingProvider")
    @DisplayName("map known db engine to SQLDialect enum")
    void shouldMapKnownEngineToSQLDialect(String dbName, String dbVersion, SQLDialect expectedDialect) {
        // given
        DatabaseEngine engine = engineFrom(dbName, dbVersion);

        // when
        SQLDialect sqlDialect = DbEngineConverter.asSQLDialect(engine);

        // then
        assertEquals(sqlDialect, expectedDialect);
    }

    private static Stream<Arguments> knownEnginesMappingProvider() {
        return Stream.of(
                Arguments.of("hsqldb", "2.3.x", HSQLDB),
                Arguments.of("ibm_db2", "9.7", DEFAULT),
                Arguments.of("mysql", "5.x", MYSQL),
                Arguments.of("oracle", "11g/12c", DEFAULT),
                Arguments.of("postgresql", "9.x", POSTGRES_9_3),
                Arguments.of("sqlite", "3.x", SQLITE),
                Arguments.of("sql_server", "2012/2014/2016", DEFAULT)
        );
    }

    @Test
    @DisplayName("map unknown db engine to DEFAULT sql dialect")
    void shouldMapUnknownEngineToSQLDialect() {
        // given
        DatabaseEngine engine = engineFrom("unknown_engine", "unknown_version");

        // when
        SQLDialect sqlDialect = DbEngineConverter.asSQLDialect(engine);

        // then
        assertEquals(sqlDialect, DEFAULT);
    }

    private static DatabaseEngine engineFrom(String name, String version) {
        return new DatabaseEngine()
                .withName(name)
                .withVersion(version);
    }

}