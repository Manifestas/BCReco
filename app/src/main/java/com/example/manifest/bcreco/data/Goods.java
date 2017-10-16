package com.example.manifest.bcreco.data;

public class Goods {


    private String barcode;
    private String model;
    private String color;
    private String modelDesc;

    public Goods(String barcode, String model, String color, String modelDesc) {
        this.barcode = barcode;
        this.model = model;
        this.color = color;
        this.modelDesc = modelDesc;
    }

    public String getBarcode() {
        return barcode;
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
}
