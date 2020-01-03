package com.temukangen.amikomhilang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.temukangen.amikomhilang.R;
import com.temukangen.amikomhilang.ReportActivity;
import com.temukangen.amikomhilang.adapter.ReportAdapter;
import com.temukangen.amikomhilang.DetailActivity;
import com.temukangen.amikomhilang.model.Report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ReportFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Report> list = new ArrayList<>();
    private ReportAdapter reportAdapter;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report, container, false);

        recyclerView = root.findViewById(R.id.rv_item_publish);

        recyclerView.setHasFixedSize(true);
        reportAdapter = new ReportAdapter(list);
        recyclerView.setAdapter(reportAdapter);

        getReport();
        fab(root);
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
    private void setAdapterValueChanged() {
        reportAdapter = new ReportAdapter(list);
        reportAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(reportAdapter);

        reportAdapter.setOnItemClickCallback(new ReportAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Report data) {
                showSelectedItem(data);
            }
        });
    }
    private void showSelectedItem(Report data) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("PrimaryKey", data.getPrimaryKey());
        intent.putExtra("Title", data.getTitle());
        intent.putExtra("Location", data.getLocation());
        intent.putExtra("PhoneNumber", data.getPhoneNumber());
        intent.putExtra("Description", data.getDescription());
        intent.putExtra("Image",data.getImage());
        intent.putExtra("Publisher", data.getPublisher());
        startActivity(intent);
    }
    private void getReport() {
        String Publisher = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Report")
                .orderByChild("publisher")
                .startAt(Publisher)
                .endAt(Publisher)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        for (Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator(); iterator.hasNext(); ) {
                            DataSnapshot dataSnapshot1 = iterator.next();

                            Report report;
                            report = dataSnapshot1.getValue(Report.class);
                            report.setPrimaryKey(dataSnapshot1.getKey());

                            list.add(report);
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