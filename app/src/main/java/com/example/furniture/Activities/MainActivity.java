package com.example.furniture.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniture.Adapters.CategoriesAdapter;
import com.example.furniture.Adapters.HomeCategoriesAdapter;
import com.example.furniture.Adapters.ProductsAdapter;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.Parser;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Categories;
import com.example.furniture.Models.Products;
import com.example.furniture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtMore;
    private Context mCtx;
    private RecyclerView rvCategory;
    private AsyncHttpClient asyncHttpClient;
    private GridView listView;
    private List<Products> productsList;
    private String userLevel;
    private ImageView imgCart, imgProfile;
    private EditText edtSearch;
    private RequestParams param = new RequestParams();
    private List<Categories> categoriesList = new ArrayList<>();
    private HomeCategoriesAdapter homeCategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCtx = this;

        prefHelper.init(mCtx);

        userLevel = prefHelper.getUserdata("level");

        txtMore = (TextView) findViewById(R.id.txtMore);
        rvCategory = (RecyclerView) findViewById(R.id.recyclerCategory);
        listView = (GridView) findViewById(R.id.lvProduct);
        imgCart = (ImageView) findViewById(R.id.imgCart);
        edtSearch = (EditText)  findViewById(R.id.input_search);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(i);
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                i.putExtra("id", Integer.parseInt(prefHelper.getUserdata("id")));
                i.putExtra("level", Integer.parseInt(prefHelper.getUserdata("level")));
                i.putExtra("username", prefHelper.getUserdata("username"));
                i.putExtra("fullname", prefHelper.getUserdata("fullname"));
                i.putExtra("email", prefHelper.getUserdata("email"));
                i.putExtra("password", prefHelper.getUserdata("password"));
                i.putExtra("phone", prefHelper.getUserdata("phone"));
                i.putExtra("created_date", prefHelper.getUserdata("created_date"));
                startActivity(i);
            }
        });

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
                            Intent i = new Intent(mCtx, ResultActivity.class);
                            i.putExtra("q", q);
                            mCtx.startActivity(i);
                        }
                        else {
                            Toast.makeText(mCtx, "Harap isi kata kunci pencarian!", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        setupCategory();
        setupContent();
    }

    private void setupContent() {
        String SERVER_URL = Config.SERVER_URL + "p=fetchproduct";

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                productsList = Parser.parseProductsList(JSONResponse);
                if (productsList.size() > 0) {
                    Categories categories = new Categories();
                    categories.setId(0);
                    categories.setCategory_name("base");
                    ProductsAdapter productsAdapter = new ProductsAdapter(MainActivity.this, productsList, userLevel, categories);
                    productsAdapter.notifyDataSetChanged();
                    listView.setAdapter(productsAdapter);
                }
                else {
                    Toast.makeText(MainActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupCategory() {
        String SERVER_URL = Config.SERVER_URL + "p=fetchcategory";

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(SERVER_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONResponse = new String(responseBody);
                Log.d("[DEBUG]", JSONResponse);
                categoriesList = Parser.parseCategoriesList(JSONResponse);
                if (categoriesList.size() > 0) {
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    homeCategoriesAdapter = new HomeCategoriesAdapter(MainActivity.this, categoriesList);
                    rvCategory.setLayoutManager(mLayoutManager);
                    rvCategory.setItemAnimator(new DefaultItemAnimator());
                    rvCategory.setAdapter(homeCategoriesAdapter);
                    homeCategoriesAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this, "Data kosong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


}