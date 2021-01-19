package com.example.furniture.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.furniture.Activities.EmployeeProductCRUDActivity;
import com.example.furniture.Activities.ManageProductCRUDActivity;
import com.example.furniture.Activities.ProductDetailActivity;
import com.example.furniture.Configs.Config;
import com.example.furniture.Helpers.macroCollection;
import com.example.furniture.Models.Categories;
import com.example.furniture.Models.Products;
import com.example.furniture.R;

import java.util.List;

import androidx.cardview.widget.CardView;

public class ProductsAdapter extends BaseAdapter {
    private Context context;
    private List<Products> dataList;
    private String userLevel;
    private Categories categories;

    public ProductsAdapter(Context context, List<Products> dataList, String userLevel, Categories categories) {
        this.context = context;
        this.dataList = dataList;
        this.userLevel = userLevel;
        this.categories = categories;
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
        convertView = layoutInflater.inflate(R.layout.item_products, null);

        ProductHolder productHolder = new ProductHolder();
        productHolder.name = convertView.findViewById(R.id.txtProductName);
        productHolder.price = convertView.findViewById(R.id.txtProductPrice);
        productHolder.image = convertView.findViewById(R.id.imgProduct);
        productHolder.cardProduct = convertView.findViewById(R.id.card);

        final Products product = dataList.get(position);
        productHolder.name.setText(product.getName());
        String price = macroCollection.formatRupiah(product.getPrice());
        productHolder.price.setText(price);
        Glide.with(context)
                .load(Config.PRODUCT_IMAGE_URL + product.getAttachment())
                .into(productHolder.image);
        productHolder.cardProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                if(userLevel.equals("0")) {
                    i = new Intent(context, ProductDetailActivity.class);
                }
                else if(userLevel.equals("1")) {
                    i = new Intent(context, EmployeeProductCRUDActivity.class);
                }
                else if(userLevel.equals("2")) {
                    i = new Intent(context, ManageProductCRUDActivity.class);
                }
                i.putExtra("id", product.getId());
                i.putExtra("category_id", product.getCategory_id());
                i.putExtra("code", product.getCode());
                i.putExtra("name", product.getName());
                i.putExtra("description", product.getDescription());
                i.putExtra("stock", product.getStock());
                i.putExtra("price", product.getPrice());
                i.putExtra("attachment", product.getAttachment());
                i.putExtra("pageMode", "edit");
                if(!userLevel.equals("0")) {
                    i.putExtra("category_name", categories.getCategory_name());
                    i.putExtra("category_code", categories.getCategory_code());
                }
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public class ProductHolder {
        private TextView name, price;
        private ImageView image;
        private CardView cardProduct;
    }
}
