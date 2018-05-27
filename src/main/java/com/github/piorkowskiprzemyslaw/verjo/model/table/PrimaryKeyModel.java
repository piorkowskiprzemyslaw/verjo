package com.github.piorkowskiprzemyslaw.verjo.model.table;

import com.github.piorkowskiprzemyslaw.verjo.xsd.Column;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class PrimaryKeyModel {
    private String name;
    @Singular
    private List<Column> columns;

    public List<String> getColumnNames() {
        return columns.stream()
                .map(Column::getName)
                .collect(Collectors.toList());
    }
}
