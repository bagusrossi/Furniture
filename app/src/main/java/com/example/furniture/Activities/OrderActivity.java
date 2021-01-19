package com.example.furniture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.furniture.Adapters.CartAdapter;
import com.example.furniture.Adapters.CategoriesAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Keranjang;
import com.example.furniture.Models.Orders;
import com.example.furniture.Models.Universal;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

public class OrderActivity extends AppCompatActivity {

    private EditText edtNama, edtTelp, edtAlamat, edtProv, edtKabkota, edtKecamatan, edtKodepos;
    private CardView btnPesan;
    private AsyncHttpClient asyncHttpClient;
    private List<Keranjang> keranjangList;
    private List<Orders> ordersList;

    private Spinner spnProv, spnKabkota, spnKec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        prefHelper.init(this);

        edtNama = (EditText) findViewById(R.id.edt_fullname);
        edtTelp = (EditText) findViewById(R.id.edt_phone);
        edtAlamat = (EditText) findViewById(R.id.edt_alamat);
        btnPesan = (CardView) findViewById(R.id.btnSubmit);
        spnProv = (Spinner) findViewById(R.id.spn_prov);
        spnKabkota = (Spinner) findViewById(R.id.spn_kabkota);
        spnKec = (Spinner) findViewById(R.id.spn_kec);
        edtKodepos = (EditText) findViewById(R.id.edt_kodepos);

        setupCart();
        setupProvinsi();

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = edtNama.getText().toString().trim();
                String telp = edtTelp.getText().toString().trim();
                String alamat = edtAlamat.getText().toString().trim();
                String prov = spnProv.getSelectedItem().toString();
                String kabkota = spnKabkota.getSelectedItem().toString();
                String kecamatan = spnKec.getSelectedItem().toString();
                String kodepos = edtKodepos.getText().toString().trim();
                if(nama.length() > 0 && telp.length() > 0 && alamat.length() > 0 && prov.length() > 0 && kabkota.length() > 0 && kecamatan.length() > 0 && kodepos.length() > 0) {
                    String fullAlamat = alamat + "\n" + prov + "\n" + kabkota + "\n" + kecamatan + "\n" + kodepos;
                    Log.d("[DEBUG]", fullAlamat);
                    order(nama, telp, fullAlamat);
                }
                else {
                    Toast.makeText(OrderActivity.this, "Harap isi semua field!", Toast.LENGTH_LONG).show();
                }
            }
        });

        spnProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Universal universal = (Universal) parent.getSelectedItem();
                Log.d("[DEBUG]", "Name: " + universal.getObject_name() + " - ID: " + universal.getId() + " - Parent: " + universal.getParent_id());
                setupKab(universal.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnKabkota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Universal universal = (Universal) parent.getSelectedItem();
                Log.d("[DEBUG]", "Name: " + universal.getObject_name() + " - ID: " + universal.getId() + " - Parent: " + universal.getParent_id());
                setupKec(universal.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnKec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Universal universal = (Universal) parent.getSelectedItem();
                Log.d("[DEBUG]", "Name: " + universal.getObject_name() + " - ID: " + universal.getId() + " - Parent: " + universal.getParent_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupProvinsi() {
        String SERVER_URL = "https://dev.farizdotid.com/api/daerahindonesia/provinsi";

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<Universal> universalList = new ArrayList<Universal>();
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                universalList = Parser.parseProvinsi(JSONResponse);
                if (universalList.size() > 0) {
                    ArrayAdapter<Universal> adapter = new ArrayAdapter<Universal>(OrderActivity.this, android.R.layout.simple_spinner_dropdown_item, universalList);
                    spnProv.setAdapter(adapter);
                }
                else {
                    Toast.makeText(OrderActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(OrderActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupKab(int id_provinsi) {
        String SERVER_URL = "https://dev.farizdotid.com/api/daerahindonesia/kota?id_provinsi=" + id_provinsi;

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<Universal> universalList = new ArrayList<Universal>();
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                universalList = Parser.parseKabupaten(JSONResponse);
                if (universalList.size() > 0) {
                    ArrayAdapter<Universal> adapter = new ArrayAdapter<Universal>(OrderActivity.this, android.R.layout.simple_spinner_dropdown_item, universalList);
                    spnKabkota.setAdapter(adapter);
                }
                else {
                    Toast.makeText(OrderActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(OrderActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupKec(int id_kota) {
        String SERVER_URL = "https://dev.farizdotid.com/api/daerahindonesia/kecamatan?id_kota=" + id_kota;

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<Universal> universalList = new ArrayList<Universal>();
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                universalList = Parser.parseKecamatan(JSONResponse);
                if (universalList.size() > 0) {
                    ArrayAdapter<Universal> adapter = new ArrayAdapter<Universal>(OrderActivity.this, android.R.layout.simple_spinner_dropdown_item, universalList);
                    spnKec.setAdapter(adapter);
                }
                else {
                    Toast.makeText(OrderActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(OrderActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void order(String nama, String telp, String alamat) {
        ordersList = new ArrayList<Orders>();
        final String orderCode = generateTrxCode();
        int total_price = prefHelper.countCartTotal();
        int user_id = Integer.parseInt(prefHelper.getUserdata("id"));

        String SERVER_URL = Config.SERVER_URL + "p=order";

        asyncHttpClient = new AsyncHttpClient();

        RequestParams param = new RequestParams();
        param.put("user_id", user_id);
        param.put("code", orderCode);
        param.put("buyer_name", nama);
        param.put("buyer_phone", telp);
        param.put("buyer_address", alamat);
        param.put("total", total_price);

        asyncHttpClient.post(SERVER_URL, param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                Parser parser = Parser.parseOrders(JSONResponse);
                if(parser.getCode() == 200) {
                    String SERVER_URLS = Config.SERVER_URL + "p=order_detail";

                    keranjangList = new ArrayList<>();
                    final int index = Integer.parseInt(prefHelper.getCartdata("cart_index", "int"));
                    for(int i = 1; i <= index; i++) {
                        int deleted = Integer.parseInt(prefHelper.getCartdata("cart_deleted_" + i, "int"));
                        if(deleted == 0) {
                            Keranjang keranjang = new Keranjang();
                            keranjang.setNama_barang(prefHelper.getCartdata("cart_nama_barang_" + i, "string"));
                            keranjang.setKode_barang(prefHelper.getCartdata("cart_kd_barang_" + i, "string"));
                            keranjang.setHarga_satuan(Integer.parseInt(prefHelper.getCartdata("cart_harga_barang_" + i, "int")));
                            keranjang.setJumlah_barang(Integer.parseInt(prefHelper.getCartdata("cart_jumlah_barang_" + i, "int")));

                            asyncHttpClient = new AsyncHttpClient();

                            RequestParams param = new RequestParams();
                            param.put("order_code", orderCode);
                            param.put("product_code", keranjang.getKode_barang());
                            param.put("product_price", keranjang.getHarga_satuan());
                            param.put("qty", keranjang.getJumlah_barang());

                            if(i == index) {
                                asyncHttpClient.post(SERVER_URLS, param, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        String JSONResponse = new String(responseBody);
                                        Log.d("[DEBUG]", JSONResponse);
                                        Parser parser = Parser.parseOrders(JSONResponse);
                                        if(parser.getCode() == 200) {
                                            Toast.makeText(OrderActivity.this, "Berhasil melakukan pemesanan, harap lakukan konfirmasi pembayaran pada menu profile!!", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(OrderActivity.this, OrderConfirmationActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            prefHelper.flushCart();
                                            startActivity(i);
                                            finish();
                                        }
                                        else if(parser.getCode() == 400) {
                                            Toast.makeText(OrderActivity.this, "Gagal memesan barang!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Toast.makeText(OrderActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else {
                                asyncHttpClient.post(SERVER_URLS, param, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        String JSONResponse = new String(responseBody);
                                        Log.d("[DEBUG]", JSONResponse);
                                        Parser parser = Parser.parseOrders(JSONResponse);
                                        if(parser.getCode() == 200) {
//                                Toast.makeText(OrderActivity.this, "Berhasil melakukan pemesanan, harap lakukan konfirmasi pembayaran pada menu profile!!", Toast.LENGTH_LONG).show();
                                        }
                                        else if(parser.getCode() == 400) {
                                            Toast.makeText(OrderActivity.this, "Gagal memesan barang!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Toast.makeText(OrderActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                }
                else if(parser.getCode() == 400) {
                    Toast.makeText(OrderActivity.this, "Gagal memesan barang!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(OrderActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupCart() {
        keranjangList = new ArrayList<>();
        int index = Integer.parseInt(prefHelper.getCartdata("cart_index", "int"));
        for(int i = 1; i <= index; i++) {
            int deleted = Integer.parseInt(prefHelper.getCartdata("cart_deleted_" + i, "int"));
            if(deleted == 0) {
                Keranjang keranjang = new Keranjang();
                keranjang.setNama_barang(prefHelper.getCartdata("cart_nama_barang_" + i, "string"));
                keranjang.setKode_barang(prefHelper.getCartdata("cart_kd_barang_" + i, "string"));
                keranjang.setHarga_satuan(Integer.parseInt(prefHelper.getCartdata("cart_harga_barang_" + i, "int")));
                keranjang.setJumlah_barang(Integer.parseInt(prefHelper.getCartdata("cart_jumlah_barang_" + i, "int")));
                keranjangList.add(keranjang);
            }
        }
    }

    public static String generateTrxCode() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
