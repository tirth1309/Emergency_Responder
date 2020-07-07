package com.minorproject.android.esrf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minorproject.android.esrf.Fragment.FirstAid;
import com.minorproject.android.esrf.Fragment.HomeFragment;
import com.minorproject.android.esrf.Fragment.UserFragment;
import com.minorproject.android.esrf.Helping_Classes.statics;
import com.minorproject.android.esrf.Models.User;
import com.minorproject.android.esrf.Services.LocationBgService;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolBar;
    public Intent serviceIntent;
    public User currUser;
    public static final String TAG ="Main Activity";
    public String uid;
    public DatabaseReference dbref;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currUser = (User) getIntent().getSerializableExtra("curruser");
        Log.d("User in MainAct",currUser.name);

        serviceIntent = new Intent(MainActivity.this, LocationBgService.class);
        serviceIntent.putExtra("curruser",currUser);

        handlePermissions();

        startService(serviceIntent);

        toolBar = getSupportActionBar();
        BottomNavigationView nav = findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bundle  = new Bundle();
        bundle.putSerializable("curruser",currUser);
        toolBar.setTitle("Home");
        Fragment frag = new HomeFragment();
        frag.setArguments(bundle);
        loadFragment(frag);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference tokenref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("tokens", 0); // 0 - for private mode
        statics.token = pref.getString("token_value",null);
        tokenref.setValue(statics.token);
    }





    private void handlePermissions() {
        int locationRequestCode = 1000;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, locationRequestCode);
        else{
            startService(serviceIntent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startService(serviceIntent);
                }
                else
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_user:
                    toolBar.setTitle("User");
                    fragment = new UserFragment();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_home:
                    toolBar.setTitle("Home");
                    fragment = new HomeFragment();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_firstAid:
                    toolBar.setTitle("First Aid");
                    fragment = new FirstAid();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }






}
