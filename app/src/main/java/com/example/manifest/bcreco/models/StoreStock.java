package com.example.manifest.bcreco.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class StoreStock {

    private String storeName;

    private Map<String, Integer> sizesCount = new HashMap<>();

    public StoreStock(String name) {
        storeName = name;
    }

    public String getStoreName() {
        return storeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreStock that = (StoreStock) o;
        return Objects.equals(storeName, that.storeName) &&
                Objects.equals(sizesCount, that.sizesCount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(storeName, sizesCount);
    }

    public void addSizeCount(String size, int count) {
        sizesCount.put(size, count);
    }

    public Set<String> getAllSizes() {
        return sizesCount.keySet();
    }
}
