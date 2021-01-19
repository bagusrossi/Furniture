package com.example.furniture.Helpers;

import android.content.Context;

import java.text.NumberFormat;
import java.util.Locale;

public class macroCollection {

    public Context context;

    public static String formatRupiah(int number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    public static String generateProductCode(String category_code, int category_index) {
        category_index += 1;
        return category_code + category_index;
    }
}
