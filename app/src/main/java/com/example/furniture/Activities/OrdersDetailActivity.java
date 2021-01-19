package com.example.furniture.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.furniture.Adapters.OrderDetailsAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.macroCollection;
import com.example.furniture.Helpers.prefHelper;
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

public class OrdersDetailActivity extends AppCompatActivity {

    private EditText edtNama, edtAlamat, edtPhone, edtTanggal, edtTotal;
    private Spinner spnStatus;
    private ListView listView;
    private AsyncHttpClient asyncHttpClient;
    private CardView btnKomplain;
    private Context mContext;
    private List<OrderDetails> orderDetailsList;
    private RequestParams params = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

        mContext = this;

        prefHelper.init(mContext);

        Intent i = getIntent();
        final Orders orders = new Orders();
        orders.setCode(i.getStringExtra("code"));
        orders.setBuyer_name(i.getStringExtra("buyer_name"));
        orders.setBuyer_phone(i.getStringExtra("buyer_phone"));
        orders.setBuyer_address(i.getStringExtra("buyer_address"));
        orders.setStatus(i.getIntExtra("status", 0));
        orders.setTotal(i.getIntExtra("total", 0));
        orders.setCreated_on(i.getStringExtra("created_on"));

        spnStatus = (Spinner) findViewById(R.id.spnStatus);
        listView = (ListView) findViewById(R.id.lvItem);
        edtAlamat = (EditText) findViewById(R.id.edt_alamat);
        btnKomplain = (CardView) findViewById(R.id.btnKomplain);
        edtNama = (EditText) findViewById(R.id.edt_fullname);
        edtTanggal = (EditText)findViewById(R.id.edt_tanggal);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtTotal = (EditText) findViewById(R.id.edt_total);

        edtAlamat.setText(orders.getBuyer_address());
        edtNama.setText(orders.getBuyer_name());
        edtTanggal.setText(orders.getCreated_on());
        edtPhone.setText(orders.getBuyer_phone());
        spnStatus.setSelection(orders.getStatus());
        edtTotal.setText(macroCollection.formatRupiah(orders.getTotal()));

        edtTotal.setEnabled(false);
        edtAlamat.setEnabled(false);
        edtNama.setEnabled(false);
        edtPhone.setEnabled(false);
        edtTotal.setEnabled(false);
        spnStatus.setEnabled(false);
        edtTanggal.setEnabled(false);

        Log.d("[DEBUG]", prefHelper.getUserdata("level"));

        if(Integer.parseInt(prefHelper.getUserdata("level")) == 1 || Integer.parseInt(prefHelper.getUserdata("level")) == 2) {
            btnKomplain.setVisibility(View.GONE);
        }

        btnKomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrdersDetailActivity.this, ComplaintsAddActivity.class);
                i.putExtra("code", orders.getCode());
                startActivity(i);
            }
        });

        setupContent(orders.getCode());

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spnStatus.getSelectedItemPosition() != orders.getStatus()) {
                    updateStatus(spnStatus.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupContent(String code) {
        String SERVER_URL = Config.SERVER_URL + "p=myorderdetail&code=" + code;

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(mContext, SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Parser parser = Parser.parseOrderDetails(JSONResponse);
                if(parser.getCode() == 200) {
                    orderDetailsList = parser.getOrderDetailsList();
                    if(orderDetailsList.size() > 0) {
                        OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(mContext, orderDetailsList);
                        orderDetailsAdapter.notifyDataSetChanged();
                        listView.setAdapter(orderDetailsAdapter);
                    }
                    else {
                        Toast.makeText(mContext, "Data kosong!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(parser.getCode() == 400) {
                    Toast.makeText(mContext, "Error!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateStatus(int selectedItemPosition) {

    }
}
