package com.example.manifest.bcreco.models;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private String model;
    private String color;
    private String modelDesc;
    private String season;
    private int price;
    private List<StoreStock> stores = new ArrayList<>();

    public Product(String model, String color, String modelDesc, String season, int price) {
        this.model = model;
        this.color = color;
        this.modelDesc = modelDesc;
        this.season = season;
        this.price = price;
    }

    /**
     * Removes leading zeros and last character(check sum number) from the string.
     *
     * @param barcode String from which need to get PLU.
     * @return String PLU.
     */
    public static String getPluFromBarcode(String barcode) {
        if (barcode == null || barcode.isEmpty()) {
            return null;
        }
        // ^ anchors to the start of the string. The 0* means zero or more 0 characters.
        barcode = barcode.replaceFirst("^0*", "");
        if (barcode.isEmpty()) { // if barcode was "00000001"
            return null;
        }
        return barcode.substring(0, barcode.length() - 1);
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

    public void addStoreStockInfo(String storeName, int size, int count) {
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
}
