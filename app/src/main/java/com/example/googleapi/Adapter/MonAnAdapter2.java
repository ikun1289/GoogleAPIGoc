package com.example.googleapi.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.googleapi.Models.MonAn;
import com.example.googleapi.R;

import java.util.List;

public class MonAnAdapter2 extends RecyclerView.Adapter<MonAnAdapter2.ViewHolder> {

    private List<MonAn> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public MonAnAdapter2(Context context, List<MonAn> data, ItemClickListener mItemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mClickListener = mItemClickListener;
        this.mData = data;
        this.context = context;
    }

    public void setData(List<MonAn> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.dong_monan, parent, false);
        return new ViewHolder(view, mClickListener);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonAn monAn = mData.get(position);
        if(monAn == null)
            return;

        holder.txtTenMonAn.setText(monAn.TenMonAn);
        holder.txtGia.setText(monAn.Gia+" đồng");
        //holder.txtMoTaMonAn.setText(monAn.MoTa);
        Glide.with(context).load(monAn.ImgMonAn).into(holder.imgMonAn);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txtTenMonAn;
        //private final TextView txtMoTaMonAn;
        private final TextView txtGia;
        private final ImageView imgMonAn;
        private final ItemClickListener itemClickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);


            itemView.setOnClickListener(this);
            txtTenMonAn = itemView.findViewById(R.id.txtTenMonAn);
            //txtMoTaMonAn = itemView.findViewById(R.id.txtMoTaMonAn);
            txtGia = itemView.findViewById(R.id.txtGia);
            imgMonAn = itemView.findViewById(R.id.imvMonAn);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
//    String getItem(int id) {
//        return null;
//    }
//
//    // allows clicks events to be caught
//    void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
