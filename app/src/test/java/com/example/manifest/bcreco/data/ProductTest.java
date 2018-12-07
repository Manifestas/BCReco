package com.example.manifest.bcreco.data;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProductTest {

    @Test
    public void getPluFromBarcodeSampleBarcode() {
        String expected = "468777";
        assertEquals(expected, Product.getPluFromBarcode("000004687771"));
    }

    @Test
    public void getPluFromBarcodeWithNoZerosBarcode() {
        String expected = "10000468777";
        assertEquals(expected, Product.getPluFromBarcode("100004687771"));
    }

    @Test
    public void getPluFromBarcodeZerosBarcode() {
        assertNull(Product.getPluFromBarcode("000000000000"));
    }

    @Test
    public void getPluFromBarcodeNullBarcode() {
        assertNull(Product.getPluFromBarcode(null));
    }

    @Test
    public void addStoreStockInfoAddToEmpty() {
        Product product = new Product("model", "color", "modelDesc", "season", 10000);
        product.addStoreStockInfo("Unimoll", 39, 1);
        List<StoreStock> stores = product.getStores();
        StoreStock store = new StoreStock("Unimoll");
        store.addSizeCount(39, 1);
        assertEquals(stores.get(0), store);
    }

    @Test
    public void addStoreStockInfoAddSameStoreAnotherSize() {
        Product product = new Product("model", "color", "modelDesc", "season", 10000);
        product.addStoreStockInfo("Unimoll", 39, 1);
        product.addStoreStockInfo("Unimoll", 40, 1);
        List<StoreStock> stores = product.getStores();
        StoreStock store = new StoreStock("Unimoll");
        store.addSizeCount(39, 1);
        store.addSizeCount(40, 1);
        assertEquals(stores.get(0), store);
    }

    @Test
    public void addStoreStockInfoAddAnotherStore() {
        Product product = new Product("model", "color", "modelDesc", "season", 10000);
        product.addStoreStockInfo("Unimoll", 39, 1);
        product.addStoreStockInfo("Inet", 39, 1);
        List<StoreStock> stores = product.getStores();
        StoreStock store = new StoreStock("Inet");
        store.addSizeCount(39, 1);
        assertEquals(stores.get(1), store);
    }
}