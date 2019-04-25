package com.tany.base.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tany.base.R;

import java.util.ArrayList;
import java.util.List;


public class LableView extends ViewGroup{
    private List<int[]> children;
    OnIntemClick onIntemClick;
    List<String> tags;
    @DrawableRes int text_background;
    public LableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        children = new ArrayList<int[]>();
    }

    public List<String> getTags() {
        return tags;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        final int count = getChildCount(); // tag的数量
        int left = 0; // 当前的左边距离
        int top = 0; // 当前的上边距离
        int totalHeight = 0; // WRAP_CONTENT时控件总高度
        int totalWidth = 0; // WRAP_CONTENT时控件总宽度

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();

            if (i == 0) { // 第一行的高度
                totalHeight = params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            }

            if (left + params.leftMargin + child.getMeasuredWidth() + params.rightMargin > getMeasuredWidth()) { // 换行
                left = 0;
                top += params.topMargin + child.getMeasuredHeight() + params.bottomMargin; // 每个TextView的高度都一样，随便取一个都行
                totalHeight += params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            }

            children.add(new int[]{left + params.leftMargin, top + params.topMargin, left + params.leftMargin + child.getMeasuredWidth(), top + params.topMargin + child.getMeasuredHeight()});

            left += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;

            if (left > totalWidth) { // 当宽度为WRAP_CONTENT时，取宽度最大的一行
                totalWidth = left;
            }
        }

        int height = 0;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = totalHeight;
        }

        int width = 0;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = totalWidth;
        }

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            int[] position = children.get(i);
            child.layout(position[0], position[1], position[2], position[3]);
        }
    }

    public void addView(final List<String> tags,@DrawableRes int text_background) {
        if (this.tags!=null){
            children = new ArrayList<int[]>();
            removeAllViews();
        }
        if (tags==null){
            return;
        }
        this.tags=tags;
        this.text_background=text_background;
        for ( int i = 0; i < tags.size(); i++) {
            final int pson=i;
            TextView tv = new TextView(getContext());
            tv.setText(tags.get(i));
            tv.setTextColor(getResources().getColor(R.color.gray1));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            tv.setLayoutParams(params);
            tv.setBackgroundResource(text_background);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onIntemClick!=null)
                    onIntemClick.onClick(v,pson,tags.get(pson));
                }
            });
            super.addView(tv);
        }
    }


    public void notifyView() {
        children = new ArrayList<int[]>();
        removeAllViews();
        if (tags==null)
            return;
        for ( int i = 0; i < tags.size(); i++) {
            final int pson=i;
            TextView tv = new TextView(getContext());
            tv.setText(tags.get(i));
            tv.setTextColor(getResources().getColor(R.color.gray1));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            tv.setLayoutParams(params);
            tv.setBackgroundResource(text_background);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onIntemClick!=null)
                        onIntemClick.onClick(v,pson,tags.get(pson));
                }
            });
            super.addView(tv);
        }
    }

    public void setOnIntemClick(OnIntemClick onIntemClick){
        this.onIntemClick=onIntemClick;
   }
    public interface OnIntemClick{
         void onClick(View view, int poson, String tags);
    }

}
