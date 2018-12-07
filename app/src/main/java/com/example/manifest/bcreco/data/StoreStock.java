package com.example.manifest.bcreco.data;

import java.util.HashMap;
import java.util.Map;

public class StoreStock {

    private String storeName;

    private Map<String, String> sizesCount = new HashMap<>();

    public StoreStock(String name) {
        storeName = name;
    }
    public void addSizeCount(String size, String count) {
        sizesCount.put(size, count);
    }
}
