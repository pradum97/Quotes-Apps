package com.pk.Shayari2.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.pk.Shayari2.Method.Upload_Post;
import com.pk.Shayari2.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.validator.ChipifyingNachoValidator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;


public class Quotes_Upload_Fragment extends Fragment {

    private  View view;

    private int position;
    private String q_category,q_language, font_style;
    private int quotesColor_bg = 0x7F313C93;
    private MaterialToolbar toolbar;
    private LinearLayout llMain;
    public static ProgressDialog progressDialog;
    private MaterialTextView textViewNoData;
    private Dialog dialog;
    private Spinner spinner_category,spinner_lang;
    private NachoTextView tag_tv;
    private TextInputEditText quotes_tv;
    private MaterialButton button_upload;
    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;
    private ImageView imageView_color;
    private RelativeLayout relativeLayout_textStyle;
    private List<String> quotes_category, language_category;
    private final String[] font = {"Anton.ttf", "Cinzel.ttf",
            "Lemonada.ttf", "Pacifico.ttf", "Poppins.ttf", "Roboto.ttf"};
    long page_number ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_quotes__upload, container, false);

        progressDialog = new ProgressDialog(view.getContext(), R.style.Theme_MyDialog);
        progressDialog.setCancelable(false);
        Toolbar toolbar = view.findViewById(R.id.toolbar_qu);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_bn);
        getActivity().setTitle("Upload Quotes");
        toolbar.setNavigationOnClickListener(v -> {activity.finish();});
        llMain =view. findViewById(R.id.ll_main_qu);
        spinner_category = view.findViewById(R.id.spinner_qu);
        imageView_color = view.findViewById(R.id.imageView_colorSelect_qu);
        relativeLayout_textStyle = view.findViewById(R.id.rel_textStyle_qu);
        button_upload = view.findViewById(R.id.button_qu);
        spinner_lang = view.findViewById(R.id.spinner_lang);
        quotes_tv = view.findViewById(R.id.editText_qu);
        constraintLayout = view.findViewById(R.id.constrainLayout_bg_qu);
        tag_tv = view.findViewById(R.id.nacho_qu);

        quotes_tv.setTextColor(getResources().getColor(R.color.white));

        tag_Text_view();

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_color);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.FILL_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        ColorPickerView colorPickerView = dialog.findViewById(R.id.colorPicker_dialog_color);
        MaterialButton button_dialog = dialog.findViewById(R.id.button_dialog_color);
        colorPickerView.setInitialColor(0x7F313C93);
        constraintLayout.setBackgroundColor(quotesColor_bg);

        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), "text_font/" + font[position]);
        quotes_tv.setTypeface(typeface);
        font_style = font[position];

        num();


        quotes_category_list();
        language_category_list();

        onClick(colorPickerView,button_dialog);



        return  view;
    }

    private void tag_Text_view() {

        tag_tv.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        tag_tv.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        tag_tv.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        tag_tv.addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        tag_tv.setNachoValidator(new ChipifyingNachoValidator());
        tag_tv.enableEditChipOnTouch(true, true);
        setCustomTag(tag_tv);


    }
    private void setCustomTag(NachoTextView tag_tv) {

        StringBuffer tag_list = new StringBuffer();

        tag_list.append(getResources().getString(R.string.app_name)).append(" ");
        tag_list.append("Love").append(" ");

        tag_tv.setText(tag_list);

       // tag_tv.setEnabled(false);
        tag_tv.setOnChipClickListener(new NachoTextView.OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip, MotionEvent event) {

                Toast.makeText(getActivity(), ""+chip.getText(), Toast.LENGTH_SHORT).show();

                tag_tv.setActivated(false);
            }
        });
    }

    private void onClick(ColorPickerView colorPickerView,
                         MaterialButton button_dialog) {
        button_upload.setOnClickListener(v -> {

            Upload_Post upload_post = new Upload_Post(getActivity());
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String quotes = quotes_tv.getText().toString();
            int quotes_background_color =quotesColor_bg;
            int quotes_text_color =quotes_tv.getCurrentTextColor();
            String fontStyle = font_style;
            String quotes_category = q_category;
            String language = q_language;
            String tag = getAllTag();
            String email = user.getEmail();
            String uid = user.getUid();

            if (TextUtils.isEmpty(quotes)){
                quotes_tv.setError("Enter Quotes");
                return;
            }
            if (TextUtils.isEmpty(quotes_category)){
                Toast.makeText(getActivity(), "Please choose any category", Toast.LENGTH_SHORT).show();

                return;
            }
            if (TextUtils.isEmpty(tag)){
                Toast.makeText(getActivity(), "Create new tag", Toast.LENGTH_SHORT).show();

                return;
            }

            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            upload_post.upload_post(page_number,email,uid,quotes,fontStyle,
                    quotes_background_color,quotes_category,language,getAllTag(),quotes_text_color
            ,"quotes","noimage","novideo","no_thumb");


        });

        relativeLayout_textStyle.setOnClickListener(v -> {
            position++;
            if (position > font.length - 1) {
                position = 0;
            }
            font_style = font[position];
            Typeface typeface1 = Typeface.createFromAsset(view.getContext().getAssets(), "text_font/" + font[position]);
            quotes_tv.setTypeface(typeface1);
        });


        colorPickerView.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
                constraintLayout.setBackgroundColor(color);
                quotesColor_bg = color;
            }
        });

        button_dialog.setOnClickListener(v -> dialog.dismiss());


        imageView_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.create();
                dialog.show();
            }
        });

    }


    private void quotes_category_list() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");

        //Value event listener for realtime data update
        ref.child("All_Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                quotes_category = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {

                    String consultaName = areaSnapshot.getValue(String.class);
                    quotes_category.add(consultaName);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, quotes_category);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_category.setAdapter(arrayAdapter);


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Toast.makeText(getActivity(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (!quotes_category.get(position).equals("Choose Category")) {

                   q_category = "" + quotes_category.get(position);

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
                spinner_lang.setAdapter(arrayAdapter);
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Toast.makeText(getActivity(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               q_language = "" + language_category.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                image_tags = image_tags.concat(" ");
                image_tags = image_tags.concat(tag_tv.getAllChips().get(i).toString());
            }

        }

        return image_tags;
    }

    private void num (){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("home");

        reference.child("Quotes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                long l = snapshot.getChildrenCount();
                l++;

                page_number = l;

               progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}