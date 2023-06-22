package com.pk.Shayari2.Firebase_Database.Firebase_Home;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pk.Shayari2.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewHolder_quotes extends RecyclerView.ViewHolder {

    View mview;
    ImageView iv_profile, iv_filetype, iv_post_image, iv_like, iv_fev, player_icon;
    MaterialTextView tv_profile_name, tv_time, tv_title, tv_seen, tv_like, tv_category,tv_quotes;
    RelativeLayout quotes_bg;


    public ViewHolder_quotes(View itemView) {
        super(itemView);
        mview = itemView;

        // item click

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclicklistner.onitemclick(view, getAdapterPosition());
            }
        });
        //item Long Click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mclicklistner.onitemlongclick(v, getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(final Context ctx, final String email, final String uid, final String post_time, final String title,
                           final int quotes_text_color, final int quotes_background_color, final long post_id,
                           final String font_style, final String category, final String language,
                           final String tag, final int page_number, final String file_type, final String image_url,
                           final String video_url, final String video_thumbnail) {

        // Image View
        iv_profile = mview.findViewById(R.id.iv_profile);
        iv_filetype = mview.findViewById(R.id.iv_filetype);
        iv_post_image = mview.findViewById(R.id.iv_post_image);
        iv_like = mview.findViewById(R.id.iv_like);
        iv_fev = mview.findViewById(R.id.iv_fev);
        player_icon = mview.findViewById(R.id.player);

        // Text View
        tv_profile_name = mview.findViewById(R.id.name);
        tv_time = mview.findViewById(R.id.time);
        tv_title = mview.findViewById(R.id.tv_title);
        tv_seen = mview.findViewById(R.id.tv_seen);
        tv_like = mview.findViewById(R.id.tv_like);
        tv_category = mview.findViewById(R.id.tv_category);
        tv_quotes = mview.findViewById(R.id.tv_quotes);

        // relative layout
        quotes_bg = mview.findViewById(R.id.quotes_bg);





        getPostUser(email, ctx);

        // set value
        tv_time.setText(post_time);
        tv_category.setText(category);


        if (email != null) {

            switch (file_type) {
                case "quotes":
                    player_icon.setVisibility(View.GONE);

                    iv_filetype.setImageResource(R.drawable.quotes_ic);
                    tv_quotes.setText(title);
                    tv_quotes.setTextColor(quotes_text_color);
                    tv_quotes.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.GONE);

                    // relative layout

                    quotes_bg.setBackgroundColor(quotes_background_color);
                    Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "text_font/" + font_style);
                    tv_quotes.setTypeface(typeface);


                    break;
                case "image":
                    player_icon.setVisibility(View.GONE);
                    iv_post_image.setVisibility(View.VISIBLE);


                    Glide.with(ctx).load(image_url).into(iv_post_image);
                    iv_filetype.setImageResource(R.drawable.img_ic);

                    tv_title.setText(title);
                    tv_title.setVisibility(View.VISIBLE);


                    break;
                case "video":

                    player_icon.setVisibility(View.VISIBLE);
                    iv_post_image.setVisibility(View.VISIBLE);


                    Glide.with(ctx).load(video_thumbnail).into(iv_post_image);
                    iv_filetype.setImageResource(R.drawable.video_ic);
                    tv_title.setText(title);
                    tv_title.setVisibility(View.VISIBLE);


                    break;
                default:

                    player_icon.setVisibility(View.GONE);
                    iv_post_image.setVisibility(View.GONE);
                    tv_quotes.setVisibility(View.GONE);
                    quotes_bg.setBackgroundColor(ctx.getResources().getColor(R.color.white));

                    break;
            }
        }


    }

    private void getPostUser(String email, Context ctx) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Query query = databaseReference.orderByChild("Email").equalTo(email);
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

                        // set value

                        Glide.with(ctx).load(data.get(5)).into(iv_profile);
                        tv_profile_name.setText(data.get(0));


                    }
                } else {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ClickListener mclicklistner;

    public interface ClickListener {
        void onitemclick(View view, int position);

        void onitemlongclick(View view, int position);

    }

    public void setOnClickListener(ClickListener clickListener) {
        mclicklistner = clickListener;
    }
}
