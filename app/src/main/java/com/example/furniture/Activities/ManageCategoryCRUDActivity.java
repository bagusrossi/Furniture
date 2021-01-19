package com.example.furniture.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

public class ManageCategoryCRUDActivity extends AppCompatActivity {

    private CardView btnSubmit, btnDelete, btnBack, btnImage;
    private EditText edtName, edtCode;
    private ImageView imgIcon;

    private int id;
    private String category_name, category_icon, category_code;

    private AsyncHttpClient asyncHttpClient;
    private String fileName = "no_image", pPath, imageName, fileExt;
    private RequestParams param = new RequestParams();
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        btnSubmit = (CardView) findViewById(R.id.btnSubmit);
        btnDelete = (CardView) findViewById(R.id.btnDelete);
        btnBack = (CardView) findViewById(R.id.btnBack);
        btnImage = (CardView) findViewById(R.id.btnImage);
        edtName = (EditText) findViewById(R.id.edt_category_name);
        edtCode = (EditText) findViewById(R.id.edt_category_code);
        imgIcon = (ImageView) findViewById(R.id.imgCategoryIcon);

        Intent i = getIntent();
        final String pageMode = i.getStringExtra("pageMode");
        switch (pageMode) {
            case "add": {
                btnDelete.setVisibility(View.GONE);
                break;
            }
            case "edit":{
                edtName.setText(i.getStringExtra("category_name"));
                category_icon = i.getStringExtra("category_icon");
                category_code = i.getStringExtra("category_code");
                edtCode.setText(category_code);
                if(!category_icon.equals("no_image")) {
                    Glide.with(ManageCategoryCRUDActivity.this)
                            .load(Config.CATEGORY_IMAGE_URL + category_icon)
                            .into(imgIcon);
                }
                id = i.getIntExtra("id", 0);
                break;
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCategory(pageMode);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(id);
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

    private void submitCategory(String pageMode) {
        category_name = edtName.getText().toString().trim();
        category_code = edtCode.getText().toString().trim();
        String SERVER_URL = "";

        int passCheck = 1;

        if(category_name.length() > 0) {
            switch(pageMode) {
                case "add": {
                    SERVER_URL = Config.SERVER_URL + "p=createcategory";
                    if(fileName == "no_image") {
                        passCheck = 0;
                    }
                    break;
                }
                case "edit": {
                    SERVER_URL = Config.SERVER_URL + "p=updatecategory";
                    break;
                }
                default: { break; }
            }

            if(passCheck == 1) {
                asyncHttpClient = new AsyncHttpClient();

                param.put("id", id);
                param.put("category_name", category_name);
                param.put("category_code", category_code);
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

                asyncHttpClient.post(ManageCategoryCRUDActivity.this, SERVER_URL, param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String JSONResponse = new String(responseBody);
                        Log.d("[DEBUG]", JSONResponse);
                        Parser parser = Parser.parseCategory(JSONResponse);
                        if(parser.getCode() == 200) {
                            Toast.makeText(ManageCategoryCRUDActivity.this, "Berhasil!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else if(parser.getCode() == 400) {
                            Toast.makeText(ManageCategoryCRUDActivity.this, "Gagal!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(ManageCategoryCRUDActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("[DEBUG]", "Failure");
                    }
                });
            }
            else {
                Toast.makeText(ManageCategoryCRUDActivity.this, "Gagal, pastikan kamu memasukan nama kategori dan foto icon kategori!", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(ManageCategoryCRUDActivity.this, "Harap isi semua field!", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteCategory(int id) {
        String SERVER_URL = Config.SERVER_URL + "p=deletecategory";

        asyncHttpClient = new AsyncHttpClient();

        RequestParams param = new RequestParams();
        param.put("id", id);

        asyncHttpClient.post(ManageCategoryCRUDActivity.this, SERVER_URL, param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                Parser parser = Parser.parseCategory(JSONResponse);
                if(parser.getCode() == 200) {
                    Toast.makeText(ManageCategoryCRUDActivity.this, "Sukses menghapus!", Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(parser.getCode() == 400) {
                    Toast.makeText(ManageCategoryCRUDActivity.this, "Gagal menghapus!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ManageCategoryCRUDActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
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
