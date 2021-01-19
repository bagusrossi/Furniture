package com.example.furniture.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.furniture.Activities.ProfileActivity;
import com.example.furniture.Models.Users;
import com.example.furniture.R;

import java.util.List;

import androidx.cardview.widget.CardView;

public class UsersAdapter extends BaseAdapter {
    private Context context;
    private List<Users> dataList;

    public UsersAdapter(Context context, List<Users> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.item_users, null);

        UserHolder userHolder = new UserHolder();
        userHolder.name = convertView.findViewById(R.id.txtNama);
        userHolder.registered = convertView.findViewById(R.id.txtRegistered);
        userHolder.cardUser = convertView.findViewById(R.id.card);

        final Users user = dataList.get(position);
        userHolder.name.setText(user.getFullname());
        Log.d("[DEBUG]", user.getCreated_date());
        String registeredStr = "Terdaftar sejak: " + user.getCreated_date();
        userHolder.registered.setText(registeredStr);
        userHolder.cardUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(context, ProfileActivity.class);
                i.putExtra("id", user.getId());
                i.putExtra("level", user.getLevel());
                i.putExtra("username", user.getUsername());
                i.putExtra("fullname", user.getFullname());
                i.putExtra("email", user.getEmail());
                i.putExtra("password", user.getPassword());
                i.putExtra("phone", user.getPhone());
                i.putExtra("created_date", user.getCreated_date());
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public class UserHolder {
        private TextView name, registered;
        private CardView cardUser;
    }
}
