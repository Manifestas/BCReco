package com.example.manifest.bcreco.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class BarcodeUtilsTest {

    @Test
    public void isValidEAN13ValidBarcode() {
        String barcode = "4600051000057";
        assertTrue(BarcodeUtils.isValidEAN13(barcode));
    }

    @Test
    public void isValidEAN13NotValidBarcode() {
        String barcode = "4601546021299";
        assertFalse(BarcodeUtils.isValidEAN13(barcode));
    }
}