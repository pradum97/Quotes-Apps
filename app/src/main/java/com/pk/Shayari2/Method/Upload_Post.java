package com.pk.Shayari2.Method;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pk.Shayari2.Fragment.Quotes_Upload_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Upload_Post {
    private final Activity activity;
    DatabaseReference ref;


    public Upload_Post(Activity activity) {
        this.activity = activity;
         ref = FirebaseDatabase.getInstance().getReference("Posts");

    }

    public void upload_post(long p_number ,String email,String uid,String Quotes,String font_Style,
                            int background_color,String quotes_category,
                            String language,String tag,int Quotes_text_color,
                            String file_type,String image_url,String video_url,
                            String video_thumbnail){

        Method method = new Method(activity);
        long timestamp = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("email", email);
        hashMap.put("uid", uid);
        hashMap.put("post_time", method.currentDateAndTime());
        hashMap.put("posttext", Quotes);
        hashMap.put("quotes_text_color", Quotes_text_color);
        hashMap.put("quotes_background_color", background_color);
        hashMap.put("post_id", timestamp);
        hashMap.put("font_style", font_Style);
        hashMap.put("category", quotes_category);
        hashMap.put("language", language);
        hashMap.put("tag", tag);
        hashMap.put("page_number", p_number);

        hashMap.put("file_type", file_type);
        hashMap.put("image_url", image_url);
        hashMap.put("video_url", video_url);
        hashMap.put("video_thumbnail", video_thumbnail);


        ref.child("home")
                .child(String.valueOf(timestamp))
                .setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Quotes_Upload_Fragment.progressDialog.dismiss();

                            System.out.println("+++++++++++=");
                            Snackbar.make(activity.findViewById(android.R.id.content),
                                    "Post Publish Successful", Snackbar.LENGTH_LONG).show();
                            activity.finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Quotes_Upload_Fragment.progressDialog.dismiss();

                Toast.makeText(activity, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
