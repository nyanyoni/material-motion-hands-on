/*
 * Copyright (C) 2017 takahirom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.takahirom.motion_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.takahirom.motion_app.datasource.PixabayResponse;
import com.github.takahirom.motion_app.util.AndroidVersionUtil;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "EXTRA_ITEM";
    private PixabayResponse.Hit photoDetail;


    public static Intent getLaunchIntent(Context context, PixabayResponse.Hit item) {
        final Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_ITEM, item);
        return intent;
    }

    public static void showForDebug(Window window) {
        if (!AndroidVersionUtil.isGreaterThanL()) {
            return;
        }
        String message = "If you want to disable this dialog, Please restart app." +
                "windowAnimations:" +
                window.getContext().getResources().getResourceName(window.getAttributes().windowAnimations) +
                "\n-----\n" +
                "SharedElementEnterTransition:" +
                window.getSharedElementEnterTransition() +
                "\n-----\n" +
                "ReturnTransition:" +
                window.getReturnTransition() +
                "\n-----\n" +
                "SharedElementReturnTransition:" +
                window.getSharedElementReturnTransition() +
                "\n-----\n" +
                "ReenterTransition:" +
                window.getReenterTransition() +
                "\n-----\n" +
                "SharedElementReenterTransition:" +
                window.getSharedElementReenterTransition() +
                "\n-----\n" +
                "ExitTransition:" +
                window.getExitTransition() +
                "\n-----\n" +
                "SharedElementExitTransition:" +
                window.getSharedElementExitTransition();
        new AlertDialog.Builder(window.getContext())
                .setMessage(message).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        photoDetail = getIntent().getParcelableExtra(EXTRA_ITEM);
        setupViews();
    }

    private void setupViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        // Stop transition for waiting image load
        ActivityCompat.postponeEnterTransition(this);
        final ImageView imageView = (ImageView) findViewById(R.id.photo);
        Glide
                .with(this)
                .load(photoDetail.getWebformatURL().replace("640", "340"))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        ActivityCompat.startPostponedEnterTransition(DetailActivity.this);
                        return false;
                    }
                })
                .into(imageView);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_photo_detail);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PhotoDetailAdapter adapter = new PhotoDetailAdapter(new PhotoDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View textView, PhotoDetailAdapter.Item item) {
                final Intent launchIntent = ContentDetailActivity.getLaunchIntent(DetailActivity.this, item);
                final ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(DetailActivity.this, textView, getString(R.string.transition_name_contents));
                ActivityCompat.startActivity(DetailActivity.this, launchIntent, optionsCompat.toBundle());
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.setPhotoDetails(photoDetail);

//        showForDebug(getWindow());
    }
}
