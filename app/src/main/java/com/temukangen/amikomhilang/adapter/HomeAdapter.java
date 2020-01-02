package com.temukangen.amikomhilang.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.temukangen.amikomhilang.R;
import com.temukangen.amikomhilang.lib.ImageUtil;
import com.temukangen.amikomhilang.model.Report;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private ArrayList<Report> reportArrayList;

    public HomeAdapter(ArrayList<Report> homeArrayList) {
        this.reportArrayList = homeArrayList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_lost, parent, false);
        return new HomeViewHolder(view);
    }
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {
        Report report = reportArrayList.get(position);
        Bitmap bitmap = ImageUtil.convert(report.getImage());
        Glide.with(holder.itemView.getContext())
                .load(bitmap)
                .apply(new RequestOptions().override(55, 55))
                .into(holder.img_item_photo);
        holder.tv_item_name.setText(report.getTitle());
        holder.tv_item_detail.setText(report.getDescription());
        holder.tv_item_loct.setText(report.getLocation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(reportArrayList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportArrayList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_item_photo;
        private TextView tv_item_name;
        private TextView tv_item_detail;
        private TextView tv_item_loct;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item_photo = itemView.findViewById(R.id.img_item_photo);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_detail = itemView.findViewById(R.id.tv_item_detail);
            tv_item_loct = itemView.findViewById(R.id.tv_item_loct);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Report data);
    }
}
