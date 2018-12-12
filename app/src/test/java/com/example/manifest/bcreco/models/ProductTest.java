package com.example.manifest.bcreco.models;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class ProductTest {
    private Product product = new Product("model", "color", "modelDesc", "season", 10000);

    @Test
    public void addStoreStockInfoAddToEmpty() {
        product.addStoreStockInfo("Unimoll", "39", 1);
        List<StoreStock> stores = product.getStores();
        StoreStock store = new StoreStock("Unimoll");
        store.addSizeCount("39", 1);
        assertEquals(stores.get(0), store);
    }

    @Test
    public void addStoreStockInfoAddSameStoreAnotherSize() {
        product.addStoreStockInfo("Unimoll", "39", 1);
        product.addStoreStockInfo("Unimoll", "40", 1);
        List<StoreStock> stores = product.getStores();
        StoreStock store = new StoreStock("Unimoll");
        store.addSizeCount("39", 1);
        store.addSizeCount("40", 1);
        assertEquals(stores.get(0), store);
    }

    @Test
    public void addStoreStockInfoAddAnotherStore() {
        product.addStoreStockInfo("Unimoll", "39", 1);
        product.addStoreStockInfo("Inet", "39", 1);
        List<StoreStock> stores = product.getStores();
        StoreStock store = new StoreStock("Inet");
        store.addSizeCount("39", 1);
        assertEquals(stores.get(1), store);
    }

    @Test
    public void getAvailableSizesAllSizesInCurrentStore() {
        product.addStoreStockInfo("Unimoll", "39", 1);
        product.addStoreStockInfo("Inet", "39", 1);
        product.addStoreStockInfo("Unimoll", "40", 1);
        product.addStoreStockInfo("Inet", "40", 1);
        product.setCurrentStoreName("Unimoll");
        Map<String, Boolean> expected = new TreeMap<>();
        expected.put("39", true);
        expected.put("40", true);

        assertEquals(expected, product.getAvailableSizes());
    }

    @Test
    public void getAvailableSizesAllSizesNotInCurrentStore() {
        product.addStoreStockInfo("Riga", "39", 1);
        product.addStoreStockInfo("Inet", "39", 1);
        product.addStoreStockInfo("Riga", "40", 1);
        product.addStoreStockInfo("Inet", "40", 1);
        product.setCurrentStoreName("Unimoll");
        Map<String, Boolean> expected = new TreeMap<>();
        expected.put("39", false);
        expected.put("40", false);

        assertEquals(expected, product.getAvailableSizes());
    }

    @Test
    public void getAvailableSizesSizesByRotation() {
        product.addStoreStockInfo("Riga", "38", 1);
        product.addStoreStockInfo("Unimoll", "39", 1);
        product.addStoreStockInfo("Riga", "40", 1);
        product.addStoreStockInfo("Unimoll", "41", 1);
        product.setCurrentStoreName("Unimoll");
        Map<String, Boolean> expected = new TreeMap<>();
        expected.put("38", false);
        expected.put("39", true);
        expected.put("40", false);
        expected.put("41", true);

        assertEquals(expected, product.getAvailableSizes());
    }

    @Test
    public void getAvailableSizesCurrentStoreIsNull() {
        product.addStoreStockInfo("Riga", "39", 1);
        product.addStoreStockInfo("Inet", "39", 1);
        product.addStoreStockInfo("Riga", "40", 1);
        product.addStoreStockInfo("Inet", "40", 1);
        product.setCurrentStoreName(null);
        Map<String, Boolean> expected = new TreeMap<>();
        expected.put("39", false);
        expected.put("40", false);

        assertEquals(expected, product.getAvailableSizes());
    }

    @Test
    public void getSizeQuantityForAllStoresWith1Quantity() {
        product.addStoreStockInfo("Riga", "39", 1);
        product.addStoreStockInfo("Inet", "39", 1);
        product.addStoreStockInfo("Riga", "40", 1);
        product.addStoreStockInfo("Inet", "40", 1);
        product.setCurrentStoreName("Unimoll");
        Map<String, Integer> expected = new TreeMap<>();
        expected.put("Inet", 1);
        expected.put("Riga", 1);
        assertEquals(expected, product.getSizeQuantityForAllStores("39"));
    }

    @Test
    public void getSizeQuantityForAllStoresWith1and2Quantity() {
        product.addStoreStockInfo("Riga", "39", 1);
        product.addStoreStockInfo("Inet", "39", 1);
        product.addStoreStockInfo("Riga", "40", 1);
        product.addStoreStockInfo("Inet", "40", 2);
        product.setCurrentStoreName("Unimoll");
        Map<String, Integer> expected = new TreeMap<>();
        expected.put("Inet", 2);
        expected.put("Riga", 1);
        assertEquals(expected, product.getSizeQuantityForAllStores("40"));
    }

    @Test
    public void getSizeQuantityForAllStoresWith0Quantity() {
        product.addStoreStockInfo("Riga", "39", 1);
        product.addStoreStockInfo("Inet", "39", 1);
        product.addStoreStockInfo("Riga", "40", 1);
        product.addStoreStockInfo("Inet", "40", 2);
        product.setCurrentStoreName("Unimoll");
        Map<String, Integer> expected = new TreeMap<>();
        assertEquals(expected, product.getSizeQuantityForAllStores("41"));
    }
}