package com.example.furniture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Users;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    private CardView btnUpdate, btnBack, btnLogout, btnPesanan, btnKonfirmasi, btnKomplain;
    private EditText edtUsername, edtPassword, edtPhone, edtEmail, edtFullname;
    private String username, password, phone, email, fullname;
    private AsyncHttpClient asyncHttpClient;
    private Users user;
    private RequestParams param = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefHelper.init(ProfileActivity.this);

        Intent i = getIntent();

        user = new Users();
        user.setId(i.getIntExtra("id", 0));
        user.setLevel(i.getIntExtra("level", 0));
        user.setUsername(i.getStringExtra("username"));
        user.setFullname(i.getStringExtra("fullname"));
        user.setEmail(i.getStringExtra("email"));
        user.setPassword(i.getStringExtra("password"));
        user.setPhone(i.getStringExtra("phone"));
        user.setCreated_date(i.getStringExtra("created_date"));

        btnUpdate = (CardView) findViewById(R.id.btnUpdate);
        btnBack = (CardView) findViewById(R.id.btnBack);
        btnLogout = (CardView) findViewById(R.id.btnLogout);
        btnPesanan = (CardView) findViewById(R.id.btnPesanan);
        btnKonfirmasi = (CardView) findViewById(R.id.btnKonfirmasi);
        btnKomplain = (CardView) findViewById(R.id.btnKomplain);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtEmail  = (EditText) findViewById(R.id.edt_email);
        edtFullname = (EditText) findViewById(R.id.edt_fullname);

        int userId = Integer.parseInt(prefHelper.getUserdata("id"));
        if(user.getId() != userId) {
            btnKonfirmasi.setVisibility(View.INVISIBLE);
            btnPesanan.setVisibility(View.INVISIBLE);
        }

        edtUsername.setText(user.getUsername());
        edtPassword.setText(user.getPassword());
        edtFullname.setText(user.getFullname());
        edtPhone.setText(user.getPhone());
        edtEmail.setText(user.getEmail());

        btnPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, HistoryActivity.class);
                i.putExtra("mode", "history");
                startActivity(i);
            }
        });

        btnKomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, ComplaintsListActivity.class);
                i.putExtra("mode", "complaint");
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefHelper.flushCart();
                prefHelper.flushSession();
                Toast.makeText(ProfileActivity.this, "Anda berhasil keluar!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, PaymentActivity.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
                phone = edtPhone.getText().toString().trim();
                email = edtEmail.getText().toString().trim();
                fullname = edtFullname.getText().toString().trim();

                if(username.length() > 0 && password.length() > 0 && phone.length() > 0 && email.length() > 0 && fullname.length() > 0) {
                    String SERVER_URL = Config.SERVER_URL + "p=updateuser";

                    asyncHttpClient = new AsyncHttpClient();

                    param.put("id", user.getId());
                    param.put("username", username);
                    param.put("fullname", fullname);
                    param.put("email", email);
                    param.put("password", password);
                    param.put("phone", phone);

                    asyncHttpClient.post(ProfileActivity.this, SERVER_URL, param, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String JSONResponse = new String(responseBody);
                            Log.d("[DEBUG]", JSONResponse);
                            Parser parser = Parser.parseUsers(JSONResponse);
                            if(parser.getCode() == 200) {
                                Toast.makeText(ProfileActivity.this, "Berhasil update!", Toast.LENGTH_LONG).show();

                                //Setup prefHelper saat baru login, assign semua var user kedalam shared preferences
                                prefHelper.setUserdata("username", username);
                                prefHelper.setUserdata("fullname", fullname);
                                prefHelper.setUserdata("email", email);
                                prefHelper.setUserdata("password", password);
                                prefHelper.setUserdata("phone", phone);

                                finish();
                            }
                            else if(parser.getCode() == 400) {
                                Toast.makeText(ProfileActivity.this, "Gagal update!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("[DEBUG]", "Failure");
                        }
                    });
                }
                else {
                    Toast.makeText(ProfileActivity.this, "Harap isi semua field!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
