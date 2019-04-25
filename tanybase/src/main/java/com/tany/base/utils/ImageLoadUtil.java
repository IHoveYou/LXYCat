package com.tany.base.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tany.base.GlideApp;


/**
 * Created by Tany on 2018/6/7.
 * Desc:
 */


public class ImageLoadUtil {
    /**
     * 加载网络图片
     *
     * @param context
     * @param iv
     * @param url
     * @param placeholderId 占位图
     */
    public static void displayImage(Context context, ImageView iv, String url, int placeholderId) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholderId)
                .centerCrop()
                .into(iv);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param iv
     * @param url
     * @param placeholderId 占位图
     * @param corner        圆角的角度
     */
    public static void displayRoundedImage(Context context, ImageView iv, String url, int placeholderId, int corner) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholderId)
                .transform(new RoundedCorners(corner)) //此处为圆角px值
                .into(iv);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param iv
     * @param url
     * @param placeholderId
     */
    public static void displayCircleImage(Context context, ImageView iv, String url, int placeholderId) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholderId)
                .transform(new CircleCrop())
                .into(iv);
    }

}
