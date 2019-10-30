package com.elasticsearch.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CollectionUtil {

    public static <T> List<T> iterableToCollectionGuava(Iterable<T> iterable){
        return Lists.newArrayList(iterable);
    }

    public static <T> List<T> iterableToCollectionJava8(Iterable<T> iterable){
        if (iterable instanceof List){
            return (List<T>)iterable;
        }
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }
}
