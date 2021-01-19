package com.example.furniture.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.furniture.Activities.ComplaintsAddActivity;
import com.example.furniture.Activities.EmployeeShipmentDetailActivity;
import com.example.furniture.Activities.ManagePaymentDetailActivity;
import com.example.furniture.Activities.OrdersDetailActivity;
import com.example.furniture.Activities.SubmitPaymentActivity;
import com.example.furniture.Helpers.macroCollection;
import com.example.furniture.Models.Orders;
import com.example.furniture.R;

import java.util.List;

import androidx.cardview.widget.CardView;

public class OrdersAdapter extends BaseAdapter {
    private Context context;
    private List<Orders> dataList;
    private String mode;

    public OrdersAdapter(Context context, List<Orders> dataList, String mode) {
        this.context = context;
        this.dataList = dataList;
        this.mode = mode;
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
        convertView = layoutInflater.inflate(R.layout.template_list_history, null);

        OrdersHolder ordersHolder = new OrdersHolder();
        ordersHolder.txtHarga = (TextView) convertView.findViewById(R.id.txtHargaTotal);
        ordersHolder.txtKode = (TextView) convertView.findViewById(R.id.txtKodeTransaksi);
        ordersHolder.txtTanggal = (TextView) convertView.findViewById(R.id.txtTanggal);
        ordersHolder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
        ordersHolder.baseLayout = (LinearLayout) convertView.findViewById(R.id.layoutPembelian);

        final Orders order = dataList.get(position);
        ordersHolder.txtHarga.setText(macroCollection.formatRupiah(order.getTotal()));
        ordersHolder.txtKode.setText(order.getCode());
        ordersHolder.txtTanggal.setText(order.getCreated_on());
        ordersHolder.txtStatus.setText(getStatus(order.getStatus()));
        ordersHolder.baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                if(mode.equals("history")) {
                    i = new Intent(context, OrdersDetailActivity.class);
                }
                else if(mode.equals("payment")) {
                    i = new Intent(context, SubmitPaymentActivity.class);
                }
                else if(mode.equals("manage")) {
                    i = new Intent(context, ManagePaymentDetailActivity.class);
                }
                else if(mode.equals("shipment")) {
                    i = new Intent(context, EmployeeShipmentDetailActivity.class);
                }
                else if(mode.equals("complaint")) {
                    i = new Intent(context, ComplaintsAddActivity.class);
                }
                i.putExtra("code", order.getCode());
                i.putExtra("buyer_name", order.getBuyer_name());
                i.putExtra("buyer_phone", order.getBuyer_phone());
                i.putExtra("buyer_address", order.getBuyer_address());
                i.putExtra("total", order.getTotal());
                i.putExtra("status", order.getStatus());
                i.putExtra("created_on", order.getCreated_on());
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public String getStatus(int status) {
        String strStatus = "";

        switch(status) {
            case 0: {
                strStatus =  "MENUNGGU PEMBAYARAN";
                break;
            }
            case 1: {
                strStatus =  "PEMBAYARAN TERVERIFIKASI";
                break;
            }
            case 2: {
                strStatus =  "DIKIRIM";
                break;
            }
            case 3: {
                strStatus = "PESANAN DITERIMA";
                break;
            }
        }

        return strStatus;
    }

    public class OrdersHolder {
        private TextView txtTanggal, txtKode, txtHarga, txtStatus;
        private LinearLayout baseLayout;
    }
}
