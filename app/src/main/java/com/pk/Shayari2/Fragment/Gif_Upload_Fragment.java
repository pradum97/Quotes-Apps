package com.pk.Shayari2.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.pk.Shayari2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.validator.ChipifyingNachoValidator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Gif_Upload_Fragment extends Fragment {

    private Spinner spinner_cat, spinner_language;
    private List<String> image_category, language_category;
    private String i_category, lang_category, g_title;
    private ProgressDialog progressDialog;
    private NachoTextView tag_tv;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gif__upload, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_bn);
        getActivity().setTitle("Create New Post");
        toolbar.setNavigationOnClickListener(v -> {activity.finish();

        });
        spinner_cat = view.findViewById(R.id.spinner_category);
        spinner_language = view.findViewById(R.id.spinner_language);
        tag_tv = view.findViewById(R.id.tag_tv);
        progressDialog = new ProgressDialog(view.getContext(), R.style.Theme_MyDialog);
        progressDialog.setCancelable(false);
        progressDialog.show();

        gif_category_list();
        language_category_list();
        setCustomTag(tag_tv);


        return view;
    }
    private void setCustomTag(NachoTextView tag_tv) {

        tag_tv.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        tag_tv.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        tag_tv.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        tag_tv.addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        tag_tv.setNachoValidator(new ChipifyingNachoValidator());
        tag_tv.enableEditChipOnTouch(true, true);

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(getResources().getString(R.string.app_name)+" ");
        stringBuffer.append("Love"+" ");

        tag_tv.setText(stringBuffer);

    }

    private void gif_category_list() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");

        //Value event listener for realtime data update
        ref.child("All_Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                image_category = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String consultaName = areaSnapshot.getValue(String.class);
                    image_category.add(consultaName);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, image_category);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_cat.setAdapter(arrayAdapter);


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Toast.makeText(getActivity(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (!image_category.get(position).equals("Choose Image Category")) {

                    i_category = "" + image_category.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void language_category_list() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");

        //Value event listener for realtime data update
        ref.child("Language").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                language_category = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String consultaName = areaSnapshot.getValue(String.class);
                    language_category.add(consultaName);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, language_category);

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_language.setAdapter(arrayAdapter);
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Toast.makeText(getActivity(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        spinner_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                lang_category = "" + language_category.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}