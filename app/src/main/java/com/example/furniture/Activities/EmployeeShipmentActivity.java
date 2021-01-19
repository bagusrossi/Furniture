package com.example.furniture.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.furniture.Adapters.OrdersAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Orders;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class EmployeeShipmentActivity extends AppCompatActivity {

    private Context mContext;
    private ListView listView;
    private List<Orders> ordersList;

    private AsyncHttpClient asyncHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mContext = this;

        prefHelper.init(mContext);

        listView = (ListView) findViewById(R.id.lvHistory);

        setupContent();
    }

    private void setupContent() {
        String SERVER_URL = Config.SERVER_URL + "p=getallverifiedorders";

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                ordersList = Parser.parseOrdersList(JSONResponse);
                Log.d("[DEBUG]", String.valueOf(ordersList.size()));
                if (ordersList.size() > 0) {
                    OrdersAdapter ordersAdapter = new OrdersAdapter(mContext, ordersList, "shipment");
                    ordersAdapter.notifyDataSetChanged();
                    listView.setAdapter(ordersAdapter);
                }
                else {
                    Toast.makeText(mContext, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupContent();
    }
}
