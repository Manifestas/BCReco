package com.example.manifest.bcreco.models;

import java.util.List;

public class InfoFromSite {

    private String maxPrice;
    private List<String> imageUrls;

    public InfoFromSite(String maxPrice, List<String> imageUrls) {
        this.maxPrice = maxPrice;
        this.imageUrls = imageUrls;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
