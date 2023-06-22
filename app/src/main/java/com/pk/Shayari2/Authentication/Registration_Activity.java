package com.pk.Shayari2.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pk.Shayari2.Method.Method;
import com.pk.Shayari2.R;
import com.pk.Shayari2.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class Registration_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE = 150 ;
    private static final String SAMPLE_CROPED_IMG_NAME = "sample";
    private ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;
    private Method method;
    public static ProgressDialog progressDialog;
    private String gender = null;
    private Uri image_uri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        method = new Method(Registration_Activity.this);
        progressDialog = new ProgressDialog(this, R.style.Theme_MyDialog);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing");


        OnClick();
    }
    private void choose_profile_image(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

        binding.ivChooseProfileImage.setImageURI(null);
        binding.ivChooseProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.man_icon));
        image_uri = null;
    }


    private void OnClick() {

        binding.ivChooseProfileImage.setOnClickListener(v -> choose_profile_image());
        binding.ivProfileChoose.setOnClickListener(v -> choose_profile_image());

        binding.male.setOnClickListener(v -> binding.female.setChecked(false));

        binding.female.setOnClickListener(v -> binding.male.setChecked(false));

        binding.buttonSubmit.setOnClickListener(v -> {
            String Name = Objects.requireNonNull(binding.name.getText()).toString();
            String email = Objects.requireNonNull(binding.email.getText()).toString();
            String password = Objects.requireNonNull(binding.password.getText()).toString();
            String Phone = Objects.requireNonNull(binding.phone.getText()).toString();
            String Referral_Code = binding.referenceCode.getText().toString();

            if (TextUtils.isEmpty(Name)) {

                binding.name.setError("Enter full name");
                return;
            }
            if (TextUtils.isEmpty(email)) {

                binding.email.setError("Enter valid email");
                return;
            }
            if (TextUtils.isEmpty(password) || password.length() < 6) {

                binding.password.setError("Enter 6 digit + password");
                return;
            }
            if (TextUtils.isEmpty(Phone)) {

                binding.phone.setError("Enter phone number");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.email.setError("Invalid Email");
                return;
            }

            if (binding.male.isChecked()) {

                gender = "male";
            }

            if (binding.female.isChecked()) {

                gender = "Female";
            }

            if (TextUtils.isEmpty(gender)) {

                Toast.makeText(Registration_Activity.this, "Please Choose Gender", Toast.LENGTH_SHORT).show();
                return;
            }


            progressDialog.show();
           startLogin(email, password);


        });
    }


    private void startLogin(String email, String password) {


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Auth.. Successful
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String id = user.getUid();
                        String name = binding.name.getText().toString();
                        String email = user.getEmail();

                        String phoneNumber = binding.phone.getText().toString();

                        if (task.isSuccessful()) {

                            if (image_uri == null){

                                method.Store_Registration_Details(name, email, phoneNumber, gender, id, "null",
                                        "custom", "");

                            }else {

                                String filepatAndname = "Users_Profile_Image/" + System.currentTimeMillis();

                                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepatAndname);
                                ref.putFile(image_uri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                Task<Uri> uritask = taskSnapshot.getStorage().getDownloadUrl();
                                                while (!uritask.isSuccessful()) ;
                                                String downloaduri = uritask.getResult().toString();

                                                method.Store_Registration_Details(name, email, phoneNumber, gender,
                                                        id, downloaduri,
                                                        "custom", "");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {

                                                progressDialog.dismiss();
                                                Toast.makeText(Registration_Activity.this, "Error :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }


                        } else {

                            progressDialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Registration_Activity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }
                });

    }

    public void save_Login_Details() {

        String email = binding.email.getText().toString();
        String password  = binding.password.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences(Login_Activity.mypreference, MODE_PRIVATE).edit();
        editor.putString(Login_Activity.pref_email, email);
        editor.putString(Login_Activity.pref_password, password);
        editor.putBoolean(Login_Activity.pref_check, true);
        editor.apply();

    }

    public void Already_Register(View view) {

        startActivity(new Intent(this, Login_Activity.class));
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE){

            startcrop(data.getData());

        }else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {

            image_uri = UCrop.getOutput(data);
            binding.ivChooseProfileImage.setImageURI(image_uri);




        }
    }

    private void startcrop(@NonNull Uri uri) {

        String des = SAMPLE_CROPED_IMG_NAME;
        des += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), des)));
        //uCrop.withAspectRatio(1,1);
        uCrop.useSourceImageAspectRatio();
        uCrop.withAspectRatio(1,1);
        // uCrop.withAspectRatio(2,3);
        // uCrop.withAspectRatio(16,9);

        uCrop.withMaxResultSize(512, 512);
        uCrop.withOptions(getCrop());
        uCrop.start(this);

    }

    private UCrop.Options getCrop() {

        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        // options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        options.setHideBottomControls(false);
        options.setImageToCropBoundsAnimDuration(1000);
        options.setFreeStyleCropEnabled(true);


        //color
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        options.setToolbarTitle("Edit Image");
        return options;

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Registration_Activity.this, Login_Activity.class));
        finish();
    }

}