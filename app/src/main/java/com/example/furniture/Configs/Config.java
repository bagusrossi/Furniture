package com.example.furniture.Configs;

public class Config {

    //Deklarasi variable untuk server_url yang diperlukan
    public static final String SERVER_URL = "http://furniture.awvcake.com/api.php?";
    public static final String CONTENT_URL = "http://furniture.awvcake.com/uploads/";
//    public static final String SERVER_URL = "http://192.168.0.12:8080/furniture/api/api.php?";
//    public static final String CONTENT_URL = "http://192.168.0.12:8080/furniture/api/uploads/";
    public static final String CATEGORY_IMAGE_URL = CONTENT_URL + "categories/";
    public static final String PRODUCT_IMAGE_URL = CONTENT_URL + "products/";
    public static final String PAYMENT_IMAGE_URL = CONTENT_URL + "payments/";
    public static final String COMPLAINT_IMAGE_URL = CONTENT_URL + "complaints/";
}
