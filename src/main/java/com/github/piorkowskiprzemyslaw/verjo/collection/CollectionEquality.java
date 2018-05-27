package com.github.piorkowskiprzemyslaw.verjo.collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionEquality {

    public static <T> boolean equals(Collection<T> c1, Collection<T> c2) {
        if (c1 == null || c2 == null || c1.size() != c2.size()) {
            return false;
        }

        return c1.containsAll(c2);
    }

}
