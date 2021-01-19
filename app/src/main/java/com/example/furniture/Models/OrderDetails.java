package com.example.furniture.Models;

public class OrderDetails {
    private String order_code;
    private String product_name;
    private String product_code;
    private int product_price;
    private int qty;
    private String fotobayar;

    public String getFotobayar() {
        return fotobayar;
    }

    public void setFotobayar(String fotobayar) {
        this.fotobayar = fotobayar;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
