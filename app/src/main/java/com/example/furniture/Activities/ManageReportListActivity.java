package com.example.furniture.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.furniture.Adapters.OrdersAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.macroCollection;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Orders;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class ManageReportListActivity extends AppCompatActivity {
    private Context mContext;
    private ListView listView;
    private AsyncHttpClient asyncHttpClient;
    private Button btnKembali;
    private EditText edtTotal;
    private int total = 0;
    private List<Orders> ordersList;
    private String from, to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managereportlist);

        mContext = this;

        prefHelper.init(mContext);

        Intent i = getIntent();
        from = i.getStringExtra("from");
        to = i.getStringExtra("to");

        listView = (ListView) findViewById(R.id.lvHistory);
        btnKembali = (Button) findViewById(R.id.btnKembali);
        edtTotal = (EditText) findViewById(R.id.edt_total);

        setupContent();

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupContent() {
        String SERVER_URL = Config.SERVER_URL + "p=getorderbydate";

        asyncHttpClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("from", from);
        params.put("to", to);

        asyncHttpClient.post(SERVER_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                ordersList = Parser.parseOrdersList(JSONResponse);
                if (ordersList.size() > 0) {
                    OrdersAdapter ordersAdapter = new OrdersAdapter(mContext, ordersList, "history");
                    ordersAdapter.notifyDataSetChanged();
                    listView.setAdapter(ordersAdapter);

                    Log.d("[DEBUG]", "Size: " + ordersList.size());

                    countTotal(ordersList);
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

    private void countTotal(List<Orders> ordersList) {
        total = 0;
        edtTotal.setText(macroCollection.formatRupiah(0));

        for (int i = 0; i < ordersList.size(); i++) {
            Orders orders = ordersList.get(i);
            total = total + orders.getTotal();
            Log.d("[DEBUG]", "Total: " + total + " - " + i);
        }

        edtTotal.setText(macroCollection.formatRupiah(total));
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupContent();
    }
}
