package com.temukangen.amikomhilang.report;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import com.temukangen.amikomhilang.home.Home;
import com.temukangen.amikomhilang.home.HomeAdapter;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>  {
    private ArrayList<Report> reportArrayList;

    public ReportAdapter(ArrayList<Report> reportArrayList) {
        this.reportArrayList = reportArrayList;
    }
    private ReportAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(ReportAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    @NonNull
    @Override
    public ReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_lost, parent, false);
        return new ReportAdapter.ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReportAdapter.ReportViewHolder holder, int position) {
        final Report report = reportArrayList.get(position);
        Bitmap bitmap = base64ToBitmap(report.getImage());
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

    public class ReportViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_item_photo;
        private TextView tv_item_name;
        private TextView tv_item_detail;
        private TextView tv_item_loct;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item_photo = itemView.findViewById(R.id.img_item_photo);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_detail = itemView.findViewById(R.id.tv_item_detail);
            tv_item_loct = itemView.findViewById(R.id.tv_item_loct);
        }
    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
    public interface OnItemClickCallback {
        void onItemClicked(Report data);
    }
}
