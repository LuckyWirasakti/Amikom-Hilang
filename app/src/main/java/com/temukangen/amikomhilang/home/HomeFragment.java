package com.temukangen.amikomhilang.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.temukangen.amikomhilang.R;
import com.temukangen.amikomhilang.report.ReportActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Home> list = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private TextInputEditText edtSearch;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        edtSearch = root.findViewById(R.id.edtSearch);
        recyclerView = root.findViewById(R.id.rv_item_lost);

        recyclerView.setHasFixedSize(true);
        homeAdapter = new HomeAdapter(list);
        recyclerView.setAdapter(homeAdapter);

        fab(root);
        getHome();
        searchHome();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    private void fab(View root) {
        root.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ReportActivity.class));
            }
        });
    }

    private void searchHome() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    search(s.toString());
                } else {
                    getHome();
                }
            }
        });
    }

    private void search(String s) {
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Report");
        Query query = databaseReference
                .orderByChild("title")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    list.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        Home home = dataSnapshot1.getValue(Home.class);
                        list.add(home);
                    }

                    setAdapterValueChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAdapterValueChanged() {
        homeAdapter = new HomeAdapter(list);
        homeAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(homeAdapter);

        homeAdapter.setOnItemClickCallback(new HomeAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Home data) {
                showSelectedItem(data);
            }
        });

    }

    private void showSelectedItem(Home data) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("Title", data.getTitle());
        intent.putExtra("Location", data.getLocation());
        intent.putExtra("PhoneNumber", data.getPhoneNumber());
        intent.putExtra("Description", data.getDescription());
        intent.putExtra("Image",data.getImage());
        intent.putExtra("Publisher", data.getPublisher());
        startActivity(intent);
    }

    private void getHome() {
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
                        setAdapterValueChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("homeError", "onCancelled: ", databaseError.toException());
                    }
                });

    }
}