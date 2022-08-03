package com.example.androidcoursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    public String finaluserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.admin_dashboard);
    }
}
        //start
//        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.navigation_view);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });
//        CheckUserAccessLevel checkUserAccessLevel = new CheckUserAccessLevel();
//        checkUserAccessLevel.checkUserLevel(role -> { finaluserRole = role;}); //// validan admin
//        // or user
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                drawerLayout.closeDrawer(GravityCompat.START);
//
//                switch (id)
//                {
//                    case R.id.nav_new_compliant:
//
//
//                        Log.d("TAG", "onCreate: "+finaluserRole);
//                        Toast.makeText(MainActivity.this, "Home is Clicked"+finaluserRole,
//                                Toast.LENGTH_SHORT).show();
////                        getSupportFragmentManager().beginTransaction().replace(R.id
////                                .fragment_container,new HomeFragment()).commit();
//                        break;
//
//                    case R.id.nav_viewed_compliant:
////                        getSupportFragmentManager().beginTransaction().replace(R.id
////                                .fragment_container,new MessageFragment()).commit();
//                        Toast.makeText(MainActivity.this, "Message is Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.nav_category:
//                        Toast.makeText(MainActivity.this, "Synch is Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.trash:
//                        Toast.makeText(MainActivity.this, "Trash is Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.settings:
//                        Toast.makeText(MainActivity.this, "Settings is Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.nav_share:
//                        Toast.makeText(MainActivity.this, "Share is clicked",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.nav_rate:
//                        Toast.makeText(MainActivity.this, "Rate us is Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    default:
//                        return true;
//                }
//                return true;
//            }
//        });
//    }
//
//    public void onCallback(String userRole) {
//        finaluserRole = userRole;
//    }

//}