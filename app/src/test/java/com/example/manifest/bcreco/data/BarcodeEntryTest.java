package com.example.manifest.bcreco.data;

import org.junit.Test;

import static org.junit.Assert.*;
import com.example.manifest.bcreco.data.DbContract.BarcodeEntry;

public class BarcodeEntryTest {
    @Test
    public void getValidBarcodeLengthMoreThen12() throws Exception {
        String result = "000004512714";
        assertEquals(result, BarcodeEntry.getValidBarcode("0000004512714"));
    }

    @Test
    public void getValidBarcodeNull() throws Exception {
        assertNull(BarcodeEntry.getValidBarcode(null));
    }

    @Test
    public void getValidBarcodeLengthLessThen12() throws Exception {
        String result = "000004512714";
        assertEquals(result, BarcodeEntry.getValidBarcode("0004512714"));
    }

}