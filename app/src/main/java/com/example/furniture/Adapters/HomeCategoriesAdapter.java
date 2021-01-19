package com.example.furniture.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.furniture.Activities.ManageCategoryCRUDActivity;
import com.example.furniture.Activities.ManageProductListActivity;
import com.example.furniture.Configs.Config;
import com.example.furniture.Models.Categories;
import com.example.furniture.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HomeCategoriesAdapter extends RecyclerView.Adapter<HomeCategoriesAdapter.MyViewHolder> {
    private List<Categories> categoriesList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        CardView card;
        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txtCategoryName);
            icon = view.findViewById(R.id.imgCategoryIcon);
            card = view.findViewById(R.id.card);
        }
    }

    public HomeCategoriesAdapter(Context context, List<Categories> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categories_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Categories categories = categoriesList.get(position);
        holder.title.setText(categories.getCategory_name());
        Glide.with(context)
                .load(Config.CATEGORY_IMAGE_URL + categories.getCategory_icon())
                .into(holder.icon);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ManageProductListActivity.class);
                i.putExtra("id", categories.getId());
                i.putExtra("category_name", categories.getCategory_name());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }
}
