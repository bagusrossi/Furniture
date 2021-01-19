package com.example.furniture.Activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Products;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgBarang;
    private TextView namaBarang, hargaBarang, descBarang, stockBarang;
    private EditText edtQty;
    private Button btnAdd;

    private String prodIcon;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);

        prefHelper.init(this);

        imgBarang = (ImageView) findViewById(R.id.imgBarang);
        namaBarang = (TextView) findViewById(R.id.txtNamaBarang);
        hargaBarang = (TextView) findViewById(R.id.txtHargaBarang);
        descBarang = (TextView) findViewById(R.id.txtDetailBarang);
        stockBarang = (TextView) findViewById(R.id.txtStockBarang);
        edtQty = (EditText) findViewById(R.id.edt_qty);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        Intent i = getIntent();
        String strName = i.getStringExtra("name") + " (Code: " + i.getStringExtra("code") + ")";
        String strStock = i.getIntExtra("stock", 0) + " unit";
        namaBarang.setText(strName);
        descBarang.setText(i.getStringExtra("description"));
        stockBarang.setText(strStock);
        hargaBarang.setText(String.valueOf(i.getIntExtra("price", 0)));

        prodIcon = i.getStringExtra("attachment");

        if(!prodIcon.equals("no_image")) {
            Glide.with(ProductDetailActivity.this)
                    .load(Config.PRODUCT_IMAGE_URL + prodIcon)
                    .into(imgBarang);
        }
        id = i.getIntExtra("id", 0);

        final Products product = new Products();
        product.setId(id);
        product.setName(i.getStringExtra("name"));
        product.setPrice(i.getIntExtra("price", 0));
        product.setCode(i.getStringExtra("code"));
        product.setStock(i.getIntExtra("stock", 0));

        if(!prefHelper.getUserdata("level").equals("0")) {
            btnAdd.setVisibility(View.INVISIBLE);
            edtQty.setVisibility(View.INVISIBLE);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtQty.getText().toString().trim().length() > 0) {
                    int qty = Integer.parseInt(edtQty.getText().toString());
                    if(qty != 0) {
                        if(qty <= product.getStock()) {
                            prefHelper.cartAction("add", product, qty);
                            String msg = prefHelper.getCartdata("cart_msg", "string");
                            if(msg.equals("OK")) {
                                Toast.makeText(ProductDetailActivity.this, "Sukses menambahkan ke keranjang, lanjutkan pembelian di keranjang!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                                Toast.makeText(ProductDetailActivity.this, "Stock barang tidak cukup, anda sudah mempunyai produk terkait di keranjang!", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(ProductDetailActivity.this, "Kuantitas tidak bisa lebih dari stok!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(ProductDetailActivity.this, "Harap isi kuantitas barang yang ingin dibeli!!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(ProductDetailActivity.this, "Harap isi kuantitas barang yang ingin dibeli!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
