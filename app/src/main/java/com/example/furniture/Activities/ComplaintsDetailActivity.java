package com.example.furniture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class ComplaintsDetailActivity extends AppCompatActivity {

    private TextView txtCode, txtNama, txtPhone, txtAlamat, txtTanggal, txtStatus, txtKomplain;
    private int id, status, user_id, userLevel;
    private ImageView imageView;
    private String order_code, image, information, created_on, buyer_name, buyer_address, buyer_phone;
    private Button btnConfirm, btnCancel;
    private AsyncHttpClient asyncHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintsdetail);

        prefHelper.init(ComplaintsDetailActivity.this);
        userLevel = Integer.parseInt(prefHelper.getUserdata("level"));

        txtCode = (TextView) findViewById(R.id.txtCode);
        txtNama = (TextView) findViewById(R.id.txtNama);
        txtPhone = (TextView) findViewById(R.id.txtHandphone);
        txtAlamat = (TextView) findViewById(R.id.txtAlamat);
        txtTanggal = (TextView) findViewById(R.id.txtTanggal);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtKomplain = (TextView) findViewById(R.id.txtInformation);
        btnCancel = (Button) findViewById(R.id.btnBack);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        imageView = (ImageView) findViewById(R.id.img);

        Intent i = getIntent();
        id = i.getIntExtra("id", 0);
        status = i.getIntExtra("status", 0);
        user_id = i.getIntExtra("user_id", 0);
        order_code = i.getStringExtra("order_code");
        image = i.getStringExtra("image");
        information = i.getStringExtra("information");
        created_on = i.getStringExtra("created_on");
        buyer_name = i.getStringExtra("buyer_name");
        buyer_address = i.getStringExtra("buyer_address");
        buyer_phone = i.getStringExtra("buyer_phone");

        txtCode.setText("(ORDER CODE) - " + order_code);
        txtNama.setText("(BUYER NAME) - " + buyer_name);
        txtPhone.setText("(HANDPHONE) - " + buyer_phone);
        txtAlamat.setText("(ALAMAT) - " + buyer_address);
        txtTanggal.setText("(TANGGAL KOMPLAIN) - " + created_on);
        txtStatus.setText(getStatus(status));
        Glide.with(ComplaintsDetailActivity.this)
                .load(Config.COMPLAINT_IMAGE_URL + image)
                .into(imageView);
        txtKomplain.setText(information);

        if(userLevel == 0) {
            btnConfirm.setVisibility(View.GONE);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnCancel.getLayoutParams();
            layoutParams.weight = 2;

            btnCancel.setLayoutParams(layoutParams);
        }
        else if(userLevel == 1) {
            if(status == 2) {
                btnConfirm.setText("PESANAN DITERIMA SELESAI");
            }
            else {
                btnConfirm.setText("KOMPLAIN SELESAI");
            }
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userLevel == 2) {
                    updateComplaint(order_code, 1);
                }
                else if(userLevel == 1) {
                    if(status == 2) {
                        updateComplaint(order_code, 3);
                    }
                    else {
                        updateComplaint(order_code, 2);
                    }
                }
            }
        });
    }

    public String getStatus(int status) {
        String strStatus = "";

        switch(status) {
            case 0: {
                strStatus =  "PENDING";
                break;
            }
            case 1: {
                strStatus =  "KOMPLAIN DI PROSES";
                break;
            }
            case 2: {
                strStatus =  "KOMPLAIN SELESAI";
                break;
            }
            case 3: {
                strStatus = "PESANAN DITERIMA";
                break;
            }
        }

        return strStatus;
    }

    public void updateComplaint(String order_code, int status) {
        String SERVER_URL = Config.SERVER_URL + "p=updatecomplaint";
        RequestParams params = new RequestParams();
        params.put("code", order_code);
        params.put("status", status);

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.post(ComplaintsDetailActivity.this, SERVER_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG", JSONResponse);
                Parser parser = Parser.parseResponse(JSONResponse);
                if(parser.getCode() == 200) {
                    Log.d("[DEBUG]", String.valueOf(parser.getCode()));
                    Toast.makeText(ComplaintsDetailActivity.this, parser.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(parser.getCode() == 400) {
                    Toast.makeText(ComplaintsDetailActivity.this, parser.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ComplaintsDetailActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
