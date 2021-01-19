package com.example.furniture.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class ResultActivity extends AppCompatActivity {

    private EditText edtSearch;
    private Context context;
    private ListView listView;
    private AsyncHttpClient asyncHttpClient;
    private RequestParams param = new RequestParams();
    private List<Products> productsList;
    private String userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        context = this;
        prefHelper.init(context);

        edtSearch = findViewById(R.id.input_search);
        listView = (ListView) findViewById(R.id.lvProduct);

        Intent i = getIntent();
        String q = i.getStringExtra("q");

        userLevel = prefHelper.getUserdata("level");

        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edtSearch.getRight() - edtSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        String q = edtSearch.getText().toString().trim();
                        if(q.length() > 0) {
                            setupContent(q);
                        }
                        else {
                            Toast.makeText(context, "Harap isi kata kunci pencarian!", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        edtSearch.setText(q);

        setupContent(q);
    }

    public void setupContent(String query) {
        String SERVER_URL = Config.SERVER_URL + "p=searchproduct&q=" + query;

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                productsList = Parser.parseProductsList(JSONResponse);
                if (productsList.size() > 0) {
                    Categories categories = new Categories();
                    ProductsAdapter productsAdapter = new ProductsAdapter(context, productsList, "0", categories);
                    productsAdapter.notifyDataSetChanged();
                    listView.setAdapter(productsAdapter);
                }
                else {
                    Toast.makeText(context, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
