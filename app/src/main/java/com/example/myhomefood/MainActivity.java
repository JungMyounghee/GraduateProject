package com.example.myhomefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    SelectFragment selectFragment;
    SearchFragment searchFragment;
    MyPageFragment myPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectFragment = new SelectFragment();
        searchFragment = new SearchFragment();
        myPageFragment = new MyPageFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, selectFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationView);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.selectIngredient:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, selectFragment).commit();
                        return true;
                    case R.id.searchRecipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, searchFragment).commit();
                        return true;
                    case R.id.myPage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, myPageFragment).commit();
                        return true;
                }
                return false;
            }
        });

        //getAppKeyHash();
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("name not found", e.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
