package com.pk.Shayari2.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pk.Shayari2.Method.Method;
import com.pk.Shayari2.Profile.Profile_Model;
import com.pk.Shayari2.R;
import com.pk.Shayari2.activity.Upload_Activity;
import com.pk.Shayari2.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile_Fragment extends Fragment {
    private FragmentProfileBinding binding;

    private Profile_Model profile_model;
    private Method method;
    private ProgressBar progressBar;

    protected String fb_link;
    protected String insta_link;
    protected String youtube_link;
    private Animation myAnim;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        method = new Method(getActivity());
        myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);


        get_Profile_Detalis();
        get_Social_Media_Link();
        onClick();

        return view;
    }

    private  void social_media_click(String click_type){

        switch (click_type){

            case "facebook" : {

                binding.ivFacebook.startAnimation(myAnim);
                if (fb_link.equals("")) {
                    method.alertBox(getResources().getString(R.string.user_not_instagram_link));

                } else {
                    Uri uri = Uri.parse(fb_link);
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                    likeIng.setPackage("com.facebook.katana");
                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {

                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(fb_link)));
                        } catch (Exception e1) {

                            method.alertBox(getResources().getString(R.string.wrong));
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }   break;
            case "instagram": {

                binding.ivInstagram.startAnimation(myAnim);

                if (insta_link.equals("")) {
                    method.alertBox(getResources().getString(R.string.user_not_instagram_link));

                } else {
                    Uri uri = Uri.parse(insta_link);
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                    likeIng.setPackage("com.instagram.android");
                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(insta_link)));
                        } catch (Exception e1) {

                            method.alertBox(getResources().getString(R.string.wrong));
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }   break;
            case "youtube"  :  {

                binding.ivYoutube.startAnimation(myAnim);

                if (youtube_link.equals("")) {
                    method.alertBox(getResources().getString(R.string.user_not_youtube_link));
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(youtube_link));
                        startActivity(intent);
                    } catch (Exception e) {
                        method.alertBox(getResources().getString(R.string.wrong));
                    }
                }
            }  break;
        }
    }

    private void onClick() {

        binding.fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), Upload_Activity.class));
            }
        });

        binding.ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                social_media_click("facebook");

            }
        });

        binding.ivInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                social_media_click("instagram");
            }
        });

        binding.ivYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                social_media_click("youtube");

            }
        });

    }

    protected void get_Profile_Detalis() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        List<String> data = new ArrayList<>();

                        data.add("" + ds.child("Name").getValue());     // 0
                        data.add("" + ds.child("USER_NAME").getValue());// 1
                        data.add("" + ds.child("Email").getValue());     // 2
                        data.add("" + ds.child("Phone").getValue());      // 3
                        data.add("" + ds.child("Gender").getValue());    // 4
                        data.add("" + ds.child("Profile_Image").getValue());// 5
                        data.add("" + ds.child("Cover_Image").getValue());  // 6
                        data.add("" + ds.child("uid").getValue());            // 7
                        data.add("" + ds.child("Auth_Types").getValue());     // 8
                        data.add("" + ds.child("Verified_Status").getValue());     // 9
                        // set value
                        binding.textViewNamePro.setText(data.get(0));

                        boolean verified_status = Boolean.parseBoolean(data.get(9));

                        // Check Verified Status
                        if (verified_status) {

                            binding.ivVerified.setVisibility(View.VISIBLE);
                            Glide.with(getActivity()).load(R.drawable.verified_ic).into(binding.ivVerified);

                        } else {
                            binding.ivVerified.setVisibility(View.GONE);
                            binding.ivVerified.setImageDrawable(null);
                        }
                        // Check Auth Type
                        switch (data.get(8)) {

                            case "custom":
                                binding.ivAuthtype.setVisibility(View.GONE);

                                break;

                            case "google":

                                Glide.with(getActivity()).load(R.drawable.google_ic).into(binding.ivAuthtype);

                                break;

                            case "facebook":

                                Glide.with(getActivity()).load(R.drawable.fb_user_pro).into(binding.ivAuthtype);

                                break;

                            default:
                        }

                        if (data.get(5).equals("null")) {

                            Glide.with(getActivity()).load(R.drawable.man_icon).into(binding.imageViewPro);
                            binding.pbProfileImage.setVisibility(View.GONE);
                        } else {

                            binding.pbProfileImage.setVisibility(View.VISIBLE);

                            Glide.with(getActivity())
                                    .load(data.get(5)).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                            Target<Drawable> target, boolean isFirstResource) {

                                    System.out.println("Profile Image load error : " + e.getMessage());
                                    binding.pbProfileImage.setVisibility(View.GONE);

                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model,
                                                               Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    binding.pbProfileImage.setVisibility(View.GONE);

                                    return false;
                                }
                            }).error(R.drawable.man_icon)
                                    .apply(new RequestOptions()
                                            .override(100, 100))
                                    .into(binding.imageViewPro);
                        }


                        binding.progressbarProfile.setVisibility(View.GONE);


                    }
                } else {

                    binding.mainLayout.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    protected void get_Social_Media_Link() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().
                        getReference("social_media/social_media_link");


        Query query = databaseReference
                .orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        fb_link = "" + ds.child("facebook").getValue();
                        insta_link = "" + ds.child("instagram").getValue();
                        youtube_link = "" + ds.child("youtube").getValue();


                    }
                } else {

                    binding.ivFacebook.setVisibility(View.GONE);
                    binding.ivInstagram.setVisibility(View.GONE);
                    binding.ivYoutube.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}