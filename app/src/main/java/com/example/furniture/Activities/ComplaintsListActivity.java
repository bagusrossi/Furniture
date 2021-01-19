package com.example.furniture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.furniture.Adapters.CategoriesAdapter;
import com.example.furniture.Adapters.ComplaintsAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Categories;
import com.example.furniture.Models.Complaints;
import com.example.furniture.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class ComplaintsListActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ListView listView;
    private AsyncHttpClient asyncHttpClient;
    private RequestParams param = new RequestParams();
    private List<Complaints> complaintsList;
    private int userLevel, user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintslist);

        prefHelper.init(ComplaintsListActivity.this);

        userLevel = Integer.parseInt(prefHelper.getUserdata("level"));
        user_id = Integer.parseInt(prefHelper.getUserdata("id"));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.lvComplaint);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ComplaintsListActivity.this, HistoryActivity.class);
                i.putExtra("mode", "complaint");
                startActivity(i);
            }
        });
    }

    private void setupContent() {
        String SERVER_URL = "";
        if(userLevel == 0) {
            SERVER_URL = Config.SERVER_URL + "p=fetchmycomplaint&user_id=" + user_id;
        }
        else if(userLevel == 2) {
            SERVER_URL = Config.SERVER_URL + "p=fetchcomplaint&status=0";
        }
        else if(userLevel == 1) {
            SERVER_URL = Config.SERVER_URL + "p=fetchcomplaint&status=1";
        }

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                Parser parser = Parser.parseComplaints(JSONResponse);
                complaintsList = parser.getComplaintsList();
                if (complaintsList != null) {
                    ComplaintsAdapter complaintsAdapter = new ComplaintsAdapter(ComplaintsListActivity.this, complaintsList, "history");
                    complaintsAdapter.notifyDataSetChanged();
                    listView.setAdapter(complaintsAdapter);
                }
                else {
                    Toast.makeText(ComplaintsListActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ComplaintsListActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupContent();
    }
}
