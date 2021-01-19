package com.example.furniture.Activities;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.furniture.Adapters.OrderDetailsAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.macroCollection;
import com.example.furniture.Models.OrderDetails;
import com.example.furniture.Models.Orders;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

public class ManagePaymentDetailActivity extends AppCompatActivity {

    private Button submit, back;
    private ImageView img;
    private TextView nama, alamat, phone, total, tanggal, status;
    private ListView lvItem;
    private List<OrderDetails> orderDetailsList;
    private Context mContext;
    private AsyncHttpClient asyncHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managepaymentdetail);

        mContext = this;

        back = (Button) findViewById(R.id.btnBack);
        submit = (Button) findViewById(R.id.btnConfirm);
        img = (ImageView) findViewById(R.id.img);
        nama = (TextView) findViewById(R.id.txtNama);
        alamat = (TextView) findViewById(R.id.txtAlamat);
        phone = (TextView) findViewById(R.id.txtHandphone);
        total = (TextView) findViewById(R.id.txtTotal);
        tanggal = (TextView) findViewById(R.id.txtTanggal);
        status = (TextView) findViewById(R.id.txtStatus);
        lvItem = (ListView) findViewById(R.id.lvItem);

        Intent i = getIntent();
        final Orders orders = new Orders();
        orders.setCode(i.getStringExtra("code"));
        orders.setBuyer_name(i.getStringExtra("buyer_name"));
        orders.setBuyer_phone(i.getStringExtra("buyer_phone"));
        orders.setBuyer_address(i.getStringExtra("buyer_address"));
        orders.setStatus(i.getIntExtra("status", 0));
        orders.setTotal(i.getIntExtra("total", 0));
        orders.setCreated_on(i.getStringExtra("created_on"));

        nama.setText(orders.getBuyer_name());
        alamat.setText(orders.getBuyer_address());
        phone.setText(orders.getBuyer_phone());
        total.setText(macroCollection.formatRupiah(orders.getTotal()));
        tanggal.setText(orders.getCreated_on());
        status.setText(getStatus(orders.getStatus()));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(orders.getStatus() != 0) {
            submit.setVisibility(View.INVISIBLE);
        }
        else {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status = orders.getStatus();
                    updateStatus(orders.getCode(), status+1);
                }
            });
        }

        setupContent(orders.getCode());
    }

    private void updateStatus(String code, int status) {
        String SERVER_URL = Config.SERVER_URL + "p=updateorder";
        RequestParams params = new RequestParams();
        params.put("code", code);
        params.put("status", status);

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.post(mContext, SERVER_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG", JSONResponse);
                Parser parser = Parser.parseResponse(JSONResponse);
                if(parser.getCode() == 200) {
                    Log.d("[DEBUG]", String.valueOf(parser.getCode()));
                    Toast.makeText(mContext, parser.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(parser.getCode() == 400) {
                    Toast.makeText(mContext, parser.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getStatus(int status) {
        String strStatus = "";

        switch(status) {
            case 0: {
                strStatus =  "MENUNGGU PEMBAYARAN";
                break;
            }
            case 1: {
                strStatus =  "PEMBAYARAN TERVERIFIKASI";
                break;
            }
            case 2: {
                strStatus =  "DIKIRIM";
                break;
            }
            case 3: {
                strStatus = "PESANAN DITERIMA";
                break;
            }
        }

        return strStatus;
    }

    private void setupContent(String code) {
        String SERVER_URL = Config.SERVER_URL + "p=orderdetail&code=" + code;

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(ManagePaymentDetailActivity.this, SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Parser parser = Parser.parseOrderDetails(JSONResponse);
                if(parser.getCode() == 200) {
                    orderDetailsList = parser.getOrderDetailsList();
                    if(orderDetailsList.size() > 0) {
                        OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(ManagePaymentDetailActivity.this, orderDetailsList);
                        orderDetailsAdapter.notifyDataSetChanged();
                        lvItem.setAdapter(orderDetailsAdapter);

                        OrderDetails orderDetails = orderDetailsList.get(0);
                        Glide.with(ManagePaymentDetailActivity.this)
                                .load(Config.PAYMENT_IMAGE_URL + orderDetails.getFotobayar())
                                .into(img);
                    }
                    else {
                        Toast.makeText(ManagePaymentDetailActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(parser.getCode() == 400) {
                    Toast.makeText(ManagePaymentDetailActivity.this, "Error!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ManagePaymentDetailActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
