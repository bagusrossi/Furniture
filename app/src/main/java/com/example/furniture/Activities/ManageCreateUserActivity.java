package com.example.furniture.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

public class ManageCreateUserActivity extends AppCompatActivity {

    private CardView btnRegister;
    private EditText edtUsername, edtPassword, edtPhone, edtEmail, edtFullname;
    private Spinner spnLevel;
    private String username, password, phone, email, fullname;
    private int level;
    private AsyncHttpClient asyncHttpClient;
    private RequestParams param = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_managecreateuser);

        btnRegister = (CardView) findViewById(R.id.btnRegister);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtEmail  = (EditText) findViewById(R.id.edt_email);
        edtFullname = (EditText) findViewById(R.id.edt_fullname);
        spnLevel = (Spinner) findViewById(R.id.spinner_jabatan);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
                phone = edtPhone.getText().toString().trim();
                email = edtEmail.getText().toString().trim();
                fullname = edtFullname.getText().toString().trim();
                level = spnLevel.getSelectedItemPosition();

                if(username.length() > 0 && password.length() > 0 && phone.length() > 0 && email.length() > 0 && fullname.length() > 0) {
                    String SERVER_URL = Config.SERVER_URL + "p=register";

                    asyncHttpClient = new AsyncHttpClient();

                    param.put("username", username);
                    param.put("fullname", fullname);
                    param.put("email", email);
                    param.put("password", password);
                    param.put("phone", phone);
                    param.put("level", level);

                    asyncHttpClient.post(ManageCreateUserActivity.this, SERVER_URL, param, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String JSONResponse = new String(responseBody);
                            Log.d("[DEBUG]", JSONResponse);
                            Parser parser = Parser.parseUsers(JSONResponse);
                            if(parser.getCode() == 200) {
                                Toast.makeText(ManageCreateUserActivity.this, "Berhasil daftar!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else if(parser.getCode() == 400) {
                                Toast.makeText(ManageCreateUserActivity.this, "Gagal registrasi!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(ManageCreateUserActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("[DEBUG]", "Failure");
                        }
                    });
                }
                else {
                    Toast.makeText(ManageCreateUserActivity.this, "Harap isi semua field!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
