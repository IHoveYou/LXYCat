package com.tany.base.widget.runtextview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug;

/**
 * Created by Tany on 2018/11/9.
 * Desc:横向的跑马灯(其实就是系统控件)
 */


public class TanyRunTextView extends android.support.v7.widget.AppCompatTextView {
    public TanyRunTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TanyRunTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TanyRunTextView(Context context) {
        super(context);
    }

    /**
     * 当前并没有焦点，我只是欺骗了Android系统
     */
    @Override
    @ViewDebug.ExportedProperty(category = "focus")
    public boolean isFocused() {
        return true;
    }
}
