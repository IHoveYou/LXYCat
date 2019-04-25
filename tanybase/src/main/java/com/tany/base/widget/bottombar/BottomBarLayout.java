package com.tany.base.widget.bottombar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tany.base.R;
import com.tany.base.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tany on 2018/3/23.
 * Desc:底部导航栏
 */


public class BottomBarLayout extends LinearLayout implements View.OnClickListener {
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     *
     * @param bottomBarLayout 底部导航栏控件
     * @param tabText 文字
     * @param normalIcon 未选中图片集合
     * @param selectIcon 选中图片集合
     * @param normalTextColor 未选中文字颜色
     * @param selectTextColor 选中文字颜色
     */
    public static void initBottomNav(BottomBarLayout bottomBarLayout, String[] tabText, int[] normalIcon, int[] selectIcon, int normalTextColor, int selectTextColor) {
        List<TabEntity> tabEntityList = new ArrayList<>();
        for (int i = 0; i < tabText.length; i++) {
            TabEntity item = new TabEntity();
            item.setText(tabText[i]);
            item.setNormalIconId(normalIcon[i]);
            item.setSelectIconId(selectIcon[i]);
            tabEntityList.add(item);
        }
        bottomBarLayout.setNormalTextColor(normalTextColor);
        bottomBarLayout.setSelectTextColor(selectTextColor);
        bottomBarLayout.setTabList(tabEntityList);
    }

    private int normalTextColor;
    private int selectTextColor;

    private LinearLayout mLinearLayout;
    private List<TabEntity> tabList = new ArrayList<>();

    public BottomBarLayout(Context context) {
        super(context);
        init(context);
    }

    public BottomBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mLinearLayout = (LinearLayout) View.inflate(context, R.layout.layout_container, null);
        addView(mLinearLayout);
    }

    public void setNormalTextColor(int color) {
        this.normalTextColor = color;
    }

    public void setSelectTextColor(int color) {
        this.selectTextColor = color;
    }

    public void setTabList(List<TabEntity> tabs) {
        if (tabs == null || tabs.size() == 0) {
            return;
        }
        this.tabList = tabs;
        mLinearLayout.setWeightSum(tabs.size());
        for (int i = 0; i < tabs.size(); i++) {
            View itemView = View.inflate(getContext(), R.layout.item_tab_layout, null);
            itemView.setId(i);
            itemView.setOnClickListener(this);
            TextView text = (TextView) itemView.findViewById(R.id.tv_title);
            ImageView icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            View redPoint = itemView.findViewById(R.id.red_point);
            TextView number = (TextView) itemView.findViewById(R.id.tv_count);
            TabEntity itemTab = tabList.get(i);
            text.setText(itemTab.getText());
            text.setTextColor(normalTextColor);
            text.setTextSize(10);
            icon.setImageResource(itemTab.getNormalIconId());
            if (itemTab.isShowPoint()) {
                redPoint.setVisibility(View.VISIBLE);
            } else {
                redPoint.setVisibility(View.GONE);
            }
            if (itemTab.getNewsCount() == 0) {
                number.setVisibility(View.GONE);
            } else if (itemTab.getNewsCount() > 99) {
                number.setVisibility(View.VISIBLE);
                number.setText("99+");
            } else {
                number.setVisibility(View.VISIBLE);
                number.setText(String.format("%d", itemTab.getNewsCount()));
            }
            itemView.setLayoutParams(new LayoutParams(AppHelper.getDisplayWidth() / tabs.size(), ViewGroup.LayoutParams.MATCH_PARENT));
            mLinearLayout.addView(itemView);
            if (i == 0) {
                showTab(0, itemView);
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (mOnItemClickListener == null) {
            return;
        }
        mOnItemClickListener.onItemClick(view.getId());
        showTab(view.getId(), view);
    }

    public void showTab(int position, View view) {
        clearStatus();
        TextView text = (TextView) view.findViewById(R.id.tv_title);
        text.setTextColor(selectTextColor);
        ImageView icon = (ImageView) view.findViewById(R.id.iv_icon);
        icon.setImageResource(tabList.get(position).getSelectIconId());

    }

    private void clearStatus() {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            View itemView = mLinearLayout.getChildAt(i);
            ImageView icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            TextView text = (TextView) itemView.findViewById(R.id.tv_title);
            text.setTextColor(normalTextColor);
            icon.setImageResource(tabList.get(i).getNormalIconId());
        }
    }

    public void setSelect(int poision) {
        showTab(poision, mLinearLayout.getChildAt(poision));
    }
}
