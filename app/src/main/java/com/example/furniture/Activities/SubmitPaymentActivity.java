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
import com.example.furniture.Models.Orders;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

public class SubmitPaymentActivity extends AppCompatActivity {

    private EditText edtCode;
    private ImageView imgIcon;
    private CardView btnSubmit, btnBack, btnImage;

    private AsyncHttpClient asyncHttpClient;
    private String fileName = "no_image", pPath, imageName, fileExt;
    private RequestParams param = new RequestParams();
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitpayment);

        prefHelper.init(this);

        edtCode = (EditText) findViewById(R.id.edt_code);
        btnSubmit = (CardView) findViewById(R.id.btnSubmit);
        btnBack = (CardView) findViewById(R.id.btnBack);
        btnImage = (CardView) findViewById(R.id.btnImage);
        imgIcon = (ImageView) findViewById(R.id.imgCategoryIcon);

        Intent i = getIntent();
        final Orders orders = new Orders();
        orders.setCode(i.getStringExtra("code"));
        orders.setBuyer_name(i.getStringExtra("buyer_name"));
        orders.setBuyer_phone(i.getStringExtra("buyer_phone"));
        orders.setBuyer_address(i.getStringExtra("buyer_address"));
        orders.setStatus(i.getIntExtra("status", 0));
        orders.setTotal(i.getIntExtra("total", 0));
        orders.setCreated_on(i.getStringExtra("created_on"));

        edtCode.setText(orders.getCode());
        edtCode.setEnabled(false);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment(Integer.parseInt(prefHelper.getUserdata("id")), orders.getCode());
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

    private void submitPayment(int user_id, String code) {
        String SERVER_URL = Config.SERVER_URL + "p=submitpayment";
        asyncHttpClient = new AsyncHttpClient();

        param.put("user_id", user_id);
        param.put("code", code);
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

        asyncHttpClient.post(SubmitPaymentActivity.this, SERVER_URL, param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                Parser parser = Parser.parseCategory(JSONResponse);
                if(parser.getCode() == 200) {
                    Toast.makeText(SubmitPaymentActivity.this, "Berhasil!", Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(parser.getCode() == 400) {
                    Toast.makeText(SubmitPaymentActivity.this, "Gagal!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(SubmitPaymentActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[DEBUG]", "Failure");
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
