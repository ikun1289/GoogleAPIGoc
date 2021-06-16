package com.example.googleapi.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.googleapi.Models.BinhLuan;
import com.example.googleapi.Models.MonAn;
import com.example.googleapi.NguoiDung.ListMonAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BinhLuanAdapter extends RecyclerView.Adapter<BinhLuanAdapter.ViewHolder> {

    private List<BinhLuan> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public BinhLuanAdapter(Context context, List<BinhLuan> data, ItemClickListener mItemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mClickListener = mItemClickListener;
        this.mData = data;
        this.context = context;
    }

    public void setData(List<BinhLuan> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.dong_binh_luan, parent, false);
        return new ViewHolder(view, mClickListener);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BinhLuan binhLuan = mData.get(position);
        if (binhLuan == null)
            return;

        holder.txtTenNguoiDang.setText(binhLuan.email);
        holder.txtGioDang.setText(binhLuan.time);
        holder.txtNoiDung.setText(binhLuan.noiDung);

        holder.btnDelete.setEnabled(binhLuan.IDNguoiGui.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        holder.btnDelete.setOnClickListener(v -> {
            BinhLuan binhLuan1 = mData.get(position);
            DocumentReference quanAnRef = FirebaseFirestore.getInstance().collection("QuanAn").document(ListMonAn.IDQuanAn);

            quanAnRef.collection("BinhLuan").document(binhLuan1.IDBinhLuan).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(context,"Xóa bình luận thành công",Toast.LENGTH_SHORT).show();
                        mData.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txtTenNguoiDang;
        private final TextView txtGioDang;
        private final TextView txtNoiDung;
        private final ImageButton btnDelete;
        private final ItemClickListener itemClickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);


            itemView.setOnClickListener(this);
            txtTenNguoiDang = itemView.findViewById(R.id.txtTenNguoiDang);
            txtGioDang = itemView.findViewById(R.id.txtGioDang);
            txtNoiDung = itemView.findViewById(R.id.txtNoiDung);
            btnDelete = itemView.findViewById(R.id.btnDeletecmt);
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
