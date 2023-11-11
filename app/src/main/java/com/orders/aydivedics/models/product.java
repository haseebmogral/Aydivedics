package com.orders.aydivedics.models;

public class product {

    public String prod_id;
    public String prod_name;
    public float prod_price;
    public float prod_weight;
    public int item_count;
    public float product_total = 0;


    public product(String prod_id, String prod_name, float prod_price, float prod_weight, int item_count) {
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.prod_price = prod_price;
        this.prod_weight = prod_weight;
        this.item_count = item_count;
    }

    public float getProd_weight() {
        return prod_weight;
    }

    public void setProd_weight(float prod_weight) {
        this.prod_weight = prod_weight;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public float getProd_price() {
        return prod_price;
    }

    public void setProd_price(float prod_price) {
        this.prod_price = prod_price;
    }
    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

}
