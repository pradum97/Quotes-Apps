package com.pk.Shayari2.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pk.Shayari2.Firebase_Database.Quotes_Home_Category.Model_quotes_category;
import com.pk.Shayari2.Firebase_Database.Quotes_Home_Category.ViewHolder_quotes_category;
import com.pk.Shayari2.Method.Method;
import com.pk.Shayari2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Home_Main_Fragment extends Fragment {
    View view;

    Method method;

    private RecyclerView mRecycleView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private LinearLayoutManager mLayoutManager; // for sorting
    private FirebaseRecyclerAdapter<Model_quotes_category, ViewHolder_quotes_category> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Model_quotes_category> options;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home__main, container, false);

        method = new Method(getActivity());
        mRecycleView = view.findViewById(R.id.recycler_view_category);
        mRecycleView.setHasFixedSize(false);
        // GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()
                ,LinearLayoutManager.HORIZONTAL,false);

        mRecycleView.setLayoutManager(linearLayoutManager);

        showCategory();

        // Replace Home Fragment

        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout_home_main,
                new Home_Fragment(), getResources().getString(R.string.home)).commitAllowingStateLoss();

        return view;
    }

    private void showCategory() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReferenceFromUrl("https://premium-quotes-34380-default-rtdb.firebaseio.com/Category/Home_quotes_category");

        options = new FirebaseRecyclerOptions.Builder<Model_quotes_category>().setQuery(mRef, Model_quotes_category.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model_quotes_category, ViewHolder_quotes_category>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_quotes_category viewHolder, int i, @NonNull Model_quotes_category model) {
                viewHolder.setDetails(getContext(), model.getName(), model.getImage());

            }


            @NonNull
            @Override
            public ViewHolder_quotes_category onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_quotes_category_design, parent, false);

                ViewHolder_quotes_category viewHolder = new ViewHolder_quotes_category(itemview);

                //item Click Listener
                viewHolder.setOnClickListener(new ViewHolder_quotes_category.ClickListener() {
                    @Override
                    public void onitemclick(View view, int position) {

                        String category = getItem(position).getName();

                        Toast.makeText(getActivity(), ""+category, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onitemlongclick(View view, int position) {

                        //  String titlee = getItem(position).getCategory();


                    }
                });

                return viewHolder;
            }
        };
        firebaseRecyclerAdapter.startListening();
        //set adapter ti firebase view
        mRecycleView.setAdapter(firebaseRecyclerAdapter);



    }


    @Override
    public void onStart() {
        super.onStart();

        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }

    }
}