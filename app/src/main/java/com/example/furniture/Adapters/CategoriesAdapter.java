package com.example.furniture.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.furniture.Activities.ManageCategoryCRUDActivity;
import com.example.furniture.Activities.ManageProductListActivity;
import com.example.furniture.Configs.Config;
import com.example.furniture.Models.Categories;
import com.example.furniture.R;

import java.util.List;

import androidx.cardview.widget.CardView;

public class CategoriesAdapter extends BaseAdapter {

    private Context context;
    private List<Categories> dataList;
    private String mode;

    public CategoriesAdapter(Context context, List<Categories> dataList, String mode) {
        this.context = context;
        this.dataList = dataList;
        this.mode = mode;
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
        convertView = layoutInflater.inflate(R.layout.item_categories, null);

        CategoryHolder categoryHolder = new CategoryHolder();
        categoryHolder.name = convertView.findViewById(R.id.txtCategoryName);
        categoryHolder.icon = convertView.findViewById(R.id.imgCategoryIcon);
        categoryHolder.cardCat = convertView.findViewById(R.id.card);

        final Categories categories = dataList.get(position);
        categoryHolder.name.setText(categories.getCategory_name());
        Glide.with(context)
                .load(Config.CATEGORY_IMAGE_URL + categories.getCategory_icon())
                .into(categoryHolder.icon);

        Log.d("[DEBUG]", "cat index adapter: " + categories.getCategory_index());
        categoryHolder.cardCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                if(mode.equals("product")) {
                    i = new Intent(context, ManageProductListActivity.class);
                    i.putExtra("id", categories.getId());
                    i.putExtra("category_name", categories.getCategory_name());
                    i.putExtra("category_code", categories.getCategory_code());
                    i.putExtra("category_index", categories.getCategory_index());
                }
                else if(mode.equals("category")) {
                    i = new Intent(context, ManageCategoryCRUDActivity.class);
                    i.putExtra("id", categories.getId());
                    i.putExtra("category_name", categories.getCategory_name());
                    i.putExtra("category_icon", categories.getCategory_icon());
                    i.putExtra("category_code", categories.getCategory_code());
                    i.putExtra("category_index", categories.getCategory_index());
                    i.putExtra("pageMode", "edit");
                }
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public class CategoryHolder {
        private TextView name;
        private ImageView icon;
        private CardView cardCat;
    }
}
