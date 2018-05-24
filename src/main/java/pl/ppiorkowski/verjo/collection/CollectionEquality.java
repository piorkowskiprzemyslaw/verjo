package pl.ppiorkowski.verjo.collection;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionEquality {

    public static <T> boolean equals(Collection<T> c1, Collection<T> c2) {
        if (c1 == null || c2 == null || c1.size() != c2.size()) {
            return false;
        }

        return c1.containsAll(c2);
    }

}
