package com.example.manifest.bcreco.data;

import java.util.HashMap;
import java.util.Map;

public class StoreStock {

    private String storeName;

    private Map<String, Integer> sizesCount = new HashMap<>();

    public StoreStock(String name) {
        storeName = name;
    }
    public void addSizeCount(String size, int count) {
        sizesCount.put(size, count);
    }
}
