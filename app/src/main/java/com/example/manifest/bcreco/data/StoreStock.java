package com.example.manifest.bcreco.data;

import java.util.HashMap;
import java.util.Map;

public class StoreStock {

    private String storeName;

    private Map<Integer, Integer> sizesCount = new HashMap<>();

    public StoreStock(String name) {
        storeName = name;
    }
    public void addSizeCount(int size, int count) {
        sizesCount.put(size, count);
    }
}
