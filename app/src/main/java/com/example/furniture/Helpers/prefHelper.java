package com.example.furniture.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.furniture.Models.Products;

public class prefHelper {

    //Deklarasi variable
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;
    public Context context;

    /* INIT VAR */
    private static final String SHAREDPREFNAME = "shared_preferences";
    int PRIVATE_MODE = 0;

    /* USER VAR */
    private static final String ID = "id"; // unt
    private static final String LEVEL = "level"; //int
    private static final String USERNAME = "username"; // STRING
    private static final String EMAIL = "email"; // STRING
    private static final String PASSWORD = "password"; // STRING
    private static final String PHONE = "phone"; // STRING
    private static final String FULLNAME = "fullname";
    private static final String CREATED_DATE = "created_date";

    /*CART*/
    private static final String CART_INDEX = "cart_index";
    private static final String CART_KD_BARANG= "cart_kd_barang";
    private static final String CART_STOCK_BARANG = "cart_stock_barang";
    private static final String CART_HARGA_BARANG = "cart_harga_barang";
    private static final String CART_JUMLAH_BARANG = "cart_jumlah_barang";
    private static final String CART_PRICE_TOTAL = "cart_price_total";
    private static final String CART_MSG = "cart_msg";

    //Constructor untuk sharedPreferences
    public prefHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHAREDPREFNAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    //Fungsi init untuk akses dari luar
    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHAREDPREFNAME, Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    //Fungsi untuk melakukan set data ke dalam shared preferences
    public static String setUserdata(String key, String value) {
        String name = null;

        switch(key) {
            case "id": {
                name = ID;
                break;
            }
            case "username": {
                name = USERNAME;
                break;
            }
            case "fullname": {
                name = FULLNAME;
                break;
            }
            case "email": {
                name = EMAIL;
                break;
            }
            case "password": {
                name = PASSWORD;
                break;
            }
            case "phone": {
                name = PHONE;
                break;
            }
            case "created_date": {
                name = CREATED_DATE;
                break;
            }
            case "level": {
                name = LEVEL;
                break;
            }
        }

        if(name != null) {
            if(name.equals(LEVEL) || name.equals(ID)) {
                editor.putInt(name, Integer.parseInt(value));
            }
            else {
                editor.putString(name, value);
            }
            editor.commit();
            System.out.println("[DEBUG]: Case matched to `" + name + "` inserted value `" + value + "`");
        }
        else {
            System.out.println("[DEBUG]: No case matched!");
        }
        return value;
    }

    //Fungsi untuk melakukan get data dari dalam shared preferences
    public static String getUserdata(String key) {
        String name = null;
        String value = null;

        switch(key) {
            case "id": {
                name = ID;
                break;
            }
            case "username": {
                name = USERNAME;
                break;
            }
            case "fullname": {
                name = FULLNAME;
                break;
            }
            case "email": {
                name = EMAIL;
                break;
            }
            case "password": {
                name = PASSWORD;
                break;
            }
            case "phone": {
                name = PHONE;
                break;
            }
            case "created_date": {
                name = CREATED_DATE;
                break;
            }
            case "level": {
                name = LEVEL;
                break;
            }
        }

        if(name != null) {
            if(name.equals(LEVEL) || name.equals(ID)) {
                value = String.valueOf(sharedPreferences.getInt(name, 0));
            }
            else {
                value = sharedPreferences.getString(name, null);
            }
            System.out.println("[DEBUG]: Case matched to `" + name + "` with value `" + value + "`");
        }
        else {
            System.out.println("[DEBUG]: No case matched!");
        }
        return value;
    }

    public static String setCartdata(String key, String value) {
        String name = null;

        switch(key) {
            case "cart_index": {
                name = CART_INDEX;
                break;
            }
            case "cart_kd_barang": {
                name = CART_KD_BARANG;
                break;
            }
            case "cart_harga_barang": {
                name = CART_HARGA_BARANG;
                break;
            }
            case "cart_jumlah_barang": {
                name = CART_JUMLAH_BARANG;
                break;
            }
            case "cart_price_total": {
                name = CART_PRICE_TOTAL;
                break;
            }
        }

        if(name != null) {
            if(name.equals(CART_INDEX) || name.equals(CART_HARGA_BARANG) || name.equals(CART_JUMLAH_BARANG) || name.equals(CART_PRICE_TOTAL)) {
                editor.putInt(name, Integer.parseInt(value));
            }
            else {
                editor.putString(name, value);
            }
            editor.commit();
            System.out.println("[DEBUG]: Case matched to `" + name + "` inserted value `" + value + "`");
        }
        else {
            System.out.println("[DEBUG]: No case matched!");
        }
        return value;
    }

    public static String getCartdata(String key, String type) {
        String value = null;

        if(key != null) {
            if(type.equals("int")) {
                value = String.valueOf(sharedPreferences.getInt(key, 0));
            }
            else if(type.equals("string")) {
                value = sharedPreferences.getString(key, null);
            }
            System.out.println("[DEBUG]: Case matched to `" + key + "` with value `" + value + "`");
        }
        else {
            System.out.println("[DEBUG]: No case matched!");
        }
        return value;
    }

    public static void cartAction(String action, Products product, int qty) {
        int index = Integer.parseInt(getCartdata("cart_index", "int"));
        int flag = 0;

        switch(action) {
            case "add": {
                for(int i = 1; i <= index; i++) {
                    String kode_barang = "";
                    kode_barang = getCartdata("cart_kd_barang_" + i, "string");
                    int jumlah_barang = Integer.parseInt(getCartdata("cart_jumlah_barang_" + i, "int"));
                    if(product.getCode().equals(kode_barang)) {
                        if(product.getStock() >= (jumlah_barang+qty)) {
                            Log.d("[DEBUG]", "stok ok - " + product.getStock() + " -- " + (jumlah_barang+qty));
                            editor.putInt("cart_jumlah_barang_" + i, jumlah_barang + qty);
                            editor.putInt("cart_stock_barang_" + i, product.getStock());
                            editor.putString("cart_msg", "OK");
                            flag = 1;
                        }
                        else {
                            Log.d("[DEBUG]", "stok not ok");
                            editor.putString("cart_msg", "NOTOK");
                            flag = 2;
                        }
                    }
                }

                if(flag == 0) {
                    index += 1;

                    editor.putInt("cart_index", index);
                    editor.putString("cart_kd_barang_" + index, product.getCode());
                    editor.putString("cart_nama_barang_" + index, product.getName());
                    editor.putInt("cart_harga_barang_" + index, product.getPrice());
                    editor.putInt("cart_stock_barang_" + index, product.getStock());
                    editor.putInt("cart_jumlah_barang_" + index, qty);
                    editor.putInt("cart_deleted_" + index, 0);
                    editor.putString("cart_msg", "OK");
                }
                break;
            }
            case "delete": {
                for(int i = 1; i <= index; i++) {
                    String kode_barang = "";
                    kode_barang = getCartdata("cart_kd_barang_" + i, "string");
                    if(product.getCode().equals(kode_barang)) {
                        editor.putInt("cart_deleted_" + i, 1);
                    }
                }
                break;
            }
            case "increase": {
                String msg = "";
                for(int i = 1; i <= index; i++) {
                    String kode_barang = "";
                    kode_barang = getCartdata("cart_kd_barang_" + i, "string");
                    int jumlah_barang = Integer.parseInt(getCartdata("cart_jumlah_barang_" + i, "int"));
                    if(product.getCode().equals(kode_barang)) {
                        Log.d("[DEBUG]", product.getStock() + "-" + (jumlah_barang + qty));
                        if(product.getStock() >= (jumlah_barang + qty)) {
                            editor.putInt("cart_jumlah_barang_" + i, jumlah_barang + qty);
                            editor.putString("cart_msg", "OK");
                        }
                        else {
                            editor.putString("cart_msg", "NOTOK");
                        }
                    }
                }
                break;
            }
            case "decrease": {
                for(int i = 1; i <= index; i++) {
                    String kode_barang = "";
                    kode_barang = getCartdata("cart_kd_barang_" + i, "string");
                    int jumlah_barang = Integer.parseInt(getCartdata("cart_jumlah_barang_" + i, "int"));
                    if(product.getCode().equals(kode_barang)) {
                        editor.putInt("cart_jumlah_barang_" + i, jumlah_barang - qty);
                    }
                }
                break;
            }
        }
        editor.commit();
    }

    public static int countCartTotal() {
        int index = Integer.parseInt(getCartdata("cart_index", "int"));
        int price_total = 0;

        for(int i = 1; i <= index; i++) {
            int deleted = Integer.parseInt(getCartdata("cart_deleted_" + i, "int"));
            if(deleted == 0) {
                int entry_price = Integer.parseInt(getCartdata("cart_harga_barang_" + i, "int")) * Integer.parseInt(getCartdata("cart_jumlah_barang_" + i, "int"));
                price_total += entry_price;
            }
        }
        editor.putInt("cart_price_total", price_total);
        editor.commit();

        return price_total;
    }

    public static void flushCart() {
        int index = Integer.parseInt(getCartdata("cart_index", "int"));

        for(int i = 1; i <= index; i++) {
            editor.remove("cart_kd_barang_" + i);
            editor.remove("cart_nama_barang_" + i);
            editor.remove("cart_harga_barang_" + i);
            editor.remove("cart_jumlah_barang_" + i);
            editor.remove("cart_deleted_" + i);
        }
        editor.putInt("cart_index", 0);
        editor.putInt("cart_price_total", 0);
        editor.commit();
    }

    //Fungsi untuk melakukan flush data / clear data yang akan dijalankan ketika user Log Out
    public static void flushSession() {
        editor.clear();
        editor.commit();
    }
}
