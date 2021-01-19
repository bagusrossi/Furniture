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

public class EmployeeActivity extends AppCompatActivity {

    private LinearLayout menuBarangControl, menuPengiriman, menuKomplain;
    private ImageView imgCart, imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        prefHelper.init(EmployeeActivity.this);

        menuBarangControl = (LinearLayout) findViewById(R.id.menuBarang);
        imgCart = (ImageView) findViewById(R.id.imgCart);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        menuPengiriman = (LinearLayout) findViewById(R.id.menuPengiriman);
        menuKomplain = (LinearLayout) findViewById(R.id.menuKomplain);

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeActivity.this, ProfileActivity.class);
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

        menuBarangControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeActivity.this, ManageProductActivity.class);
                startActivity(i);
            }
        });
        menuPengiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeActivity.this, EmployeeShipmentActivity.class);
                startActivity(i);
            }
        });

        menuKomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeActivity.this, ComplaintsListActivity.class);
                startActivity(i);
            }
        });
    }
}
