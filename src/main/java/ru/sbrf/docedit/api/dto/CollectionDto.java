package ru.sbrf.docedit.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
public class CollectionDto<T> {
    @JsonProperty("items")
    private Collection<T> collection;
    @JsonProperty("item_count")
    private int itemCount;

    public CollectionDto() {
    }

    public CollectionDto(Collection<T> collection, int itemCount) {
        this.collection = collection;
        this.itemCount = itemCount;
    }


    public static <T, R> CollectionDto<R> toDto(Collection<T> collection, Function<T, R> itemMapper) {
        return new CollectionDto<>(collection.stream().map(itemMapper).collect(Collectors.toList()), collection.size());
    }


    public Collection<T> getCollection() {
        return collection;
    }

    public void setCollection(Collection<T> collection) {
        this.collection = collection;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public String toString() {
        return "CollectionDto{" +
                "collection=" + collection +
                ", itemCount=" + itemCount +
                '}';
    }
}
