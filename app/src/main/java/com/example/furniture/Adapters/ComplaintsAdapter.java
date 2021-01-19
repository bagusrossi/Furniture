package com.example.furniture.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.furniture.Activities.ComplaintsDetailActivity;
import com.example.furniture.Models.Complaints;
import com.example.furniture.R;

import java.util.List;

import androidx.cardview.widget.CardView;

public class ComplaintsAdapter extends BaseAdapter {
    private Context context;
    private List<Complaints> dataList;
    private String mode;

    public ComplaintsAdapter(Context context, List<Complaints> dataList, String mode) {
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
        convertView = layoutInflater.inflate(R.layout.template_list_complaints, null);

        ComplaintsHolder complaintsHolder = new ComplaintsHolder();
        complaintsHolder.txtKode = (TextView) convertView.findViewById(R.id.txtKodeTransaksi);
        complaintsHolder.txtTanggal = (TextView) convertView.findViewById(R.id.txtTanggal);
        complaintsHolder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
        complaintsHolder.baseLayout = (CardView) convertView.findViewById(R.id.layoutPembelian);

        final Complaints complaints = dataList.get(position);

        complaintsHolder.txtKode.setText(complaints.getOrder_code());
        complaintsHolder.txtTanggal.setText(complaints.getCreated_on());
        complaintsHolder.txtStatus.setText(getStatus(complaints.getStatus()));

        complaintsHolder.baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ComplaintsDetailActivity.class);

                i.putExtra("id", complaints.getId());
                i.putExtra("order_code", complaints.getOrder_code());
                i.putExtra("user_id", complaints.getUser_id());
                i.putExtra("image", complaints.getImage());
                i.putExtra("information", complaints.getInformation());
                i.putExtra("status", complaints.getStatus());
                i.putExtra("created_on", complaints.getCreated_on());
                i.putExtra("buyer_name", complaints.getBuyer_name());
                i.putExtra("buyer_phone", complaints.getBuyer_phone());
                i.putExtra("buyer_address", complaints.getBuyer_address());
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public String getStatus(int status) {
        String strStatus = "";

        switch(status) {
            case 0: {
                strStatus =  "PENDING";
                break;
            }
            case 1: {
                strStatus =  "KOMPLAIN DI PROSES";
                break;
            }
            case 2: {
                strStatus =  "KOMPLAIN SELESAI";
                break;
            }
            case 3: {
                strStatus = "PESANAN DITERIMA";
                break;
            }
        }

        return strStatus;
    }

    public class ComplaintsHolder {
        private TextView txtTanggal, txtKode, txtStatus;
        private CardView baseLayout;
    }
}
