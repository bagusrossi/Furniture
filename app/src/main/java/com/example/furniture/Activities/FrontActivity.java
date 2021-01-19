package com.example.furniture.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Users;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class FrontActivity extends AppCompatActivity {

    private CardView btnLogin, btnRegister;
    private String username, password;
    private AsyncHttpClient asyncHttpClient;
    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        prefHelper.init(FrontActivity.this);

        btnLogin = (CardView) findViewById(R.id.btnLogin);
        btnRegister = (CardView) findViewById(R.id.btnRegister);

        String sessionCheck = prefHelper.getUserdata("id");
        Log.d("[DEBUG]", "sessioncheck");

        if(!sessionCheck.equals("0")) {
            username = prefHelper.getUserdata("username");
            password = prefHelper.getUserdata("password");

            login(username, password);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FrontActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FrontActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    public void login(String username, String password) {
        usersList = new ArrayList<Users>();

        String SERVER_URL = Config.SERVER_URL + "p=login";

        asyncHttpClient = new AsyncHttpClient();

        RequestParams param = new RequestParams();
        param.put("username", username);
        param.put("password", password);

        asyncHttpClient.post(SERVER_URL, param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                Parser parser = Parser.parseUsers(JSONResponse);
                if(parser.getCode() == 200) {
                    Log.d("[DEBUG]", String.valueOf(parser.getCode()));
                    usersList = parser.getUsersList();
                    Log.d("[DEBUG]", String.valueOf(usersList.size()));
                    if (usersList.size() > 0) {
                        Users users = usersList.get(0);

                        //Setup prefHelper saat baru login, assign semua var user kedalam shared preferences
                        prefHelper.setUserdata("id", String.valueOf(users.getId()));
                        prefHelper.setUserdata("username", users.getUsername());
                        prefHelper.setUserdata("fullname", users.getFullname());
                        prefHelper.setUserdata("email", users.getEmail());
                        prefHelper.setUserdata("password", users.getPassword());
                        prefHelper.setUserdata("phone", users.getPhone());
                        prefHelper.setUserdata("level", String.valueOf(users.getLevel()));
                        prefHelper.setUserdata("created_date", users.getCreated_date());

                        Toast.makeText(FrontActivity.this, "Berhasil masuk..", Toast.LENGTH_LONG).show();
                        Intent i;
                        if(users.getLevel() == 2) {
                            i = new Intent(FrontActivity.this, ManageActivity.class);
                        }
                        else if(users.getLevel() == 1) {
                            i = new Intent(FrontActivity.this, EmployeeActivity.class);
                        }
                        else {
                            i = new Intent(FrontActivity.this, MainActivity.class);
                        }
                        startActivity(i);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                    }
                }
                else if(parser.getCode() == 400) {
                    Toast.makeText(FrontActivity.this, "Gagal masuk, harap periksa kembali username dan password!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(FrontActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}