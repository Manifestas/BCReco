package com.example.manifest.bcreco.utils;

public class BarcodeUtils {

    public static boolean isValidEAN13(String barcode) {
        int length = barcode.length();
        if (length != 13) {
            return false;
        }
        int odds = 0;
        int evens = 0;
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                evens += Integer.parseInt(barcode.charAt(i) + "");
            } else {
                odds += Integer.parseInt(barcode.charAt(i) + "");
            }
        }
        odds *= 3;
        return (evens + odds) % 10 == 0;
    }
}


