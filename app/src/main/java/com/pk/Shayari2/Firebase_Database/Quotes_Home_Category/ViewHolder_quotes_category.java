package com.pk.Shayari2.Firebase_Database.Quotes_Home_Category;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.pk.Shayari2.R;
import com.google.android.material.button.MaterialButton;

public class ViewHolder_quotes_category extends RecyclerView.ViewHolder {

    View mview;


    public ViewHolder_quotes_category(View itemView) {
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

    public void setDetails(final Context ctx, final String Name, final String image) {

        MaterialButton name = mview.findViewById(R.id.name);

       name.setText(Name);


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
