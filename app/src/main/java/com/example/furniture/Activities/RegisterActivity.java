package com.example.furniture.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterActivity extends AppCompatActivity {

    private CardView btnRegister;
    private EditText edtUsername, edtPassword, edtPhone, edtEmail, edtFullname;
    private String username, password, phone, email, fullname;
    private AsyncHttpClient asyncHttpClient;
    private RequestParams param = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (CardView) findViewById(R.id.btnRegister);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtEmail  = (EditText) findViewById(R.id.edt_email);
        edtFullname = (EditText) findViewById(R.id.edt_fullname);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
                phone = edtPhone.getText().toString().trim();
                email = edtEmail.getText().toString().trim();
                fullname = edtFullname.getText().toString().trim();

                if(username.length() > 0 && password.length() > 0 && phone.length() > 0 && email.length() > 0 && fullname.length() > 0) {
                    String SERVER_URL = Config.SERVER_URL + "p=register";

                    asyncHttpClient = new AsyncHttpClient();

                    param.put("username", username);
                    param.put("fullname", fullname);
                    param.put("email", email);
                    param.put("password", password);
                    param.put("phone", phone);

                    asyncHttpClient.post(RegisterActivity.this, SERVER_URL, param, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String JSONResponse = new String(responseBody);
                            Log.d("[DEBUG]", JSONResponse);
                            Parser parser = Parser.parseUsers(JSONResponse);
                            if(parser.getCode() == 200) {
                                Toast.makeText(RegisterActivity.this, "Berhasil daftar, silahkan ke menu login!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else if(parser.getCode() == 400) {
                                Toast.makeText(RegisterActivity.this, "Gagal registrasi!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("[DEBUG]", "Failure");
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Harap isi semua field!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}