package com.temukangen.amikomhilang.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.temukangen.amikomhilang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_lost, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return Item.name.length;
    }


    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView img_item_photo;
        private TextView tv_item_name, tv_item_detail;

        public ItemHolder(View view){
            super(view);

            img_item_photo = view.findViewById(R.id.img_item_photo);
            tv_item_name = view.findViewById(R.id.tv_item_name);
            tv_item_detail = view.findViewById(R.id.tv_item_detail);
            view.setOnClickListener(this);
        }

        private void bindView(int postition) {
            tv_item_name.setText(Item.description[postition]);
            tv_item_detail.setText(Item.description[postition]);
            img_item_photo.setImageResource(Item.image[postition]);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
