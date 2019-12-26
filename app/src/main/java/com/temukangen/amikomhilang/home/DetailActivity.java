package com.temukangen.amikomhilang.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.temukangen.amikomhilang.R;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_DESC= "extra_desc";
    public static final String EXTRA_TITLE= "extra_title";
    public static final String EXTRA_LOCATION= "extra_location";
    public static final String EXTRA_PHONE= "extra_phone";

    private ImageView ivImage;
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvDescription;

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

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            int resint = bundle.getInt("Image");
            ivImage.setImageResource(resint);
        }

        tvTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
        tvDescription.setText(getIntent().getStringExtra(EXTRA_DESC));
        tvLocation.setText(getIntent().getStringExtra(EXTRA_LOCATION));

        findViewById(R.id.btnContactPerson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsappContact(getIntent().getStringExtra(EXTRA_PHONE));
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
