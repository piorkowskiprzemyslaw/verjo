package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.collection.CollectionEquality;
import com.github.piorkowskiprzemyslaw.verjo.model.table.AlternateKeyModel;
import com.github.piorkowskiprzemyslaw.verjo.model.table.PrimaryKeyModel;
import com.github.piorkowskiprzemyslaw.verjo.xsd.Column;
import com.github.piorkowskiprzemyslaw.verjo.xsd.Reference;
import com.github.piorkowskiprzemyslaw.verjo.xsd.ReferenceColumn;
import com.github.piorkowskiprzemyslaw.verjo.xsd.Table;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReferenceModel {

    private final Reference reference;

    public static ReferenceModel of(Reference reference) {
        return new ReferenceModel(reference);
    }

    public TableModel getFKTable() {
        return TableModel.of((Table) reference.getFKTable());
    }

    public TableModel getPKTable() {
        return TableModel.of((Table) reference.getPKTable());
    }

    boolean isReferenceInSchema(String defaultSchema, Collection<String> allowedSchemas) {
        String pkSchemaName = getPKTable().getSchema(defaultSchema);
        String fkSchemaName = getFKTable().getSchema(defaultSchema);
        return allowedSchemas.containsAll(Arrays.asList(pkSchemaName, fkSchemaName));
    }

    public String getName() {
        return reference.getName();
    }

    private List<Column> getPKColumns() {
        return referenceColumnsStream()
                .map(rc -> (Column) rc.getPKColumn())
                .collect(Collectors.toList());
    }

    List<Column> getFKTableReferenceColumns() {
        return referenceColumnsStream()
                .map(rc -> (Column) rc.getFKColumn())
                .collect(Collectors.toList());
    }

    private Stream<ReferenceColumn> referenceColumnsStream() {
        return reference.getReferenceColumns().getReferenceColumn().stream();
    }

    Optional<String> getUniqueKeyName() {
        Optional<String> ukNameFromPrimaryKey = getUKNameFromPrimaryKey();
        if (ukNameFromPrimaryKey.isPresent()) {
            return ukNameFromPrimaryKey;
        }
        return getUKNameFromAlternateKeys();
    }

    private Optional<String> getUKNameFromPrimaryKey() {
        List<Column> pkColumns = getPKColumns();
        PrimaryKeyModel tablePK = getPKTable().getPrimaryKey();
        if (CollectionEquality.equals(pkColumns, tablePK.getColumns())) {
            return Optional.ofNullable(tablePK.getName());
        }
        return Optional.empty();
    }

    private Optional<String> getUKNameFromAlternateKeys() {
        return getPKTable().getAlternateKeys().stream()
                .map(this::getUKNameFromAlternateKey)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    private Optional<String> getUKNameFromAlternateKey(AlternateKeyModel alternateKey) {
        List<Column> pkColumns = getPKColumns();
        if (CollectionEquality.equals(pkColumns, alternateKey.getColumns())) {
            return Optional.ofNullable(alternateKey.getName());
        }
        return Optional.empty();
    }
}
