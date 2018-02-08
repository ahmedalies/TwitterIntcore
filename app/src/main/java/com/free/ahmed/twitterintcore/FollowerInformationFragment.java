package com.free.ahmed.twitterintcore;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * Created by ahmed on 2/8/2018.
 */

public class FollowerInformationFragment extends Fragment {

    private static final String USER_EXTRA = "user_data_extra";
    User mUser;

    TextView mTextView;
    ImageView ppImageView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    UserTimeline mUserTimeline;
    RecyclerView mRecyclerView;

    public static FollowerInformationFragment createFragment(User user){
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_EXTRA, user);

        FollowerInformationFragment fragment = new FollowerInformationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User) getArguments().getSerializable(USER_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follower_information, container, false);

        mCollapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(mUser.getName());

        ppImageView = view.findViewById(R.id.pp_view);

        ppImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ImageBrowserActivity.createIntent(getActivity(), mUser.getImageUrl());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ppImageView,
                        getResources().getString(R.string.pp_shared));
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, optionsCompat.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        Picasso.with(getActivity()).load(mUser.getImageUrl()).into(ppImageView);
        mRecyclerView = view.findViewById(R.id.tweets_rec_view);

        mUserTimeline = new UserTimeline.Builder().screenName(mUser.getScreenName()).maxItemsPerRequest(10).build();
        final TweetTimelineRecyclerViewAdapter adapter = new TweetTimelineRecyclerViewAdapter.Builder(getActivity())
                .setTimeline(mUserTimeline).setViewStyle(R.style.tw__TweetLightWithActionsStyle).build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);

        return view;
    }
}
