package pl.ppiorkowski.verjo;

import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.tools.JooqLogger;
import org.jooq.util.*;
import org.jooq.util.Database;
import org.jooq.util.jaxb.*;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class XMLDb implements Database {

    private static final JooqLogger log = JooqLogger.getLogger(XMLDb.class);

    @Getter
    private List<CatalogDefinition> catalogs;

    @Getter
    private List<SchemaDefinition> schemata;

    @Override
    public CatalogDefinition getCatalog(String name) {
        return catalogs.stream()
                .filter(c -> Objects.equals(c.getName(), name))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<SchemaDefinition> getSchemata(CatalogDefinition catalog) {
        return null;
    }

    @Override
    public SchemaDefinition getSchema(String name) {
        return null;
    }

    @Override
    public Relations getRelations() {
        return null;
    }

    @Override
    public List<SequenceDefinition> getSequences(SchemaDefinition schema) {
        return null;
    }

    @Override
    public List<IdentityDefinition> getIdentities(SchemaDefinition schema) {
        return null;
    }

    @Override
    public List<IndexDefinition> getIndexes(SchemaDefinition schema) {
        return null;
    }

    @Override
    public List<IndexDefinition> getIndexes(TableDefinition schema) {
        return null;
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema) {
        return null;
    }

    @Override
    public List<ForeignKeyDefinition> getForeignKeys(SchemaDefinition schema) {
        return null;
    }

    @Override
    public List<CheckConstraintDefinition> getCheckConstraints(SchemaDefinition schema) {
        return null;
    }

    @Override
    public List<TableDefinition> getTables(SchemaDefinition schema) {
        return null;
    }

    @Override
    public TableDefinition getTable(SchemaDefinition schema, String name) {
        return null;
    }

    @Override
    public TableDefinition getTable(SchemaDefinition schema, String name, boolean ignoreCase) {
        return null;
    }

    @Override
    public TableDefinition getTable(SchemaDefinition schema, Name name) {
        return null;
    }

    @Override
    public TableDefinition getTable(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return null;
    }

    @Override
    public List<EnumDefinition> getEnums(SchemaDefinition schema) {
        return null;
    }

    @Override
    public EnumDefinition getEnum(SchemaDefinition schema, String name) {
        return null;
    }

    @Override
    public EnumDefinition getEnum(SchemaDefinition schema, String name, boolean ignoreCase) {
        return null;
    }

    @Override
    public EnumDefinition getEnum(SchemaDefinition schema, Name name) {
        return null;
    }

    @Override
    public EnumDefinition getEnum(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return null;
    }

    @Override
    public List<DomainDefinition> getDomains(SchemaDefinition schema) {
        return null;
    }

    @Override
    public DomainDefinition getDomain(SchemaDefinition schema, String name) {
        return null;
    }

    @Override
    public DomainDefinition getDomain(SchemaDefinition schema, String name, boolean ignoreCase) {
        return null;
    }

    @Override
    public DomainDefinition getDomain(SchemaDefinition schema, Name name) {
        return null;
    }

    @Override
    public DomainDefinition getDomain(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return null;
    }

    @Override
    public List<UDTDefinition> getUDTs(SchemaDefinition schema) {
        return null;
    }

    @Override
    public UDTDefinition getUDT(SchemaDefinition schema, String name) {
        return null;
    }

    @Override
    public UDTDefinition getUDT(SchemaDefinition schema, String name, boolean ignoreCase) {
        return null;
    }

    @Override
    public UDTDefinition getUDT(SchemaDefinition schema, Name name) {
        return null;
    }

    @Override
    public UDTDefinition getUDT(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return null;
    }

    @Override
    public List<UDTDefinition> getUDTs(PackageDefinition pkg) {
        return null;
    }

    @Override
    public List<ArrayDefinition> getArrays(SchemaDefinition schema) {
        return null;
    }

    @Override
    public ArrayDefinition getArray(SchemaDefinition schema, String name) {
        return null;
    }

    @Override
    public ArrayDefinition getArray(SchemaDefinition schema, String name, boolean ignoreCase) {
        return null;
    }

    @Override
    public ArrayDefinition getArray(SchemaDefinition schema, Name name) {
        return null;
    }

    @Override
    public ArrayDefinition getArray(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return null;
    }

    @Override
    public List<RoutineDefinition> getRoutines(SchemaDefinition schema) {
        return null;
    }

    @Override
    public List<PackageDefinition> getPackages(SchemaDefinition schema) {
        return null;
    }

    @Override
    public PackageDefinition getPackage(SchemaDefinition schema, String inputName) {
        return null;
    }

    @Override
    public void setConnection(Connection connection) {

    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public List<String> getInputCatalogs() {
        return null;
    }

    @Override
    public List<String> getInputSchemata() {
        return null;
    }

    @Override
    public List<String> getInputSchemata(CatalogDefinition catalog) {
        return null;
    }

    @Override
    public List<String> getInputSchemata(String catalog) {
        return null;
    }

    @Override
    public String getOutputCatalog(String inputCatalog) {
        return null;
    }

    @Override
    public String getOutputSchema(String inputSchema) {
        return null;
    }

    @Override
    public String getOutputSchema(String inputCatalog, String inputSchema) {
        return null;
    }

    @Override
    public void setConfiguredCatalogs(List<Catalog> catalogs) {

    }

    @Override
    public void setConfiguredSchemata(List<Schema> schemata) {

    }

    @Override
    public void setExcludes(String[] excludes) {

    }

    @Override
    public String[] getExcludes() {
        return new String[0];
    }

    @Override
    public void setIncludes(String[] includes) {

    }

    @Override
    public String[] getIncludes() {
        return new String[0];
    }

    @Override
    public void setIncludeExcludeColumns(boolean includeExcludeColumns) {

    }

    @Override
    public boolean getIncludeExcludeColumns() {
        return false;
    }

    @Override
    public void setIncludeForeignKeys(boolean includeForeignKeys) {

    }

    @Override
    public boolean getIncludeForeignKeys() {
        return false;
    }

    @Override
    public void setIncludeUniqueKeys(boolean includeUniqueKeys) {

    }

    @Override
    public boolean getIncludeUniqueKeys() {
        return false;
    }

    @Override
    public void setIncludePrimaryKeys(boolean includePrimaryKeys) {

    }

    @Override
    public boolean getIncludePrimaryKeys() {
        return false;
    }

    @Override
    public void setIncludeIndexes(boolean includeIndexes) {

    }

    @Override
    public boolean getIncludeIndexes() {
        return false;
    }

    @Override
    public void setIncludeSequences(boolean includeSequences) {

    }

    @Override
    public boolean getIncludeSequences() {
        return false;
    }

    @Override
    public void setIncludeUDTs(boolean includeUDTs) {

    }

    @Override
    public boolean getIncludeUDTs() {
        return false;
    }

    @Override
    public void setIncludePackages(boolean includePackages) {

    }

    @Override
    public boolean getIncludePackages() {
        return false;
    }

    @Override
    public void setIncludeRoutines(boolean includeRoutines) {

    }

    @Override
    public boolean getIncludeRoutines() {
        return false;
    }

    @Override
    public void setIncludeTables(boolean includeTables) {

    }

    @Override
    public boolean getIncludeTables() {
        return false;
    }

    @Override
    public void addFilter(Filter filter) {

    }

    @Override
    public List<Filter> getFilters() {
        return null;
    }

    @Override
    public <D extends Definition> List<D> filterExcludeInclude(List<D> definitions) {
        return null;
    }

    @Override
    public <D extends Definition> List<D> sort(List<D> definitions) {
        return null;
    }

    @Override
    public List<Definition> getIncluded() {
        return null;
    }

    @Override
    public List<Definition> getExcluded() {
        return null;
    }

    @Override
    public List<Definition> getAll() {
        return null;
    }

    @Override
    public void setRegexFlags(List<RegexFlag> regexFlags) {

    }

    @Override
    public List<RegexFlag> getRegexFlags() {
        return null;
    }

    @Override
    public void setRecordVersionFields(String[] recordVersionFields) {

    }

    @Override
    public String[] getRecordVersionFields() {
        return new String[0];
    }

    @Override
    public void setRecordTimestampFields(String[] recordTimestampFields) {

    }

    @Override
    public String[] getRecordTimestampFields() {
        return new String[0];
    }

    @Override
    public void setSyntheticPrimaryKeys(String[] primaryKeys) {

    }

    @Override
    public String[] getSyntheticPrimaryKeys() {
        return new String[0];
    }

    @Override
    public void setOverridePrimaryKeys(String[] primaryKeys) {

    }

    @Override
    public String[] getOverridePrimaryKeys() {
        return new String[0];
    }

    @Override
    public void setSyntheticIdentities(String[] syntheticIdentities) {

    }

    @Override
    public String[] getSyntheticIdentities() {
        return new String[0];
    }

    @Override
    public void setConfiguredCustomTypes(List<CustomType> types) {

    }

    @Override
    public List<CustomType> getConfiguredCustomTypes() {
        return null;
    }

    @Override
    public CustomType getConfiguredCustomType(String name) {
        return null;
    }

    @Override
    public void setConfiguredEnumTypes(List<EnumType> types) {

    }

    @Override
    public List<EnumType> getConfiguredEnumTypes() {
        return null;
    }

    @Override
    public void setConfiguredForcedTypes(List<ForcedType> types) {

    }

    @Override
    public SchemaVersionProvider getSchemaVersionProvider() {
        return null;
    }

    @Override
    public void setSchemaVersionProvider(SchemaVersionProvider provider) {

    }

    @Override
    public CatalogVersionProvider getCatalogVersionProvider() {
        return null;
    }

    @Override
    public void setCatalogVersionProvider(CatalogVersionProvider provider) {

    }

    @Override
    public Comparator<Definition> getOrderProvider() {
        return null;
    }

    @Override
    public void setOrderProvider(Comparator<Definition> provider) {

    }

    @Override
    public List<ForcedType> getConfiguredForcedTypes() {
        return null;
    }

    @Override
    public ForcedType getConfiguredForcedType(Definition definition) {
        return null;
    }

    @Override
    public ForcedType getConfiguredForcedType(Definition definition, DataTypeDefinition definedType) {
        return null;
    }

    @Override
    public SQLDialect getDialect() {
        return null;
    }

    @Override
    public DSLContext create() {
        return null;
    }

    @Override
    public boolean isArrayType(String dataType) {
        return false;
    }

    @Override
    public void setSupportsUnsignedTypes(boolean supportsUnsignedTypes) {

    }

    @Override
    public boolean supportsUnsignedTypes() {
        return false;
    }

    @Override
    public void setIgnoreProcedureReturnValues(boolean ignoreProcedureReturnValues) {

    }

    @Override
    public boolean ignoreProcedureReturnValues() {
        return false;
    }

    @Override
    public void setDateAsTimestamp(boolean dateAsTimestamp) {

    }

    @Override
    public boolean dateAsTimestamp() {
        return false;
    }

    @Override
    public void setIncludeRelations(boolean includeRelations) {

    }

    @Override
    public boolean includeRelations() {
        return false;
    }

    @Override
    public void setTableValuedFunctions(boolean tableValuedFunctions) {

    }

    @Override
    public boolean tableValuedFunctions() {
        return false;
    }

    @Override
    public boolean exists(Table<?> table) {
        return false;
    }

    @Override
    public boolean existAll(Table<?>... tables) {
        return false;
    }

    @Override
    public void setProperties(Properties properties) {
        log.info("Setting properties in XMLDb");
    }

    @Override
    public Properties getProperties() {
        return null;
    }
}
