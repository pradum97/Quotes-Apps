package com.pk.Shayari2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.pk.Shayari2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.pk.Shayari2.databinding.ActivitySocialMediaBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Social_Media_Activity extends AppCompatActivity {

    private ActivitySocialMediaBinding binding; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySocialMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


      button_click();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.back_bn);
        setTitle("Add social media link");
        binding.toolbar.setNavigationOnClickListener(v -> {finish();

        });
    }

    private void button_click() {

        binding.bnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fb_link = binding.fb.getText().toString();
                String insta_link = binding.insta.getText().toString();
                String youtube_link = binding.youtube.getText().toString();

                if (TextUtils.isEmpty(fb_link) &&
                        (TextUtils.isEmpty(insta_link) &&
                                (TextUtils.isEmpty(youtube_link)))){

                    Toast.makeText(Social_Media_Activity.this, "Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> map = new HashMap<>();

                map.put("email","" + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                map.put("UID","" + FirebaseAuth.getInstance().getUid());

                map.put("facebook","" + fb_link);
                map.put("instagram",""+insta_link);
                map.put("youtube",""+youtube_link);

                FirebaseDatabase.getInstance().getReference("social_media_link")
                        .child(FirebaseAuth.getInstance().getUid())
                        .setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    Toast.makeText(Social_Media_Activity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(Social_Media_Activity.this, "Error : "+task.getException(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
    }
}