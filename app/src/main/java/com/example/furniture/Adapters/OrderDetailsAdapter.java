package com.example.furniture.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.furniture.Activities.OrdersDetailActivity;
import com.example.furniture.Helpers.macroCollection;
import com.example.furniture.Helpers.prefHelper;
import com.example.furniture.Models.Keranjang;
import com.example.furniture.Models.OrderDetails;
import com.example.furniture.Models.Orders;
import com.example.furniture.Models.Products;
import com.example.furniture.R;

import java.util.List;

public class OrderDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<OrderDetails> dataList;

    public OrderDetailsAdapter(Context context, List<OrderDetails> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.template_list_detail, null);

        prefHelper.init(context);

        final OrderDetailsHolder cartHolder = new OrderDetailsHolder();
        cartHolder.txtNamaBarang = (TextView) convertView.findViewById(R.id.txtNamaBarang);
        cartHolder.txtHarga = (TextView) convertView.findViewById(R.id.txtHargaBarang);
        cartHolder.edtQty = (EditText) convertView.findViewById(R.id.edt_qty);

        final OrderDetails orderDetails = dataList.get(position);
        cartHolder.txtNamaBarang.setText(orderDetails.getProduct_name());
        int calc = (orderDetails.getProduct_price() * orderDetails.getQty());
        String harga = macroCollection.formatRupiah(calc);
        cartHolder.txtHarga.setText(harga);
        cartHolder.edtQty.setText(String.valueOf(orderDetails.getQty()));

        return convertView;
    }

    public class OrderDetailsHolder {
        private TextView txtNamaBarang, txtHarga;
        private EditText edtQty;
    }
}
