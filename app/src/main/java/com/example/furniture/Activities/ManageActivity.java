package com.example.furniture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.R;

import androidx.appcompat.app.AppCompatActivity;

public class ManageActivity extends AppCompatActivity {

    private LinearLayout menuUserControl, menuBarangControl, menuCategoryControl, menuPembayaran, menuLaporan, menuKomplain;
    private ImageView imgCart, imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        prefHelper.init(ManageActivity.this);

        menuUserControl = (LinearLayout) findViewById(R.id.menuUser);
        menuCategoryControl = (LinearLayout) findViewById(R.id.menuKategori);
        menuBarangControl = (LinearLayout) findViewById(R.id.menuBarang);
        imgCart = (ImageView) findViewById(R.id.imgCart);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        menuPembayaran = (LinearLayout) findViewById(R.id.menuKonfirmasi);
        menuLaporan = (LinearLayout) findViewById(R.id.menuLaporan);
        menuKomplain = (LinearLayout) findViewById(R.id.menuKomplain);

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageActivity.this, ProfileActivity.class);
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

        menuUserControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageActivity.this, ManageUserActivity.class);
                startActivity(i);
            }
        });

        menuCategoryControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageActivity.this, ManageCategoryActivity.class);
                startActivity(i);
            }
        });

        menuBarangControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageActivity.this, ManageProductActivity.class);
                startActivity(i);
            }
        });

        menuPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageActivity.this, ManagePaymentActivity.class);
                startActivity(i);
            }
        });

        menuLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageActivity.this, ManageReportActivity.class);
                startActivity(i);
            }
        });

        menuKomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageActivity.this, ComplaintsListActivity.class);
                startActivity(i);
            }
        });
    }
}
