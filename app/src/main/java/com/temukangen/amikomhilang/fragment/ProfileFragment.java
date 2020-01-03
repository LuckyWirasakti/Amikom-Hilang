package com.temukangen.amikomhilang.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.temukangen.amikomhilang.R;

public class ProfileFragment extends Fragment {

    private TextView tvNim;
    private TextView tvName;
    private TextView tvEmail;
    private ImageView ivImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        loadProfile(root);
        return root;
    }

    private void loadProfile(View root) {
        tvName = root.findViewById(R.id.tvName);
        tvNim = root.findViewById(R.id.tvNim);
        tvEmail = root.findViewById(R.id.tvEmail);
        ivImage = root.findViewById(R.id.ivImage);

        FirebaseDatabase
                .getInstance()
                .getReference("Profile")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String image = dataSnapshot.child("nim").getValue(String.class);
                        String imageYear = image.substring(0,2);
                        String imageName = image.replace(".","_");
                        String url = "http://www.amikom.ac.id/public/fotomhs/20"+ imageYear +"/"+ imageName +".jpg";

                        tvName.setText(dataSnapshot.child("name").getValue(String.class));
                        tvNim.setText(dataSnapshot.child("nim").getValue(String.class));
                        tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                        Glide.with(getContext())
                                .load(url)
                                .centerCrop()
                                .override(150,200)
                                .placeholder(R.drawable.photo)
                                .into(ivImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("profileError", "onCancelled: ", databaseError.toException());
                    }
                });
    }
}