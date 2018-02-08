package com.free.ahmed.twitterintcore;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import static com.free.ahmed.twitterintcore.R.id.follower_pager;

public class FollowerActivity extends AppCompatActivity {

    private static final String LIST_EXTRA = "followers_extra";
    private static final String POSITION_EXTRA = "follower_position_extra";

    ArrayList<User> mUsers;
    int startPos;
    ViewPager mViewPager;


    public static Intent createIntent(ArrayList<User> users, int pos, Context context){
        Intent intent = new Intent(context, FollowerActivity.class);
        intent.putExtra(POSITION_EXTRA, pos);
        intent.putExtra(LIST_EXTRA, users);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        mViewPager = (ViewPager) findViewById(follower_pager);

        mUsers = (ArrayList<User>) getIntent().getSerializableExtra(LIST_EXTRA);
        startPos = getIntent().getIntExtra(POSITION_EXTRA, 0);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FollowerInformationFragment.createFragment(mUsers.get(position));
            }

            @Override
            public int getCount() {
                return mUsers.size();
            }
        });

        mViewPager.setCurrentItem(startPos);
    }

}
