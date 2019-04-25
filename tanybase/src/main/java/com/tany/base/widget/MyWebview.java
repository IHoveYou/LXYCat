package com.tany.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Tany on 2018/11/15.
 * Desc:
 */


public class MyWebview extends WebView {
    public interface PlayFinish {
        void After();
    }

    PlayFinish df;

    public void setDf(PlayFinish playFinish) {
        this.df = playFinish;
    }

    public MyWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebview(Context context) {
        super(context);
    }

    //onDraw表示显示完毕
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (df != null) {
            df.After();
        }
    }
}
