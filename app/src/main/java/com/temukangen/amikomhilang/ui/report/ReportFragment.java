package com.temukangen.amikomhilang.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.temukangen.amikomhilang.R;
import com.temukangen.amikomhilang.Report;

public class ReportFragment extends Fragment {

    private Button btnPublish;
    private EditText edtTitle, edtDescription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTitle = view.findViewById(R.id.edtTitle);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnPublish = view.findViewById(R.id.btnPublish);

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                String description = edtDescription.getText().toString();

                if (title.isEmpty()){
                    Toast.makeText(getContext(), "Title is Empty", Toast.LENGTH_SHORT).show();
                } else if (description.isEmpty()){
                    Toast.makeText(getContext(), "Description is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    edtTitle.setText("");
                    edtDescription.setText("");

                    Report report = new Report(title, description);

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Report")
                            .push()
                            .setValue(report)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Published", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}