package com.pk.Shayari2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.pk.Shayari2.Authentication.Login_Activity;
import com.pk.Shayari2.Fragment.Favorite_Fragment;
import com.pk.Shayari2.Fragment.Home_Main_Fragment;
import com.pk.Shayari2.Fragment.Profile_Fragment;
import com.pk.Shayari2.Fragment.Reward_Fragment;
import com.pk.Shayari2.Fragment.Setting_Fragment;
import com.pk.Shayari2.Method.Method;
import com.pk.Shayari2.R;
import com.pk.Shayari2.databinding.ActivityMainBinding;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    private Method method;
    public MaterialToolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    final Fragment fragment1 = new Home_Main_Fragment();
    final Fragment fragment2 = new Reward_Fragment();
    final Fragment fragment3 = new Favorite_Fragment();
    final Fragment fragment4 = new Profile_Fragment();
    protected FragmentManager fm = getSupportFragmentManager();
    protected Fragment active = fragment1;

    private  ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this, R.style.Theme_MyDialog);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing");

        progressDialog.show();

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.home));


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_side_nav);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            navigationView.getMenu().getItem(7).setTitle("Log Out");
        }else {

            navigationView.getMenu().getItem(7).setTitle("Login");
        }


        final View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        MaterialTextView textView_appName = headerLayout.findViewById(R.id.textView_name_nav);


        navigationView.getMenu().getItem(5).setVisible(true);

        tabLayout();



    }

    @Override
    protected void onStart() {
        super.onStart();
        check_user_();
    }

    protected void check_user_() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    progressDialog.dismiss();

                }else {

                    FirebaseAuth.getInstance().getCurrentUser()
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                            if (task.isSuccessful()){

                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                startActivity(new Intent(MainActivity.this,Login_Activity.class));

                                finish();

                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void tabLayout() {

        fm.beginTransaction().add(R.id.container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.container, fragment1, "1").commit();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.getTabAt(2)
                .setCustomView(R.layout.tab_uoloadicon);


        ImageView imageView = tabLayout.getTabAt(2)
                .getCustomView().findViewById(R.id.image);

        Glide.with(this).load(R.drawable.plus_icon_gif).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_down);
                imageView.startAnimation(animation);*/

                Intent intent = new Intent(MainActivity.this, Upload_Activity.class);
                startActivity(intent);
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {



                switch (tab.getPosition()) {

                    case 0:
                        backStackRemove();
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        toolbar.setTitle(tab.getText());


                        break;

                    case 1:
                        backStackRemove();
                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;
                        toolbar.setTitle(tab.getText());


                        break;

                    case 2:
                      // Add Post

                        break;

                    case 3:
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        toolbar.setTitle(tab.getText());


                        break;

                    case 4:

                        fm.beginTransaction().hide(active).show(fragment4).commit();
                        active = fragment4;
                        toolbar.setTitle(tab.getText());



                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {



            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });


    }

    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());

        //Closing drawer on item click
        drawer.closeDrawers();
        // Handle bottom_navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.home:
                toolbar.setTitle(item.getTitle());
                selectDrawerItem(0);
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.category:
                toolbar.setTitle(item.getTitle());
                selectDrawerItem(1);

                return true;

            case R.id.download:
                toolbar.setTitle(item.getTitle());

                return true;

            case R.id.upload:
                toolbar.setTitle(item.getTitle());
                //deselectDrawerItem(3);
                selectDrawerItem(3);

                return true;

            case R.id.reference_code:
                toolbar.setTitle(item.getTitle());
                selectDrawerItem(4);

                backStackRemove();

                return true;

            case R.id.earn_point:
                toolbar.setTitle(item.getTitle());
                deselectDrawerItem(5);

              //  startActivity(new Intent(MainActivity.this, Spinner.class));
                return true;

            case R.id.setting:
                toolbar.setTitle(item.getTitle());
                selectDrawerItem(6);

                backStackRemove();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Setting_Fragment(),
                                getResources().getString(R.string.setting)).commit();
                return true;

            case R.id.login:

            //  method.logout();

                CharSequence title = navigationView.getMenu().getItem(7).getTitle();

                if ("Log Out".equals(title)) {
                    // logout Action

                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(MainActivity.this, Login_Activity.class));
                    finish();

                } else if ("Login".equals(title)) {

                    // Login Action

                }


              return true;

            default:

                return true;
        }
    }


    public void selectDrawerItem(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    public void deselectDrawerItem(int position) {
        navigationView.getMenu().getItem(position).setCheckable(false);
        navigationView.getMenu().getItem(position).setChecked(false);
    }

    public void backStackRemove() {

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void unCheck() {

        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

}