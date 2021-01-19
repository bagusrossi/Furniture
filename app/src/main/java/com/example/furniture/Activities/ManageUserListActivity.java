package com.example.furniture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.furniture.Adapters.UsersAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Models.Users;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class ManageUserListActivity extends AppCompatActivity {

    private int level;
    private ListView lvUser;
    private AsyncHttpClient asyncHttpClient;
    private RequestParams param = new RequestParams();
    private List<Users> usersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageuserlist);

        Intent i = getIntent();
        String menuContext = i.getStringExtra("context");

        if(menuContext.equals("tukang")) {
            level = 1;
        }
        else if(menuContext.equals("customer")){
            level = 0;
        }

        lvUser = (ListView) findViewById(R.id.lv_user);

        fetchData(level);
    }

    private void fetchData(int level) {
        Log.d("[DEBUG]", String.valueOf(level));
        String SERVER_URL = Config.SERVER_URL + "p=fetchuser";

        asyncHttpClient = new AsyncHttpClient();

        param.put("level", level);

        asyncHttpClient.post(SERVER_URL, param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                usersList = Parser.parseUsersList(JSONResponse);
                if (usersList.size() > 0) {
                    UsersAdapter usersAdapter = new UsersAdapter(ManageUserListActivity.this, usersList);
                    usersAdapter.notifyDataSetChanged();
                    lvUser.setAdapter(usersAdapter);
                }
                else {
                    Toast.makeText(ManageUserListActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ManageUserListActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData(level);
    }
}
