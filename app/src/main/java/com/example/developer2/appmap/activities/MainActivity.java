package com.example.developer2.appmap.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.developer2.appmap.R;
import com.example.developer2.appmap.fragments.MapFragment;
import com.example.developer2.appmap.fragments.WelcomeFragment;

public class MainActivity extends AppCompatActivity {

    Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            currentFragment = new WelcomeFragment();
            changeFragment(currentFragment);
        }else{

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.menu_welcome:
                currentFragment = new WelcomeFragment();
                break;
            case R.id.menu_map:
                currentFragment = new MapFragment();
                break;
        }
        changeFragment(currentFragment);
        return super.onOptionsItemSelected(item);
    }
    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();

    }

}
