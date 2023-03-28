package com.github.piorkowskiprzemyslaw.verjo.model.table;

import com.github.piorkowskiprzemyslaw.verjo.xsd.Column;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class AlternateKeyModel {
    String name;
    @Singular
    List<Column> columns;

    public List<String> getColumnNames() {
        return getColumns().stream()
                .map(Column::getName)
                .collect(Collectors.toList());
    }
}
