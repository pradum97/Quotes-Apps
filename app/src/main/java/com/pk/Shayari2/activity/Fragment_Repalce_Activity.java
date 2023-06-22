package com.pk.Shayari2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pk.Shayari2.Fragment.Gif_Upload_Fragment;
import com.pk.Shayari2.Fragment.ImageUploadFragment;
import com.pk.Shayari2.Fragment.Quotes_Upload_Fragment;
import com.pk.Shayari2.Fragment.Video_Upload_Fragment;
import com.pk.Shayari2.R;
import com.google.android.material.appbar.MaterialToolbar;

public class Fragment_Repalce_Activity extends AppCompatActivity {

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_repalce);
        if (getIntent().getStringExtra("replace_type") != null) {

            ActionType();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_bn);
        setTitle("Create New Post");
        toolbar.setNavigationOnClickListener(v -> finish());


    }

    private void ActionType() {

        switch (getIntent().getStringExtra("replace_type")) {
            case "quote_upload":
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new Quotes_Upload_Fragment(), "quotes_upload").commit();

                break;

            case "image_upload":

                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new ImageUploadFragment(), "image_upload").commit();

                break;

            case "video_upload":

                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new Video_Upload_Fragment(), "video_upload").commit();

                break;

            case "gif_upload":

                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new Gif_Upload_Fragment(), "gif_upload").commit();


                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}