package com.free.ahmed.twitterintcore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_login);

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                login(session);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void login(TwitterSession session){
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        String name = session.getUserName();

        getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putBoolean(Constants.AUTH, true).apply();

        getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putString(Constants.TOKEN, token).apply();

        getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putString(Constants.SECRET, secret).apply();

        getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putString(Constants.USERNAME, name).apply();


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
