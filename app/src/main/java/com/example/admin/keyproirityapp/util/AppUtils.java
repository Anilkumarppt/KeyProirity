package com.example.admin.keyproirityapp.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.admin.keyproirityapp.R;

/**
 * Created by Dell on 9/9/2018.
 */

public class AppUtils {

    public static final String TAG = AppUtils.class.getSimpleName();

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            imageView.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "loadImage: " + imageUrl);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_email)
//                .thumbnail(thumbnailRequest)
                    .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

    }
}
