package com.free.ahmed.twitterintcore;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.User;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<com.free.ahmed.twitterintcore.User> mUsers;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String nextCursor;
    private boolean loading = true;
    int lastVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_main);

        if (!getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getBoolean(Constants.AUTH, false)){
           startLogin();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rec_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        getFollowersList();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0){
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    lastVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading){
                        if ( (visibleItemCount + lastVisibleItem) >= totalItemCount){
                            loading = false;
                            addListNext(nextCursor);
                        }
                    }
                }
            }
        });
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
            case R.id.menu_change_language:
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

    private void getFollowersList(){
        if (!isInternetConnection()){
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        Call<User> userCall = TwitterCore.getInstance().getApiClient(session).
                getAccountService().verifyCredentials(true, false, false);

        userCall.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                MyTwitterApiClient apiClient = new MyTwitterApiClient(session);

                apiClient.getCustomService().list(response.body().id, 2, 20).enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        BufferedReader reader = null;
                        StringBuilder sb = new StringBuilder();

                        reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                        String line;

                        try {
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String result = sb.toString();
                        JsonObject obj = new Gson().fromJson(result,JsonObject.class);
                        JsonArray usersArray= (JsonArray) obj.get("users");
                        nextCursor = obj.get("next_cursor_str").getAsString();
                        mUsers = new ArrayList<>();
                        for(int i = 0; i < usersArray.size(); i++){
                            JsonObject userObject = (JsonObject) usersArray.get(i);
                            Log.i("json-user",userObject.toString());
                            com.free.ahmed.twitterintcore.User user = new com.free.ahmed.twitterintcore.User();
                            user.setId(userObject.get("id_str").getAsString());
                            user.setBio(userObject.get("description").getAsString());
                            user.setImageUrl(userObject.get("profile_image_url").getAsString());
                            user.setName(userObject.get("name").getAsString());
                            user.setScreenName(userObject.get("screen_name").getAsString());
                            Log.i("twitter-user",user.toString());
                            mUsers.add(user);
                        }

                        updateUi(null);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addListNext(final String next){
        if (!isInternetConnection()){
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        Call<User> userCall = TwitterCore.getInstance().getApiClient(session).
                getAccountService().verifyCredentials(true, false, false);

        userCall.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                MyTwitterApiClient apiClient = new MyTwitterApiClient(session);

                apiClient.getCustomService().nextCursor(response.body().id, 2, 20, next).enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        BufferedReader reader = null;
                        StringBuilder sb = new StringBuilder();

                        reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                        String line;

                        try {
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String result = sb.toString();
                        JsonObject obj = new Gson().fromJson(result,JsonObject.class);
                        JsonArray usersArray= (JsonArray) obj.get("users");
                        nextCursor = obj.get("next_cursor_str").getAsString();
                        for(int i = 0; i < usersArray.size(); i++){
                            JsonObject userObject = (JsonObject) usersArray.get(i);
                            Log.i("json-user",userObject.toString());
                            com.free.ahmed.twitterintcore.User user = new com.free.ahmed.twitterintcore.User();
                            user.setId(userObject.get("id_str").getAsString());
                            user.setBio(userObject.get("description").getAsString());
                            user.setImageUrl(userObject.get("profile_image_url").getAsString());
                            user.setName(userObject.get("name").getAsString());
                            user.setScreenName(userObject.get("screen_name").getAsString());
                            Log.i("twitter-user",user.toString());
                            mUsers.add(user);
                        }

                        updateUi(next);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class FollowersViewHolder extends RecyclerView.ViewHolder{

        ImageView ppView;
        TextView usernameView;
        TextView bioView;

        public FollowersViewHolder(View itemView) {
            super(itemView);

            ppView = itemView.findViewById(R.id.pp_view);
            usernameView = itemView.findViewById(R.id.username_view);
            bioView = itemView.findViewById(R.id.bio_view);
        }
    }

    private class FollowersRecyclerAdapter extends RecyclerView.Adapter<FollowersViewHolder>{

        @Override
        public FollowersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(MainActivity.this).inflate(R.layout.followers_card, parent, false);
            return new FollowersViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FollowersViewHolder holder, final int position) {
            holder.usernameView.setText(mUsers.get(position).getName());
            holder.bioView.setText(mUsers.get(position).getBio());
            Picasso.with(MainActivity.this).load(mUsers.get(position).getImageUrl()).into(holder.ppView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = FollowerActivity.createIntent(mUsers, position, MainActivity.this);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

    }

    public  boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo() == null){
            return false;
        }

        return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void updateUi(String next){
        if (next == null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new LinearLayoutManager(this);
                mRecyclerView.setLayoutManager(mLayoutManager);
            } else {
                mLayoutManager = new GridLayoutManager(this, 3);
                mRecyclerView.setLayoutManager(mLayoutManager);
            }

            mAdapter = new FollowersRecyclerAdapter();
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(lastVisibleItem + 1);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void refresh(){
        updateUi(null);
        mSwipeRefreshLayout.setRefreshing(false);
        loading = true;
    }
}
