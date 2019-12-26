package com.temukangen.amikomhilang.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.temukangen.amikomhilang.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Home> list = new ArrayList<>();
    private HomeAdapter homeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.rv_item_lost);
        recyclerView.setHasFixedSize(true);
        getHomeData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }
    private void showSelectedItem(Home data) {
        Toast.makeText(getContext(), "Kamu memilih " + data.getTitle(), Toast.LENGTH_SHORT).show();
        Intent moveWithDataIntent = new Intent(getContext(), DetailActivity.class);
        moveWithDataIntent.putExtra(DetailActivity.EXTRA_TITLE, data.getTitle());
        moveWithDataIntent.putExtra(DetailActivity.EXTRA_LOCATION, data.getLocation());
        moveWithDataIntent.putExtra(DetailActivity.EXTRA_PHONE, data.getPhoneNumber());
        moveWithDataIntent.putExtra(DetailActivity.EXTRA_DESC, data.getDescription());
        moveWithDataIntent.putExtra("Image",data.getImage());
        startActivity(moveWithDataIntent);
    }
    private void getHomeData() {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Report")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        for (Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator(); iterator.hasNext(); ) {
                            DataSnapshot dataSnapshot1 = iterator.next();
                            Home home = dataSnapshot1.getValue(Home.class);
                            list.add(home);
                        }

                        Collections.reverse(list);
                        homeAdapter = new HomeAdapter(list);
                        recyclerView.setAdapter(homeAdapter);
                        homeAdapter.setOnItemClickCallback(new HomeAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(Home data) {
                                showSelectedItem(data);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("homeError", "onCancelled: ", databaseError.toException());
                    }
                });

    }
}