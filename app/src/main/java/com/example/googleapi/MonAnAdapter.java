package com.example.googleapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MonAnAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<MonAn> monAnList;

    public MonAnAdapter(Context context, int layout, List<MonAn> monAnList) {
        this.context = context;
        this.layout = layout;
        this.monAnList = monAnList;
    }

    @Override
    public int getCount() {
        return monAnList.size();
    }

    @Override
    public Object getItem(int position) {
        return monAnList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);

        TextView txtTenMonAn = convertView.findViewById(R.id.txtTenMonAn);
        TextView txtMoTaMonAn = convertView.findViewById(R.id.txtMoTaMonAn);
        TextView txtGia = convertView.findViewById(R.id.txtGia);

        //gán
        MonAn monAn = monAnList.get(position);
        txtTenMonAn.setText(monAn.TenMonAn);
        txtMoTaMonAn.setText(monAn.MoTa);
        txtGia.setText(monAn.Gia+" đồng");


        return convertView;
    }
}
