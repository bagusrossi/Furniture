package com.example.furniture.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.furniture.Adapters.CartAdapter;
import com.example.furniture.Helpers.macroCollection;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Keranjang;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {
    private Context context;
    private AsyncHttpClient asyncHttpClient;
    private ListView listView;
    public EditText edtTotal;
    private Button btnBatal, btnPesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        context = this;

        prefHelper.init(context);

        listView = (ListView) findViewById(R.id.lvPurchase);
        edtTotal = (EditText) findViewById(R.id.edt_total);
        btnBatal = (Button) findViewById(R.id.btnBatal);
        btnPesan = (Button) findViewById(R.id.btnPesan);

        setupList();

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, OrderActivity.class);
                startActivity(i);

            }
        });
    }

    private void setupList() {
        List<Keranjang> keranjangList = new ArrayList<>();
        int index = Integer.parseInt(prefHelper.getCartdata("cart_index", "int"));
        for(int i = 1; i <= index; i++) {
            int deleted = Integer.parseInt(prefHelper.getCartdata("cart_deleted_" + i, "int"));
            if(deleted == 0) {
                Keranjang keranjang = new Keranjang();
                keranjang.setNama_barang(prefHelper.getCartdata("cart_nama_barang_" + i, "string"));
                keranjang.setStock_barang(Integer.parseInt(prefHelper.getCartdata("cart_stock_barang_" + i, "int")));
                keranjang.setKode_barang(prefHelper.getCartdata("cart_kd_barang_" + i, "string"));
                keranjang.setHarga_satuan(Integer.parseInt(prefHelper.getCartdata("cart_harga_barang_" + i, "int")));
                keranjang.setJumlah_barang(Integer.parseInt(prefHelper.getCartdata("cart_jumlah_barang_" + i, "int")));
                keranjangList.add(keranjang);
            }
        }

        if(keranjangList.size() > 0) {
            CartAdapter cartAdapter = new CartAdapter(CartActivity.this, keranjangList);
            cartAdapter.notifyDataSetChanged();
            listView.setAdapter(cartAdapter);

            int total_price = prefHelper.countCartTotal();
            edtTotal.setText(macroCollection.formatRupiah(total_price));
        }
        else {
            Toast.makeText(CartActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupList();
    }
}
