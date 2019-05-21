package com.example.androidnc.cuoikyandroid;

import com.google.firebase.database.PropertyName;

public class PhoneModel {
    private  int id;
    private  long price;
    private  String producer;
    private  String description;
    private  String product_name;

    public PhoneModel() {

    }

    public PhoneModel(int id, long price, String producer, String description, String product_name) {
        this.id = id;
        this.product_name = product_name;
        this.price = price;
        this.producer = producer;
        this.description = description;
    }
    @PropertyName("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @PropertyName("product_name")
    public String getName() {
        return product_name;
    }

    public void setName(String name) {
        this.product_name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
