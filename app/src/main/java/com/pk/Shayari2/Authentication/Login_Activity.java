package com.pk.Shayari2.Authentication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pk.Shayari2.Method.Method;
import com.pk.Shayari2.R;
import com.pk.Shayari2.activity.MainActivity;
import com.pk.Shayari2.databinding.ActivityLoginBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Login_Activity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    private Method method;
    public static final String mypreference = "mypref";
    public static final String pref_check = "pref_check";

    public static final String pref_email = "pref_email";
    public static final String pref_password = "pref_password";

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    public static FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    //Google login
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 007;

    // exit
    boolean doubleBackToExitPressedOnce = false;

    //Facebook login
    private CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;

    private ProgressDialog progressDialog;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        method = new Method(Login_Activity.this);

        method.permission_Check();

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this, R.style.Theme_MyDialog);
        progressDialog.setCancelable(false);

        pref = getSharedPreferences(mypreference, 0); // 0 - for private mode
        editor = pref.edit();


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {


            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    firebaseAuth.signOut();
                }
            }
        };

        // setClickListener
        Button_Click();

        CheckPassWordSave();

    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            startActivity(new Intent(Login_Activity.this, MainActivity.class));
            finish();

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    protected void Button_Click() {

        //facebook button
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();

        binding.facebookLoginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (method.isNetworkAvailable()) {

                    progressDialog.setMessage("Processing...");
                    progressDialog.show();

                    binding.loginButton.performClick();

                } else {

                    method.alertBox(getResources().getString(R.string.internet_connection));
                }

            }
        });


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        binding.loginButton.setReadPermissions("email", "public_profile");
        binding.loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                progressDialog.dismiss();

                Toast.makeText(Login_Activity.this, "facebook:onCancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                progressDialog.dismiss();
                Toast.makeText(Login_Activity.this, "facebook:onError", Toast.LENGTH_SHORT).show();

            }
        });


        binding.forgetBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (method.isNetworkAvailable()) {

                    method.forgetEmail();

                } else {

                    method.alertBox(getResources().getString(R.string.internet_connection));
                }
            }
        });

        //Google Login
        binding.googleLoginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (method.isNetworkAvailable()) {
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                } else {

                    method.alertBox(getResources().getString(R.string.internet_connection));
                }
            }
        });
        binding.buttonLoginActivity.
                setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!method.isNetworkAvailable()) {

                    method.alertBox(getResources().getString(R.string.internet_connection));
                    return;

                }

                String email = Objects.requireNonNull(binding.email.getText()).toString();
                String password = Objects.requireNonNull(binding.password.getText()).toString();

                if (TextUtils.isEmpty(email)) {

                    binding.email.setError("Enter valid email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {

                    binding.password.setError("Enter password");
                    return;
                }

                if (binding.checkboxRemember.isChecked()) {

                    editor.putString(pref_email, binding.email.getText().toString());
                    editor.putString(pref_password, binding.password.getText().toString());
                    editor.putBoolean(pref_check, true);
                    editor.apply();


                }


                startLogin();

            }
        });

    }

    protected void startLogin() {
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        String email = Objects.requireNonNull(binding.email.getText()).toString();
        String password = Objects.requireNonNull(binding.password.getText()).toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log_In_Successful(email, password);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Login_Activity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });


    }

    private void Log_In_Successful(String email, String password) {

        progressDialog.dismiss();

        Toast.makeText(this, "Log In Successful", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(Login_Activity.this, MainActivity.class));
        finish();

    }


    public void Register(View view) {

        startActivity(new Intent(this, Registration_Activity.class));
        finish();
    }

    public void set(View view) {

        firebaseAuth.signOut();
        finish();
    }

    //Google login get callback
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                progressDialog.dismiss();
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);

                            boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();


                            if (newuser) {

                                // New User
                                String id = user.getUid();
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                String photo_Url = String.valueOf(user.getPhotoUrl());
                                String phoneNumber = user.getPhoneNumber();

                                method.Store_Registration_Details(name, email, phoneNumber, "", id, photo_Url,
                                        "google", "");

                            } else {

                                // Old User
                                Log_In_Successful("" + user.getEmail(), "");

                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login_Activity.this, "Error : " + task.getException(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        binding.loginButton.setLogoutText("Processing...");

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);

                            boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();


                            if (newuser) {

                                // New User
                                String id = user.getUid();
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                Uri photo_Url = user.getPhotoUrl();
                                String phoneNumber = user.getPhoneNumber();

                                method.Store_Registration_Details(name, email, phoneNumber, "", id, String.valueOf(photo_Url),
                                        "facebook", "");

                            } else {

                                // Old User
                                assert user != null;
                                Log_In_Successful("" + user.getEmail(), "");

                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            // Toast.makeText(Login_Activity.this, "Error : "+task.getException(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

                String error = "An account already exists with the same email address but " +
                        "different sign-in credentials. Sign in using a provider associated with this email address.";
                LoginManager.getInstance().logOut();

                Toast.makeText(Login_Activity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Error : " + e.getMessage());
                if (Objects.equals(e.getMessage(), error)) {

                    LoginManager.getInstance().logOut();

                }
                progressDialog.dismiss();
            }
        });
    }

    private void CheckPassWordSave() {

        if (pref.getBoolean(pref_check, false)) {

            binding.email.setText(pref.getString(pref_email, null));
            binding.password.setText(pref.getString(pref_password, null));

            binding.checkboxRemember.setChecked(true);
        } else {
            binding.email.setText("");
            binding.password.setText("");
            binding.checkboxRemember.setChecked(false);
        }

    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

      /*  new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);*/
    }
}