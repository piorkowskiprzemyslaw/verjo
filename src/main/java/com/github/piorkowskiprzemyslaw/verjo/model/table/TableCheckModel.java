package com.github.piorkowskiprzemyslaw.verjo.model.table;

import lombok.Value;

@Value
public class TableCheckModel {
    private String name;
    private String checkExpression;
}
