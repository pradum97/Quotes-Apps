package com.pk.Shayari2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pk.Shayari2.Method.Method;
import com.pk.Shayari2.R;
import com.pk.Shayari2.adapter.UploadStatusAdapter;
import com.pk.Shayari2.databinding.ActivityUploadBinding;
import com.pk.Shayari2.interfaces.UploadStatusIF;
import com.pk.Shayari2.item.UploadStatusList;

import java.util.ArrayList;
import java.util.List;

public class Upload_Activity extends AppCompatActivity {

    private  ActivityUploadBinding binding;
    private RecyclerView recyclerView;

    private List<UploadStatusList> uploadStatusLists;
    private UploadStatusAdapter uploadStatusAdapter;
    private UploadStatusIF uploadStatusIF;

    private Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        method = new Method(this);

        recyclerView = findViewById(R.id.recyclerView_us);
        GridLayoutManager layoutManager = new GridLayoutManager(Upload_Activity.this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (uploadStatusAdapter.getItemViewType(position) == 1) {
                    return 2;
                }
                return 1;
            }
        });
        uploadStatusLists = new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);

        uploadStatusIF = type -> {

           choseOption(type);

        };

        onClick();

        setdata();

    }

    private void setdata() {

            uploadStatusLists.add(new UploadStatusList(getResources().getString(R.string.upload_video), "video",
                    R.drawable.video_status_ic, R.drawable.line_video_us));

            uploadStatusLists.add(new UploadStatusList(getResources().getString(R.string.upload_quotes), "quote",
                    R.drawable.quotes_status_ic, R.drawable.line_quotes_us));

            uploadStatusLists.add(new UploadStatusList(getResources().getString(R.string.upload_image), "image",
                    R.drawable.photos_status_ic, R.drawable.line_image_us));

            uploadStatusLists.add(new UploadStatusList(getResources().getString(R.string.upload_gif), "gif",
                    R.drawable.gif_status_ic, R.drawable.line_gif_us));

            uploadStatusAdapter = new UploadStatusAdapter(Upload_Activity.this, uploadStatusLists, uploadStatusIF);
            recyclerView.setAdapter(uploadStatusAdapter);

    }

    public void choseOption(String type) {

        if (!method.isNetworkAvailable()) {

            method.alertBox(getResources().getString(R.string.internet_connection));
            return;

        }

        switch (type) {
            case "video":

                startActivity(new Intent(Upload_Activity.this,Fragment_Repalce_Activity.class)
                        .putExtra("replace_type","video_upload"));
                finish();

                break;
            case "quote":

                startActivity(new Intent(Upload_Activity.this,Fragment_Repalce_Activity.class)
                        .putExtra("replace_type","quote_upload"));
                finish();

                break;
            case "image":

                startActivity(new Intent(Upload_Activity.this,Fragment_Repalce_Activity.class)
                        .putExtra("replace_type","image_upload"));
                finish();

                break;
            default:
                // gif
                startActivity(new Intent(Upload_Activity.this,Fragment_Repalce_Activity.class)
                        .putExtra("replace_type","gif_upload"));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().
                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();
    }

    private void onClick() {

        binding.imageViewCloseUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}