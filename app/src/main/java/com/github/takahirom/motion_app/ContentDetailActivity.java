package com.github.takahirom.motion_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import static com.github.takahirom.motion_app.DetailActivity.EXTRA_ITEM;

/**
 * Created by Yayoi Namiki on 2017/05/09.
 */
public class ContentDetailActivity extends AppCompatActivity {


    public static Intent getLaunchIntent(Context context, PhotoDetailAdapter.Item item) {
        final Intent intent = new Intent(context, ContentDetailActivity.class);
        intent.putExtra(EXTRA_ITEM, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        PhotoDetailAdapter.Item item = (PhotoDetailAdapter.Item)getIntent().getSerializableExtra(EXTRA_ITEM);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        TextView textView = (TextView) findViewById(R.id.text_content_detail);
        textView.setText(item.text);

        ImageView imageView = (ImageView) findViewById(R.id.image_content_detail);
        imageView.setImageResource(item.imageRes);
    }

}
