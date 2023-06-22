package com.pk.Shayari2.Method;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.pk.Shayari2.Authentication.Registration_Activity;
import com.pk.Shayari2.BuildConfig;
import com.pk.Shayari2.R;
import com.pk.Shayari2.activity.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;

public class Method {
    private final Activity activity;
    private final int cover_image_index = new Random().nextInt(9);


    public Method(Activity activity) {
        this.activity = activity;


    }

    //Network check
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //alert message box
    public void alertBox(String message) {

        if (!activity.isFinishing()) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
            builder.setMessage(Html.fromHtml(message));
            builder.setCancelable(false);
            builder.setPositiveButton(activity.getResources().getString(R.string.ok),
                    (arg0, arg1) -> {

                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    public void Store_Registration_Details(final String name,final String email, String phone,String gender,
                                           String uid, String photourl, String auth_Types,
                                           String location) {

        Map<Object, Object> map = new HashMap<>();

        map.put("Name", "" + name);
        map.put("Account_Type", "User");
        map.put("USER_NAME", "" +generateUsernameFromEmailId(email)) ;
        map.put("Email", "" + email);
        map.put("Phone", "" + phone);
        map.put("Gender", "" + gender);
        map.put("Profile_Image", "" + photourl);
        map.put("Cover_Image", "" + generateCoverImage().get(cover_image_index));
        map.put("Auth_Types", "" + auth_Types);
        map.put("uid", "" + uid);
        map.put("Location", ""+location);
        map.put("ID_NUMBER", "");
        map.put("ID_TYPE", "");
        map.put("ID_IMAGE", "");
        map.put("Verified_Status", false);
        map.put("Device_Id", android_Id());
        map.put("Language", "English");
        map.put("create_Time", "" + currentDateAndTime());

        FirebaseDatabase.getInstance().getReference("Users").child(uid)
                .setValue(map)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        loginSucessFul();


                    }else {
                        Registration_Activity.progressDialog.dismiss();
                        error_on_storing_data();
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(activity, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                   Registration_Activity.progressDialog.dismiss();
                    error_on_storing_data();
                });
    }

    @SuppressLint("HardwareIds")
    public  String android_Id(){

        return Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    private void error_on_storing_data(){

        FirebaseAuth.getInstance().getCurrentUser().delete()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        FirebaseAuth.getInstance().signOut();
                    }
                }).addOnFailureListener(e -> {


        });
    }

    public String generateUsernameFromEmailId(String email) {

        StringTokenizer st = new StringTokenizer(email, "@");

        Random random = new Random();

        int n1= random.nextInt(20);
        int n2 = random.nextInt(15);
        int n3 = random.nextInt(8);

        int finalNumber = (n1+n2)-n3;

        System.out.println("pppppppp"+n1+"+"+n2+"-"+n3+"=="+finalNumber);

        String u_name = st.nextToken();
        int user_name_Length = 10;

        if (u_name.length() > user_name_Length) {

            return u_name.substring(0, user_name_Length)+ finalNumber;

        } else {
            return u_name;
        }
    }

    private List<String> generateCoverImage() {

        List<String> list = new ArrayList<>();

        list.add("https://cdn.pixabay.com/photo/2017/08/30/01/05/milky-way-2695569__480.jpg");
        list.add("https://1stwebdesigner.com/wp-content/uploads/2019/07/css-background-effects-thumb.jpg");
        list.add("https://static1.makeuseofimages.com/wordpress/wp-content/uploads/2017/02/Photoshop-Replace-Background-Featured.jpg");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqhG3oXZJMdiHKLGc450PHjJV-fQdMyx95yQ&usqp=CAU");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ2dPh-oBpEYYHuL3ok2hfQx1ZEInsuDIxxCA&usqp=CAU");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm88LuK1hj4hwAWNWOytvvtAmkkbfSsveRYA&usqp=CAU");
        list.add("https://images.pexels.com/photos/235986/pexels-photo-235986.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRYHd4nhm7Ji3L0DMrHi3dYJ9_x0X_3zM4qkQ&usqp=CAU");
        list.add("https://kreditings.com/wp-content/uploads/2020/06/CB-Edit-Photo-Editing-background-Download-6.jpg");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlESRl5v6mAgChfzXDnd1CeikfDZB7ZhmUAw&usqp=CAU");

        return list;
    }

    public String currentDateAndTime() {

        String timestamp = String.valueOf(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));

        return DateFormat.format("dd-MM-yyyy hh:mm aa ", calendar).toString();

    }

    private void loginSucessFul() {

        Toast.makeText(activity, "Account Create Successful ", Toast.LENGTH_SHORT).show();

        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();

        if ( Registration_Activity.progressDialog != null){
            Registration_Activity.progressDialog.dismiss();

        }

    }

    public void forgetEmail() {

        final Dialog dailog = new Dialog(activity);
        dailog.setContentView(R.layout.design_forget_email);
        dailog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dailog.setCancelable(false);
        dailog.setCanceledOnTouchOutside(false);
        dailog.getWindow().getAttributes().windowAnimations = R.style.animaton;

        final TextInputEditText ed_email = dailog.findViewById(R.id.email);
        final MaterialTextView tv_error = dailog.findViewById(R.id.tv_error);
        final MaterialButton bn_sendEmail = dailog.findViewById(R.id.bn_forget);
        final FloatingActionButton close_bn = dailog.findViewById(R.id.close_bn);

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);

        close_bn.setOnClickListener(v -> dailog.dismiss());

        bn_sendEmail.setOnClickListener(view -> {
            String email = ed_email.getText().toString();

            if (TextUtils.isEmpty(email)) {

                ed_email.setError("Please Enter Valid Email");
                return;
            }

            progressDialog.setMessage("Processing...");
            progressDialog.show();
            tv_error.setVisibility(View.VISIBLE);

            FirebaseAuth.getInstance().
                    sendPasswordResetEmail(Objects.requireNonNull(ed_email.getText()).toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            tv_error.setVisibility(View.VISIBLE);
                            dailog.dismiss();
                            tv_error.setText("Send Successful Please Check Email");

                            progressDialog.dismiss();

                            Toast.makeText(activity, "Send Successful Please Check Email", Toast.LENGTH_LONG).show();
                        } else {
                            tv_error.setText(task.getException().toString());
                            progressDialog.dismiss();
                        }

                    }).addOnFailureListener(e -> {
                        tv_error.setText("" + e.getMessage());
                        progressDialog.dismiss();

                        Toast.makeText(activity, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        });


        dailog.setCancelable(true);
        dailog.show();
    }

    public void permission_Check() {

        Dexter.withContext(activity)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                )
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.isAnyPermissionPermanentlyDenied()) {

                            activity.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" +
                                    BuildConfig.APPLICATION_ID)));
                            Toast.makeText(activity, "  Permission Denied" + "\n\n" + "Click : " + "\n\n" +
                                            "1. App Permissions " + "\n\n" +
                                            "2. Storage," + "\n\n" +
                                            "3. Click Allow Button",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();


                    }

                }).check();


    }


}

