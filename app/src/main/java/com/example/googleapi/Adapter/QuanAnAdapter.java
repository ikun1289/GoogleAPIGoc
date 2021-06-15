package com.example.googleapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.R;

import java.util.List;

public class QuanAnAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<QuanAn> quanAnList;

    public QuanAnAdapter(Context context, int layout, List<QuanAn> quanAnList) {
        this.context = context;
        this.layout = layout;
        this.quanAnList = quanAnList;
    }

    @Override
    public int getCount() {
        return quanAnList.size();
    }

    @Override
    public Object getItem(int position) {
        return quanAnList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;//tạm thời chưa sài
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);

        TextView txtTenQuan = convertView.findViewById(R.id.txtTenQuan);
        TextView txtGioHoatDong = convertView.findViewById(R.id.txtGioHoatDong);
        TextView txtMoTa = convertView.findViewById(R.id.txtMoTa);
        ImageView imageView = convertView.findViewById(R.id.quananImg);

        //gán
        QuanAn quanAn = quanAnList.get(position);
        txtTenQuan.setText(quanAn.TenQuan);
        txtGioHoatDong.setText(quanAn.GioMoCua + " - " +quanAn.GioDongCua);
        txtMoTa.setText(quanAn.MoTa);
        Glide.with(context).load(quanAn.imgQuanAn).placeholder(R.drawable.foodimg).into(imageView);

        return convertView;

    }
}
