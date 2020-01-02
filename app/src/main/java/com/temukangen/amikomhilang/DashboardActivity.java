package com.temukangen.amikomhilang;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.temukangen.amikomhilang.model.Report;

import java.io.ByteArrayOutputStream;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_home, R.id.navigation_report)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public static class ReportActivity extends AppCompatActivity {

        private Button btnPublish;
        private EditText edtTitle;
        private EditText edtDescription;
        private EditText edtPhoneNumber;
        private EditText edtLocation;
        private ImageView ivImage;
        private Bitmap bitmap;
        private String image = "";
        private static final int CAMERA_REQUEST_CODE = 777;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_report);
            getSupportActionBar().setTitle("Publish");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        @Override
        protected void onStart() {
            super.onStart();

            edtTitle = findViewById(R.id.edtTitle);
            edtDescription = findViewById(R.id.edtDescription);
            edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
            edtLocation = findViewById(R.id.edtLocation);
            ivImage = findViewById(R.id.ivImage);

            btnPublish = findViewById(R.id.btnPublish);

            btnPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = edtTitle.getText().toString();
                    String description = edtDescription.getText().toString();
                    String phoneNumber = edtPhoneNumber.getText().toString().trim();
                    String location = edtLocation.getText().toString();

                    if (title.isEmpty()){
                        Toast.makeText(ReportActivity.this, "Title is Empty", Toast.LENGTH_SHORT).show();
                    } else if (phoneNumber.isEmpty()){
                        Toast.makeText(ReportActivity.this, "Phone number is Empty", Toast.LENGTH_SHORT).show();
                    } else if (location.isEmpty()){
                        Toast.makeText(ReportActivity.this, "Location is Empty", Toast.LENGTH_SHORT).show();
                    } else if (description.isEmpty()) {
                        Toast.makeText(ReportActivity.this, "Description is Empty", Toast.LENGTH_SHORT).show();
                    } else if (image.isEmpty()) {
                        Toast.makeText(ReportActivity.this, "Photo is Empty", Toast.LENGTH_SHORT).show();
                    } else {

                        Report report = new Report();
                        report.setTitle(title);
                        report.setDescription(description);
                        report.setPhoneNumber(phoneNumber);
                        report.setLocation(location);
                        report.setPublisher(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        report.setImage(image);

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
                                            Toast.makeText(ReportActivity.this, "Published", Toast.LENGTH_SHORT).show();
                                            edtTitle.setText("");
                                            edtDescription.setText("");
                                            edtPhoneNumber.setText("");
                                            edtLocation.setText("");
                                        } else {
                                            Toast.makeText(ReportActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE);
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case (CAMERA_REQUEST_CODE):
                    if (resultCode == Activity.RESULT_OK) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                        ivImage.setImageBitmap(bitmap);
                        image = bitmapToBase64(bitmap);
                    }
            }
        }

        private String bitmapToBase64(Bitmap bitmap) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
    }
}
