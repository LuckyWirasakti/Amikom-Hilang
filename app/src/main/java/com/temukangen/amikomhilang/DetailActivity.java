package com.temukangen.amikomhilang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.temukangen.amikomhilang.lib.ImageUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {
    private ImageView ivImage;
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvDescription;
    private TextView tvPublisher;
    private Button btnDetail;
    private CircleImageView cvPublisher;
    private String nim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Item");

        ivImage = findViewById(R.id.ivImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvLocation = findViewById(R.id.tvLocation);
        tvDescription = findViewById(R.id.tvDescription);
        tvPublisher = findViewById(R.id.tvPublisher);
        cvPublisher = findViewById(R.id.cvPublisher);
        btnDetail = findViewById(R.id.btnContactPerson);



        tvTitle.setText(getIntent().getStringExtra("Title"));
        tvDescription.setText(getIntent().getStringExtra("Description"));
        tvLocation.setText(getIntent().getStringExtra("Location"));
        ivImage.setImageBitmap(ImageUtil.convert(getIntent().getStringExtra("Image")));

        loadButtonConfig();
        loadProfile(getIntent().getStringExtra("Publisher"));

    }

    private void loadButtonConfig() {
        if(getIntent().getStringExtra("Publisher").equals(FirebaseAuth.getInstance().getUid())){
            btnDetail.setText("Done");
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Report")
                            .child(getIntent().getStringExtra("PrimaryKey"))
                            .removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Dropped successful", Toast.LENGTH_SHORT).show();
                                    new Intent(getApplicationContext(), DashboardActivity.class);
                                    finish();
                                }
                            });
                }
            });
        } else {
            findViewById(R.id.btnContactPerson).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openWhatsappContact(getIntent().getStringExtra("PhoneNumber"));
                }
            });
        }
    }

    private void loadProfile(String publisher) {
        FirebaseDatabase
                .getInstance()
                .getReference("Profile")
                .child(publisher)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tvPublisher.setText(dataSnapshot.child("name").getValue(String.class));
                        nim = dataSnapshot.child("nim").getValue(String.class);
                        String url = "http://www.amikom.ac.id/public/fotomhs/20"
                                + nim.substring(0,2)
                                +"/"
                                + nim.replace(".","_") +".jpg";
                        Glide.with(getApplicationContext())
                                .load(url)
                                .centerCrop()
                                .override(100)
                                .placeholder(R.drawable.common_full_open_on_phone)
                                .into(cvPublisher);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("publisherError", "onCancelled: "+databaseError.getMessage());
                    }
                });
    }

    private void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }
}
