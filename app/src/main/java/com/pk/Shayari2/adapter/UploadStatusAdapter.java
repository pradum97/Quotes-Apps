package com.pk.Shayari2.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pk.Shayari2.R;
import com.pk.Shayari2.interfaces.UploadStatusIF;
import com.pk.Shayari2.item.UploadStatusList;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class UploadStatusAdapter extends RecyclerView.Adapter<UploadStatusAdapter.ViewHolder> {

    private final Activity activity;
    private final UploadStatusIF uploadStatusIF;
    private final List<UploadStatusList> uploadStatusLists;

    public UploadStatusAdapter(Activity activity, List<UploadStatusList> uploadStatusLists, UploadStatusIF uploadStatusIF) {
        this.activity = activity;
        this.uploadStatusIF = uploadStatusIF;
        this.uploadStatusLists = uploadStatusLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).
                inflate(R.layout.uplaod_status_adapter, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(activity).load(uploadStatusLists.get(position).getUpload_image())
                .into(holder.imageView);

        holder.textView.setText(uploadStatusLists.get(position).getUpload_name());

        holder.linearLayout.setBackground(activity.getResources().getDrawable(uploadStatusLists.get(position).getUpload_bg()));

        holder.linearLayout.setOnClickListener(v -> uploadStatusIF.UploadType(uploadStatusLists.get(position).getUpload_type()));

    }

    @Override
    public int getItemCount() {
        return uploadStatusLists.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (uploadStatusLists.size() == 1) {
            return 1;
        } else {
            if (uploadStatusLists.size() == 3) {
                if (position == 2) {
                    return 1;
                }
            }
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final MaterialTextView textView;
        private final LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_us_adapter);
            textView = itemView.findViewById(R.id.textView_us_adapter);
            linearLayout = itemView.findViewById(R.id.ll_us_adapter);

        }
    }
}
