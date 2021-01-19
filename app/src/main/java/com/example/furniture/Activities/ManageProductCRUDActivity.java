package com.example.furniture.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.macroCollection;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

public class ManageProductCRUDActivity extends AppCompatActivity {
    private CardView btnSubmit, btnDelete, btnBack, btnImage;
    private EditText edtCatName, edtProdName, edtCode, edtDesc, edtStock, edtPrice;
    private ImageView imgIcon;
    private TextView txtFormated;

    private AsyncHttpClient asyncHttpClient;
    private String fileName = "no_image", pPath, imageName, fileExt;
    private String prodIcon, category_name, prod_name, prod_desc, prod_code, category_code;
    private int id, prod_stock, prod_price, category_id, category_index;
    private RequestParams param = new RequestParams();
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageproductcrud);

        btnSubmit = (CardView) findViewById(R.id.btnSubmit);
        btnDelete = (CardView) findViewById(R.id.btnDelete);
        btnBack = (CardView) findViewById(R.id.btnBack);
        btnImage = (CardView) findViewById(R.id.btnImage);
        txtFormated = (TextView) findViewById(R.id.txtFormatedPrice);
        edtCatName = (EditText) findViewById(R.id.edt_category_name);
        edtProdName = (EditText) findViewById(R.id.edt_product_name);
        edtCode = (EditText) findViewById(R.id.edt_product_code);
        edtDesc = (EditText) findViewById(R.id.edt_product_desc);
        edtStock = (EditText) findViewById(R.id.edt_product_stock);
        edtPrice = (EditText) findViewById(R.id.edt_product_price);
        imgIcon = (ImageView) findViewById(R.id.imgProductIcon);

        edtDesc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = "";
                if(edtPrice.getText().toString().trim().length() > 0) {
                    str = "Price: " + macroCollection.formatRupiah(Integer.parseInt(edtPrice.getText().toString().trim()));
                }
                else {
                    str = "Price: Rp0";
                }
                txtFormated.setText(str);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Intent i = getIntent();
        final String pageMode = i.getStringExtra("pageMode");
        category_id = i.getIntExtra("category_id", 0);
        category_code = i.getStringExtra("category_code");
        category_index = i.getIntExtra("category_index", 0);
        Log.d("[DEBUG]", "Cat index:" + category_index);
        switch (pageMode) {
            case "add": {
                edtCatName.setText(i.getStringExtra("category_name"));
                String prodCode = macroCollection.generateProductCode(category_code, category_index);
                edtCode.setText(prodCode);
                btnDelete.setVisibility(View.GONE);
                break;
            }
            case "edit":{
                edtCatName.setText(i.getStringExtra("category_name"));
                edtProdName.setText(i.getStringExtra("name"));
                edtCode.setText(i.getStringExtra("code"));
                edtDesc.setText(i.getStringExtra("description"));
                edtStock.setText(String.valueOf(i.getIntExtra("stock", 0)));
                edtPrice.setText(String.valueOf(i.getIntExtra("price", 0)));

                prodIcon = i.getStringExtra("attachment");

                if(!prodIcon.equals("no_image")) {
                    Glide.with(ManageProductCRUDActivity.this)
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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(id);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    private void deleteProduct(int id) {
        String SERVER_URL = Config.SERVER_URL + "p=deleteproduct";

        asyncHttpClient = new AsyncHttpClient();

        RequestParams param = new RequestParams();
        param.put("id", id);

        asyncHttpClient.post(ManageProductCRUDActivity.this, SERVER_URL, param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                Parser parser = Parser.parseCategory(JSONResponse);
                if(parser.getCode() == 200) {
                    Toast.makeText(ManageProductCRUDActivity.this, "Sukses menghapus!", Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(parser.getCode() == 400) {
                    Toast.makeText(ManageProductCRUDActivity.this, "Gagal menghapus!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ManageProductCRUDActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitProduct(String pageMode) {
        category_name = edtCatName.getText().toString().trim();
        prod_name = edtProdName.getText().toString().trim();
        prod_code = edtCode.getText().toString().trim();
        prod_desc = edtDesc.getText().toString().trim();
        prod_stock = Integer.parseInt(edtStock.getText().toString().trim());
        prod_price = Integer.parseInt(edtPrice.getText().toString().trim());

        if(category_name.length() > 0 && prod_name.length() > 0 && prod_code.length() > 0 && prod_desc.length() > 0 && prod_stock > 0 && prod_price > 0) {
            String SERVER_URL = "";

            int passCheck = 1;
            switch(pageMode) {
                case "add": {
                    SERVER_URL = Config.SERVER_URL + "p=createproduct";
                    if (fileName == "no_image") {
                        passCheck = 0;
                    }
                    break;
                }
                case "edit": {
                    SERVER_URL = Config.SERVER_URL + "p=updateproduct";
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
                param.put("description", prod_desc);
                param.put("stock", prod_stock);
                param.put("price", prod_price);

                param.put("file_name", fileName);
                if(!fileName.equals("no_image")) {
                    try {
                        param.put("uploaded_file", new File(pPath));
                        param.put("old_image", imageName);
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                asyncHttpClient.post(ManageProductCRUDActivity.this, SERVER_URL, param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String JSONResponse = new String(responseBody);
                        Log.d("[DEBUG]", JSONResponse);
                        Parser parser = Parser.parseProducts(JSONResponse);
                        if(parser.getCode() == 200) {
                            Toast.makeText(ManageProductCRUDActivity.this, "Berhasil!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent();
                            i.putExtra("category_index", category_index+1);
                            setResult(2, i);
                            finish();
                        }
                        else if(parser.getCode() == 400) {
                            Toast.makeText(ManageProductCRUDActivity.this, "Gagal!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(ManageProductCRUDActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("[DEBUG]", "Failure");
                    }
                });
            }
            else {
                Toast.makeText(ManageProductCRUDActivity.this, "Gagal, pastikan kamu memasukan nama produk dan foto produk!", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(ManageProductCRUDActivity.this, "Harap isi semua field!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            pPath = cursor.getString(columnIndex);
            cursor.close();

            String splittedFileName[] = pPath.split("/");
            fileName = splittedFileName[splittedFileName.length - 1];

            Glide.with(this)
                    .load(new File(pPath))
                    .into(imgIcon);
            Log.d("[DEBUG]", fileName);
        }
    }
}
