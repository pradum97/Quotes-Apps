package com.pk.Shayari2.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pk.Shayari2.Firebase_Database.Firebase_Home.Model_quotes;
import com.pk.Shayari2.Firebase_Database.Firebase_Home.ViewHolder_quotes;
import com.pk.Shayari2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;


public class Home_Fragment extends Fragment {

    private RecyclerView mRecycleView;
    private  FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private LinearLayoutManager mLayoutManager; // for sorting
    private FirebaseRecyclerAdapter<Model_quotes, ViewHolder_quotes> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Model_quotes> options;
    ProgressDialog progressDialog;
    private  LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
       // progressDialog.show();

        mRecycleView = view.findViewById(R.id.recyclerViewcategory);
        mRecycleView.setHasFixedSize(false);
        mRecycleView.setFocusable(false);
        mRecycleView.setNestedScrollingEnabled(false);
         linearLayoutManager = new LinearLayoutManager(getActivity());
         linearLayoutManager.setReverseLayout(true);
         linearLayoutManager.setStackFromEnd(true);
        mRecycleView.setLayoutManager(linearLayoutManager);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Posts");
        showdata();


        return  view;
    }
    private void showdata() {

        options = new FirebaseRecyclerOptions.Builder<Model_quotes>().setQuery(mRef.child("home"), Model_quotes.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model_quotes, ViewHolder_quotes>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_quotes viewHolder, int i, @NonNull Model_quotes model) {

                viewHolder.setDetails(getContext(), model.getEmail(),model.getUid(),model.getPost_time(),model.getPosttext(),
                        model.getQuotes_text_color(),model.getQuotes_background_color(),model.getPost_id(),
                        model.getFont_style(),model.getCategory(),model.getLanguage(),model.getTag(),
                        model.getPage_number(),model.getFile_type(), model.getImage_url(), model.getVideo_url(),
                        model.getVideo_thumbnail());

                progressDialog.dismiss();

            }

            @Override
            public void onError(@NonNull @NotNull DatabaseError error) {
                super.onError(error);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @NonNull
            @Override
            public ViewHolder_quotes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                System.out.println("================="+viewType);


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_design, parent, false);

                ViewHolder_quotes viewHolder = new ViewHolder_quotes(itemview);

                //item Click Listener
                viewHolder.setOnClickListener(new ViewHolder_quotes.ClickListener() {
                    @Override
                    public void onitemclick(View view, int position) {


                      /*  String category = getItem(position).getCategory();

                        Intent intent = new Intent(view.getContext(), Category_View_Activity.class);
                        intent.putExtra("category", category);
                        startActivity(intent);*/


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