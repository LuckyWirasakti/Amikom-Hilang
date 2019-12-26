package com.temukangen.amikomhilang.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.temukangen.amikomhilang.R;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_DESC= "extra_desc";
    public static final String EXTRA_TITLE= "extra_title";
    public static final String EXTRA_LOCATION= "extra_location";
    public static final String EXTRA_PHONE= "extra_phone";
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Item");

        imageView = findViewById(R.id.dtlImage);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvLocation = findViewById(R.id.tvLocation);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            int resint = bundle.getInt("Image");
            imageView.setImageResource(resint);
        }
        String description = getIntent().getStringExtra(EXTRA_DESC);
        tvDescription.setText(description);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        tvTitle.setText(title);
        String location = getIntent().getStringExtra(EXTRA_LOCATION);
        tvLocation.setText(location);
        String phone = getIntent().getStringExtra(EXTRA_PHONE);
        findViewById(R.id.btnContactPerson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsappContact("+6285799976669");
            }
        });
    }

    private void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, "Halo kak namaku Lucky"));
    }

}
