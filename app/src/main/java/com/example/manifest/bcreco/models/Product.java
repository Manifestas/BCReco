package com.example.manifest.bcreco.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Product {

    private String model;
    private String color;
    private String modelDesc;
    private String season;
    private int price;
    private String currentStoreName;
    private List<StoreStock> stores = new ArrayList<>();

    public Product(String model, String color, String modelDesc, String season, int price) {
        this.model = model;
        this.color = color;
        this.modelDesc = modelDesc;
        this.season = season;
        this.price = price;
    }

    public List<StoreStock> getStores() {
        return stores;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getModelDesc() {
        return modelDesc;
    }

    public String getSeason() {
        return season;
    }

    public int getPrice() {
        return price;
    }

    public String getCurrentStoreName() {
        return currentStoreName;
    }

    public void setCurrentStoreName(String currentStoreName) {
        this.currentStoreName = currentStoreName;
    }

    public void addStoreStockInfo(String storeName, String size, int count) {
        for (StoreStock store : stores) {
            if (store.getStoreName().equals(storeName)) {
                store.addSizeCount(size, count);
                return;
            }
        }
        StoreStock storeStock = new StoreStock(storeName);
        storeStock.addSizeCount(size, count);
        stores.add(storeStock);
    }

    /**
     * Returns all available sizes in all stores. If size as key in Map is available in current
     * store value in Map will be true.
     * @return Map with Size as key and boolean availability in current store.
     */
    public Map<String, Boolean> getAvailableSizes() {
        Map<String, Boolean> allAvailableSizes = new TreeMap<>();
        for (StoreStock storeStock : stores) {
            Set<String> oneStoreSizes = storeStock.getAllSizes();
            for (String size : oneStoreSizes) {
                boolean isCurrentStore = storeStock.getStoreName().equals(currentStoreName);
                if (!allAvailableSizes.containsKey(size)) {
                    if (isCurrentStore) {
                        allAvailableSizes.put(size, true);
                    } else {
                        allAvailableSizes.put(size, false);
                    }

                } else {
                    if (!allAvailableSizes.get(size) && isCurrentStore) {
                        allAvailableSizes.put(size, true);
                    }
                }
            }
        }
        return allAvailableSizes;
    }

    /**
     * Returns the amount of this size in each store.
     * @param size which quantity will be returned.
     * @return Map with storeName as key and size quantity as value.
     */
    public Map<String, Integer> getSizeQuantityForAllStores(String size) {
        Map<String, Integer> storesSizeQuantity = new TreeMap<>();
        for (StoreStock storeStock : stores) {
            int sizeQuantity = storeStock.getSizeQuantity(size);
            if (sizeQuantity != 0) {
                storesSizeQuantity.put(storeStock.getStoreName(), sizeQuantity);
            }
        }
        return storesSizeQuantity;
    }
}
