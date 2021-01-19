package com.example.furniture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.furniture.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ManageUserActivity extends AppCompatActivity {

    private LinearLayout menuKontrolTukang, menuKontrolUser;
    private CardView btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageuser);

        menuKontrolTukang = (LinearLayout) findViewById(R.id.menuKontrolTukang);
        menuKontrolUser = (LinearLayout) findViewById(R.id.menuKontrolUser);
        btnRegister = (CardView) findViewById(R.id.btnRegister);

        menuKontrolTukang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageUserActivity.this, ManageUserListActivity.class);
                i.putExtra("context", "tukang");
                startActivity(i);
            }
        });

        menuKontrolUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageUserActivity.this, ManageUserListActivity.class);
                i.putExtra("context", "customer");
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageUserActivity.this, ManageCreateUserActivity.class);
                startActivity(i);
            }
        });
    }
}
