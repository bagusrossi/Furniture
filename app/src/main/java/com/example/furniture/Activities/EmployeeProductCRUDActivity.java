package com.example.furniture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

public class EmployeeProductCRUDActivity extends AppCompatActivity {
    private CardView btnSubmit, btnBack;
    private EditText edtCatName, edtProdName, edtCode, edtStock;
    private ImageView imgIcon;

    private AsyncHttpClient asyncHttpClient;
    private String prodIcon, category_name, prod_name, prod_code;
    private int id, prod_stock, category_id;
    private RequestParams param = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeeproductcrud);

        btnSubmit = (CardView) findViewById(R.id.btnSubmit);
        btnBack = (CardView) findViewById(R.id.btnBack);
        edtCatName = (EditText) findViewById(R.id.edt_category_name);
        edtProdName = (EditText) findViewById(R.id.edt_product_name);
        edtCode = (EditText) findViewById(R.id.edt_product_code);
        edtStock = (EditText) findViewById(R.id.edt_product_stock);
        imgIcon = (ImageView) findViewById(R.id.imgProductIcon);

        Intent i = getIntent();
        final String pageMode = i.getStringExtra("pageMode");
        category_id = i.getIntExtra("category_id", 0);
        switch (pageMode) {
            case "edit":{
                edtCatName.setText(i.getStringExtra("category_name"));
                edtProdName.setText(i.getStringExtra("name"));
                edtCode.setText(i.getStringExtra("code"));
                edtStock.setText(String.valueOf(i.getIntExtra("stock", 0)));

                prodIcon = i.getStringExtra("attachment");

                if(!prodIcon.equals("no_image")) {
                    Glide.with(EmployeeProductCRUDActivity.this)
                            .load(Config.PRODUCT_IMAGE_URL + prodIcon)
                            .into(imgIcon);
                }
                id = i.getIntExtra("id", 0);
                break;
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProduct(pageMode);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submitProduct(String pageMode) {
        category_name = edtCatName.getText().toString().trim();
        prod_name = edtProdName.getText().toString().trim();
        prod_code = edtCode.getText().toString().trim();
        prod_stock = Integer.parseInt(edtStock.getText().toString().trim());

        if(category_name.length() > 0 && prod_name.length() > 0 && prod_code.length() > 0 && prod_stock > 0) {
            String SERVER_URL = "";

            int passCheck = 1;
            switch(pageMode) {
                case "edit": {
                    SERVER_URL = Config.SERVER_URL + "p=updateproductstock";
                    break;
                }
                default: {
                    break;
                }
            }
            if(passCheck == 1) {
                asyncHttpClient = new AsyncHttpClient();

                param.put("id", id);
                param.put("category_id", category_id);
                param.put("code", prod_code);
                param.put("name", prod_name);
                param.put("stock", prod_stock);

                asyncHttpClient.post(EmployeeProductCRUDActivity.this, SERVER_URL, param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String JSONResponse = new String(responseBody);
                        Log.d("[DEBUG]", JSONResponse);
                        Parser parser = Parser.parseProducts(JSONResponse);
                        if(parser.getCode() == 200) {
                            Toast.makeText(EmployeeProductCRUDActivity.this, "Berhasil!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else if(parser.getCode() == 400) {
                            Toast.makeText(EmployeeProductCRUDActivity.this, "Gagal!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(EmployeeProductCRUDActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("[DEBUG]", "Failure");
                    }
                });
            }
            else {
                Toast.makeText(EmployeeProductCRUDActivity.this, "Gaga!", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(EmployeeProductCRUDActivity.this, "Harap isi semua field!", Toast.LENGTH_LONG).show();
        }
    }
}
