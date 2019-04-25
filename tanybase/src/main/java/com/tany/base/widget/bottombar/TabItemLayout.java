package com.tany.base.widget.bottombar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by liuhao on 16/11/28.
 */

public class TabItemLayout extends LinearLayout {

    

    public TabItemLayout(Context context) {
        super(context);
    }

    public TabItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}