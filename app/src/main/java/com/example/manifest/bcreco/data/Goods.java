package com.example.manifest.bcreco.data;

public class Goods {

    private String model;
    private String color;
    private String modelDesc;
    private String season;
    private int price;
    private int size;

    public Goods(String model, String color, String modelDesc, String season,
                 int price, int size) {
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

    public int getSize() {
        return size;
    }
}
