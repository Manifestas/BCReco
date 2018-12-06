package com.example.manifest.bcreco.data;

public class Goods {

    private String model;
    private String color;
    private String modelDesc;
    private String season;
    private int price;
    private String size;

    public Goods(String model, String color, String modelDesc, String season,
                 int price, String size) {
        this.model = model;
        this.color = color;
        this.modelDesc = modelDesc;
        this.season = season;
        this.price = price;
        this.size = size;
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

    public String getSize() {
        return size;
    }

    /**
     * Removes leading zeros and last character(check sum number) from the string.
     * @param barcode String from which need to get PLU.
     * @return String PLU.
     */
    public static String getPluFromBarcode(String barcode) {
        // ^ anchors to the start of the string. The 0* means zero or more 0 characters.
        barcode = barcode.replaceFirst("^0*", "");
        return barcode.substring(0, barcode.length() - 1);
    }
}
