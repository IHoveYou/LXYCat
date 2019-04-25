package com.tany.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.gcssloop.widget.RCImageView;
import com.tany.base.GlideApp;
import com.tany.base.R;
import com.tany.base.utils.StringUtil;

/**
 * 加载网络图片 圆角/圆形
 */

public class MyRCImageView extends RCImageView {

    TypedArray array;
    Drawable err;

    public MyRCImageView(Context context) {
        super(context);
    }

    public MyRCImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyRCImageView, 0, 0);
        err = array.getDrawable(R.styleable.MyRCImageView_err);
    }

    public MyRCImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyRCImageView, 0, 0);
        err = array.getDrawable(R.styleable.MyRCImageView_err);
    }


    public void setUrl(String url) {
        if (StringUtil.isEmpty(url) || url.equals("0")) {
            GlideApp.with(getContext())
                    .load("")
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(err)
                    .into(this);
            return;
        }

        //符合标准
        GlideApp.with(getContext())
                .load(url)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(err)
                .into(this);
    }

    public void setErr(Drawable err) {
        this.err = err;
    }

    public void setSrc(int icon) {
        setImageResource(icon);
    }

}
