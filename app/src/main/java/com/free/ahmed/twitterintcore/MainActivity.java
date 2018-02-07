package com.free.ahmed.twitterintcore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.twitter.sdk.android.core.Twitter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getBoolean(Constants.AUTH, false)){
           startLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit()
                        .putBoolean(Constants.AUTH, false).apply();
                startLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
