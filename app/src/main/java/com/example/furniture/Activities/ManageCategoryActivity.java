package com.example.furniture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.furniture.Adapters.CategoriesAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Models.Categories;
import com.example.furniture.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class ManageCategoryActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ListView listView;
    private AsyncHttpClient asyncHttpClient;
    private RequestParams param = new RequestParams();
    private List<Categories> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managecategory);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.lvCategory);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageCategoryActivity.this, ManageCategoryCRUDActivity.class);
                i.putExtra("pageMode", "add");
                startActivity(i);
            }
        });
    }

    private void setupContent() {
        String SERVER_URL = Config.SERVER_URL + "p=fetchcategory";

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                categoriesList = Parser.parseCategoriesList(JSONResponse);
                if (categoriesList.size() > 0) {
                    CategoriesAdapter categoriesAdapter = new CategoriesAdapter(ManageCategoryActivity.this, categoriesList, "category");
                    categoriesAdapter.notifyDataSetChanged();
                    listView.setAdapter(categoriesAdapter);
                }
                else {
                    Toast.makeText(ManageCategoryActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ManageCategoryActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupContent();
    }
}
