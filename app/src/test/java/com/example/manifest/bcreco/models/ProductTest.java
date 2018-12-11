package com.example.manifest.bcreco.models;

import com.example.manifest.bcreco.models.Product;
import com.example.manifest.bcreco.models.StoreStock;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProductTest {
    private Product product = new Product("model", "color", "modelDesc", "season", 10000);

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
}