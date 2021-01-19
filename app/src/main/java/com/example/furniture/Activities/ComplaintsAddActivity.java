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
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

public class ComplaintsAddActivity extends AppCompatActivity {

    private String fileName = "no_image", pPath, imageName, fileExt;
    private EditText edtCode, edtKomplain;
    private ImageView img;
    private int user_id;
    private CardView btnSubmit, btnBack, btnImage;
    private RequestParams params = new RequestParams();
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintsadd);

        prefHelper.init(ComplaintsAddActivity.this);

        user_id = Integer.parseInt(prefHelper.getUserdata("id"));

        Intent i = getIntent();
        String code = i.getStringExtra("code");

        btnSubmit = (CardView) findViewById(R.id.btnSubmit);
        btnBack = (CardView) findViewById(R.id.btnBack);
        btnImage = (CardView) findViewById(R.id.btnImage);
        img = (ImageView) findViewById(R.id.imgComplaint);
        edtCode = (EditText) findViewById(R.id.edt_order_code);
        edtKomplain = (EditText) findViewById(R.id.edt_komplain);
        
        edtCode.setText(code);
        edtCode.setEnabled(false);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComplaint();
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

    private void submitComplaint() {
        String order_code = edtCode.getText().toString().trim();
        String complaint = edtKomplain.getText().toString().trim();
        if(!fileName.equals("no_image") && order_code.length() > 0 && complaint.length() > 0) {
            String SERVER_URL = Config.SERVER_URL + "p=submitcomplaint";

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

            params.put("user_id", user_id);
            params.put("order_code", order_code);
            params.put("complaint", complaint);
            params.put("file_name", fileName);
            if(!fileName.equals("no_image")) {
                try {
                    params.put("uploaded_file", new File(pPath));
                    params.put("old_image", imageName);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            asyncHttpClient.post(ComplaintsAddActivity.this, SERVER_URL, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String JSONResponse = new String(responseBody);
                    Log.d("[DEBUG]", JSONResponse);
                    Parser parser = Parser.parseProducts(JSONResponse);
                    if(parser.getCode() == 200) {
                        Toast.makeText(ComplaintsAddActivity.this, "Berhasil!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if(parser.getCode() == 400) {
                        Toast.makeText(ComplaintsAddActivity.this, "Gagal!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(ComplaintsAddActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("[DEBUG]", "Failure");
                }
            });
        }
        else {
            Toast.makeText(ComplaintsAddActivity.this, "Gagal, pastikan kamu memasukan foto dan mengisi semua field!", Toast.LENGTH_LONG).show();
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
                    .into(img);
            Log.d("[DEBUG]", fileName);
        }
    }
}
