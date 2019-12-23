package com.temukangen.amikomhilang.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.temukangen.amikomhilang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TextView tvNim, tvName;
    private CircleImageView cvImage;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvName);
        tvNim = view.findViewById(R.id.tvNim);
        cvImage = view.findViewById(R.id.cvImage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase
                .getInstance()
                .getReference("User")
                .child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String image = dataSnapshot.child("nim").getValue(String.class);
                        String imageYear = image.substring(0,2);
                        String imageName = image.replace(".","_");
                        String url = "http://www.amikom.ac.id/public/fotomhs/20"+ imageYear +"/"+ imageName +".jpg";

                        tvName.setText(dataSnapshot.child("name").getValue(String.class));
                        tvNim.setText(dataSnapshot.child("nim").getValue(String.class));
                        Glide.with(getContext())
                                .load(url)
                                .centerCrop()
                                .override(100)
                                .placeholder(R.drawable.photo)
                                .into(cvImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}