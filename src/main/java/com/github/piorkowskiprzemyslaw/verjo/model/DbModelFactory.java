package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.model.provider.DatabaseModelProvider;
import com.github.piorkowskiprzemyslaw.verjo.xsd.DatabaseModel;
import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class DbModelFactory {

    public static DbModel build(String xmlFilePath) {
        DatabaseModel model = new DatabaseModelProvider(xmlFilePath).getModel();
        return new DbModel(model);
    }

}
