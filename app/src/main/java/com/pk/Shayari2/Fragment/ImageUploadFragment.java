package com.pk.Shayari2.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pk.Shayari2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
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


public class ImageUploadFragment extends Fragment {


    private static final int PICK_IMAGE = 100;
    private NachoTextView tag_tv;
    private MaterialButton submit_bn,choose_image_bn;
    private TextInputEditText image_title;
    private ImageView iv_image_upload;
    private Spinner spinner_cat, spinner_language;
    private String i_category, lang_category, i_title;
    private List<String> image_category, language_category;
    private ProgressDialog progressDialog;
    private Uri image_uri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_upload, container, false);
        progressDialog = new ProgressDialog(view.getContext(), R.style.Theme_MyDialog);
        progressDialog.setCancelable(false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_bn);
        getActivity().setTitle("Create New Post");
        toolbar.setNavigationOnClickListener(v -> {activity.finish();

        });
        tag_tv = view.findViewById(R.id.tag_tv);
        choose_image_bn = view.findViewById(R.id.bn_add_imageFile);
        iv_image_upload = view.findViewById(R.id.iv_upload);
        submit_bn = view.findViewById(R.id.bn_submit);
        spinner_cat = view.findViewById(R.id.spinner_cat_image_upload);
        spinner_language = view.findViewById(R.id.spinner_language);
        image_title = view.findViewById(R.id.i_title);

        progressDialog.show();


        image_category_list();
        language_category_list();
        setCustomTag(tag_tv);
        onClick();


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

    private void image_category_list() {

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

    private void onClick() {
        submit_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i_title = image_title.getText().toString();

                if (TextUtils.isEmpty(i_title)) {

                    Toast.makeText(getActivity(), "Enter Title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(i_category)) {

                    Toast.makeText(getActivity(), "Please Select Image Category", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lang_category)) {

                    Toast.makeText(getActivity(), "Please Select Language", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*if (TextUtils.isEmpty(getAllTag())){

                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                    return;
                }*/



                Toast.makeText(getContext(), "" + i_category + "\n" + lang_category, Toast.LENGTH_SHORT).show();
            }
        });

        choose_image_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickImage();

            }
        });

        iv_image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickImage();
            }
        });


    }

    private String getAllTag() {

        //get all tag
        String image_tags = "";
        for (int i = 0; i < tag_tv.getAllChips().size(); i++) {
            if (i == 0) {
                image_tags = image_tags.concat(tag_tv.getAllChips().get(i).toString());
            } else {
                image_tags = image_tags.concat(",");
                image_tags = image_tags.concat(tag_tv.getAllChips().get(i).toString());
            }

        }

        return image_tags;
    }

    private void PickImage() {

      /*  ImagePicker.with(this)
                .galleryOnly()
                //.cropSquare()//Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(PICK_IMAGE);*/

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE){
            assert data != null;
            image_uri = data.getData();

            iv_image_upload.setImageURI(data.getData());
            choose_image_bn.setText("Change Image");


        }else {
        }
    }
}