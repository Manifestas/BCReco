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
}


