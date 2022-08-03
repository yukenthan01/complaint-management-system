package com.example.androidcoursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

public class UserDashboardActivity extends AppCompatActivity  {
    public String finalUserRole;
    public String finalfullname;
    public String finalProfieImage;
    TextView fullnameTextView,hiddenUserRole;
    ImageView profileImageView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    View haderXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        CheckUserAccessLevel checkUserAccessLevel = new CheckUserAccessLevel();//role and full name
        checkUserAccessLevel.checkUserLevel(role -> { finalUserRole = role;}); //// validan admin
        // or user
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        haderXml = navigationView.getHeaderView(0);
        profileImageView = haderXml.findViewById(R.id.profilepicnew);

        checkUserAccessLevel.getFullname(fullname->{
            fullnameTextView = findViewById(R.id.fullname);
            fullnameTextView.setText(fullname);
//
            finalfullname = fullname;
        });
        checkUserAccessLevel.getProfileImageUrl(imageUrl ->  {
            finalProfieImage = imageUrl;
            Glide.with(profileImageView.getContext()).load(imageUrl).into
             (profileImageView);
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                drawerLayout.closeDrawer(GravityCompat.START);

                if(finalUserRole.equals("user")){
                    switch (id)
                    {
                    case R.id.nav_new_compliant:
                        toolbar.setTitle(R.string.new_compliant);
                        item.setChecked(true);
                        replaceFragment(new NewComplaintFragment());
                        break;

                    case R.id.nav_viewed_compliant:
                        toolbar.setTitle(R.string.viewed_compliants);
                        item.setChecked(true);
                        replaceFragment(new ViewComplaintsFragment(finalUserRole));
                        Toast.makeText(UserDashboardActivity.this, "Message is Clicked",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_user_view:
                        toolbar.setTitle(R.string.tittle_profile);
                        item.setChecked(true);
                        replaceFragment(new ProfileFragment());
                        break;
                    case R.id.nav_password_change:
                        toolbar.setTitle(R.string.nav_user_credentials);
                        item.setChecked(true);
                        replaceFragment(new PasswordChangeFragment());
                        break;
                    case R.id.logout:
                        item.setChecked(true);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(),
                                LoginActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        return true;
                }
                }
                else if(finalUserRole.equals("admin")){
                    switch (id)
                    {
                        case R.id.nav_viewed_compliant:
                            toolbar.setTitle(R.string.viewed_compliants);
                            item.setChecked(true);
                            replaceFragment(new ViewComplaintsFragment());
                            Toast.makeText(UserDashboardActivity.this, "Message is Clicked",Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.nav_user_view:
                            toolbar.setTitle(R.string.tittle_profile);
                            item.setChecked(true);
                            replaceFragment(new ProfileFragment());
                            break;
                        case R.id.nav_password_change:
                            toolbar.setTitle(R.string.nav_user_credentials);
                            item.setChecked(true);
                            replaceFragment(new PasswordChangeFragment());
                            break;
                        case R.id.logout:
                            item.setChecked(true);
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getApplicationContext(),
                                    LoginActivity.class);
                            startActivity(intent);
                            break;

                        default:
                            return true;
                    }
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}