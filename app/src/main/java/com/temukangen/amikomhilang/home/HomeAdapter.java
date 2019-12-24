package com.temukangen.amikomhilang.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.temukangen.amikomhilang.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private ArrayList<Home> homeArrayList;

    public HomeAdapter(ArrayList<Home> homeArrayList) {
        this.homeArrayList = homeArrayList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_lost, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Home home = homeArrayList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(home.getImage())
                .apply(new RequestOptions().override(55, 55))
                .into(holder.img_item_photo);
        holder.tv_item_name.setText(home.getName());
        holder.tv_item_detail.setText(home.getDescription());
    }

    @Override
    public int getItemCount() {
        return homeArrayList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_item_photo;
        private TextView tv_item_name, tv_item_detail;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item_photo = itemView.findViewById(R.id.img_item_photo);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_detail = itemView.findViewById(R.id.tv_item_detail);
        }
    }
}
