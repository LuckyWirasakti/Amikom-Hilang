package com.temukangen.amikomhilang.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.temukangen.amikomhilang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {
    private ImageView ivImage;
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvDescription;
    private TextView tvPublisher;
    private CircleImageView cvPublisher;
    private String nim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Item");

        loadProfile(getIntent().getStringExtra("Publisher"));

        ivImage = findViewById(R.id.ivImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvLocation = findViewById(R.id.tvLocation);
        tvDescription = findViewById(R.id.tvDescription);
        tvPublisher = findViewById(R.id.tvPublisher);
        cvPublisher = findViewById(R.id.cvPublisher);

        tvTitle.setText(getIntent().getStringExtra("Title"));
        tvDescription.setText(getIntent().getStringExtra("Description"));
        tvLocation.setText(getIntent().getStringExtra("Location"));
        ivImage.setImageBitmap(base64ToBitmap(getIntent().getStringExtra("Image")));

        findViewById(R.id.btnContactPerson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsappContact(getIntent().getStringExtra("PhoneNumber"));
            }
        });
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

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
