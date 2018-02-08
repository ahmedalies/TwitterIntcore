package com.free.ahmed.twitterintcore;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageBrowserActivity extends AppCompatActivity  {

    private static final String IMAGE_URL_EXTRA = "image_url";

    ScaleGestureDetector mDetector;

    String imageUrl;
    ImageView mImageView;

    public static Intent createIntent(Context context, String imageUrl){
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putExtra(IMAGE_URL_EXTRA, imageUrl);

        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);

        mImageView = (ImageView) findViewById(R.id.pp_view);
        imageUrl = getIntent().getStringExtra(IMAGE_URL_EXTRA);

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(ImageBrowserActivity.this, R.anim.zoom_in);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                animation.start();
                return false;
            }
        });

        Picasso.with(this).load(imageUrl).into(mImageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            supportFinishAfterTransition();
        }
    }

}
