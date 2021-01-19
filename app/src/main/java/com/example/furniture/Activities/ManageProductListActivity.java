package com.example.furniture.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniture.Adapters.CategoriesAdapter;
import com.example.furniture.Adapters.ProductsAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Categories;
import com.example.furniture.Models.Products;
import com.example.furniture.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class ManageProductListActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private GridView listView;
    private AsyncHttpClient asyncHttpClient;
    private RequestParams param = new RequestParams();
    private List<Products> productsList;
    private int id, category_index;
    private TextView txtTagline;
    private String category_name, category_code, userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageproductlist);

        prefHelper.init(this);

        userLevel = prefHelper.getUserdata("level");

        Intent i = getIntent();
        id = i.getIntExtra("id", 0);
        category_name = i.getStringExtra("category_name");
        category_code = i.getStringExtra("category_code");
        category_index = i.getIntExtra("category_index", 0);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        txtTagline = (TextView) findViewById(R.id.txtTagline);
        listView = (GridView) findViewById(R.id.lvProduct);

        String strTagline = "Result from category [" + category_name + "]";
        txtTagline.setText(strTagline);

        if(userLevel.equals("0")) {
            fab.hide();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageProductListActivity.this, ManageProductCRUDActivity.class);
                i.putExtra("pageMode", "add");
                i.putExtra("category_name", category_name);
                i.putExtra("category_id", id);
                i.putExtra("category_code", category_code);
                i.putExtra("category_index", category_index);
                startActivityForResult(i, 2);
            }
        });

        setupContent(id);
    }

    private void setupContent(final int id) {
        String SERVER_URL = Config.SERVER_URL + "p=fetchproductcat";

        asyncHttpClient = new AsyncHttpClient();

        param.put("category_id", id);

        asyncHttpClient.post(SERVER_URL, param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                productsList = Parser.parseProductsList(JSONResponse);
                if (productsList.size() > 0) {
                    Categories categories = new Categories();
                    categories.setId(id);
                    categories.setCategory_name(category_name);
                    ProductsAdapter productsAdapter = new ProductsAdapter(ManageProductListActivity.this, productsList, userLevel, categories);
                    productsAdapter.notifyDataSetChanged();
                    listView.setAdapter(productsAdapter);
                }
                else {
                    Toast.makeText(ManageProductListActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ManageProductListActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupContent(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2) {
            Log.d("[DEBUG]", "oldVar: " + category_index);
            Log.d("[DEBUG]", "new: " + data.getIntExtra("category_index", 0));
            category_index = data.getIntExtra("category_index", 0);
            Log.d("[DEBUG","newVar: " + category_index);
        }
    }
}
