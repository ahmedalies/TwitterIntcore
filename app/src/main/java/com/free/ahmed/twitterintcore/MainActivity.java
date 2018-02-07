package com.free.ahmed.twitterintcore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twitter.sdk.android.core.Twitter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getBoolean(Constants.AUTH, false)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
