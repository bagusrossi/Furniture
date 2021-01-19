package com.example.furniture.Models;

public class Categories {
    private int id;
    private String category_name;
    private String category_icon;
    private String category_code;
    private int category_index;

    public int getCategory_index() {
        return category_index;
    }

    public void setCategory_index(int category_index) {
        this.category_index = category_index;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }
}
