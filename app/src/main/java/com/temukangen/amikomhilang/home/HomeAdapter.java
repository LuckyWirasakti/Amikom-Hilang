package com.temukangen.amikomhilang.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {
        Home home = homeArrayList.get(position);
        Bitmap bitmap = base64ToBitmap(home.getImage());
        Glide.with(holder.itemView.getContext())
                .load(bitmap)
                .apply(new RequestOptions().override(55, 55))
                .into(holder.img_item_photo);
        holder.tv_item_name.setText(home.getTitle());
        holder.tv_item_location.setText(home.getLocation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(homeArrayList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeArrayList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_item_photo;
        private TextView tv_item_name, tv_item_location;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item_photo = itemView.findViewById(R.id.img_item_photo);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_location = itemView.findViewById(R.id.tv_item_location);
        }
    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
    public interface OnItemClickCallback {
        void onItemClicked(Home data);
    }
}
