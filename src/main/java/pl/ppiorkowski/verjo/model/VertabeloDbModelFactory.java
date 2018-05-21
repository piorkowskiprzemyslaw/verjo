package pl.ppiorkowski.verjo.model;

import lombok.AllArgsConstructor;
import pl.ppiorkowski.verjo.model.provider.DatabaseModelProvider;
import pl.ppiorkowski.verjo.xsd.DatabaseModel;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class VertabeloDbModelFactory {

    public static VertabeloDbModel build(String xmlFilePath) {
        DatabaseModel model = new DatabaseModelProvider(xmlFilePath).getModel();
        return new VertabeloDbModel(model);
    }

}
