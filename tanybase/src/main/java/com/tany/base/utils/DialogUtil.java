package com.tany.base.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tany.base.R;

import static android.R.style.Theme_Material_Dialog_Alert;
import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by yao.dang on 2018/5/9.
 */

public class DialogUtil {
    public static Dialog makeLoadingDialog(Activity activity) {
        return makeLoadingDialog(activity, "正在加载...");
    }

    public static Dialog makeLoadingDialog(Activity activity, String message) {
        return makeLoadingDialog(activity, message, true);
    }

    /**
     * 加载中对话框，ui类似toast
     *
     * @param activity
     * @param message
     * @param cancelable
     * @return
     */
    public static Dialog makeLoadingDialog(Activity activity, String message, boolean cancelable) {
        if (activity == null) {
            return null;
        }
        Dialog dialog = new Dialog(activity, Theme_Material_Dialog_Alert);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);

        View rootView = LayoutInflater.from(activity).inflate(R.layout.common_popupview, null);
        ImageView ivIcon = (ImageView) rootView.findViewById(R.id.popupview_loading_iv);
        TextView tvMessage = (TextView) rootView.findViewById(R.id.popupview_loading_tv);
        tvMessage.setText(message);

        RotateAnimation animation = new RotateAnimation(0, 359, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        ivIcon.startAnimation(animation);
        dialog.getWindow().setDimAmount(0);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(rootView);
        return dialog;
    }

    public static Dialog show(Activity activity, String content, ClickListener listener) {
        return show(activity, content, null, "取消", "确认", true, true, listener, true);
    }

    public static Dialog show(Activity activity, String content, String rightButton, ClickListener listener) {
        return show(activity, content, null, "取消", rightButton, true, true, listener, true);
    }

    public static Dialog show(Activity activity, String content, String leftButton, String rightButton, ClickListener listener) {
        return show(activity, content, null, leftButton, rightButton, true, true, listener, true);
    }

    public static Dialog show(Activity activity, String title, String content, String leftButton, String rightButton, ClickListener listener) {
        return show(activity, title, content, leftButton, rightButton, true, true, listener, true);
    }

    public static Dialog show(Activity activity, String title, String content, String leftButton, String rightButton, boolean cancelable, ClickListener listener) {
        return show(activity, title, content, leftButton, rightButton, cancelable, cancelable, listener, true);
    }

    public static Dialog show(Activity activity, String title, String content, String leftButton, String rightButton, boolean cancelable, ClickListener listener, boolean cancelByClickedButton) {
        return show(activity, title, content, leftButton, rightButton, cancelable, cancelable, listener, cancelByClickedButton);
    }

    /**
     * 两个按钮类型dialog对话框，直接显示，无需再show();
     *
     * @param activity
     * @param title
     * @param content
     * @param leftButton
     * @param rightButton
     * @param touchCancelable
     * @param backCancelable
     * @param clickListener
     * @return
     */
    public static Dialog show(Activity activity, String title, String content,
                              String leftButton, String rightButton, boolean touchCancelable,
                              boolean backCancelable, final ClickListener clickListener, final boolean cancelByClickedButton) {
        View root = LayoutInflater.from(activity).inflate(R.layout.common_dialog_two_button, null);
        final Dialog dialog = new Dialog(activity, R.style.AlertDialogStyle);
        dialog.setContentView(root);
        TextView tvTitle = (TextView) root.findViewById(R.id.dialog_title);
        TextView tvContent = (TextView) root.findViewById(R.id.dialog_content);
        final TextView tvLeft = (TextView) root.findViewById(R.id.dialog_tv_left);
        final TextView tvRight = (TextView) root.findViewById(R.id.dialog_tv_right);

        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        if (TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(content);
        }
        tvLeft.setText(leftButton);
        tvRight.setText(rightButton);
        if (clickListener != null) {
            final View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == tvLeft) {
                        clickListener.onLeftClicked();
                    } else if (v == tvRight) {
                        clickListener.onRightClicked();
                    }
                    if (cancelByClickedButton)
                        dialog.dismiss();
                }
            };
            tvLeft.setOnClickListener(listener);
            tvRight.setOnClickListener(listener);
        }

        dialog.setCanceledOnTouchOutside(touchCancelable);
        dialog.setCancelable(backCancelable);
        dialog.show();
        dialog.getWindow().setLayout(SizeUtil.dpToPx(270, activity), LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public static Dialog showSingle(Activity activity, String content) {
        return showSingle(activity, content, null, "确定", false, false, null);
    }

    public static Dialog showSingle(Activity activity, String content, String button, SingleClickListener listener) {
        return showSingle(activity, content, null, button, false, false, listener);
    }

    public static Dialog showSingle(Activity activity, String title, String content, String button, SingleClickListener listener) {
        return showSingle(activity, title, content, button, false, false, listener);
    }

    public static Dialog showSingleNotDismissOnclick(Activity activity, String title, String content, String button, SingleClickListener listener) {
        return showSingleNotDismissOnclick(activity, title, content, button, false, false, listener);
    }

    public static Dialog showSingle(Activity activity, String title, String content, String button, boolean cancelable, SingleClickListener listener) {
        return showSingle(activity, title, content, button, cancelable, cancelable, listener);
    }

    /**
     * 单个按钮类型的dialog对话框，直接显示，无需再show();
     *
     * @param activity
     * @param title
     * @param content
     * @param button
     * @param touchCancelable
     * @param backCancelable
     * @param clickListener
     * @return
     */
    public static Dialog showSingle(Activity activity, String title, String content,
                                    String button, boolean touchCancelable,
                                    boolean backCancelable, final SingleClickListener clickListener) {
        View root = LayoutInflater.from(activity).inflate(R.layout.common_dialog_single_btn1, null);
        final Dialog dialog = new Dialog(activity, R.style.AlertDialogStyle);
        dialog.setContentView(root);
        TextView tvTitle = (TextView) root.findViewById(R.id.dialog_single_title);
        TextView tvContent = (TextView) root.findViewById(R.id.dialog_single_content);
        final TextView btn = (TextView) root.findViewById(R.id.dialog_single_btn);
        tvTitle.setText(title);
        if (TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(content);
        }
        btn.setText(button);
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    if (v == btn) {
                        clickListener.onClicked();
                    }
                }
                dialog.dismiss();
            }
        };
        btn.setOnClickListener(listener);
        dialog.setCanceledOnTouchOutside(touchCancelable);
        dialog.setCancelable(backCancelable);
        dialog.show();
        dialog.getWindow().setLayout(SizeUtil.dpToPx(270, activity), LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    /**
     * 单个按钮类型的dialog对话框，直接显示，无需再show();
     *
     * @param activity
     * @param title
     * @param content
     * @param button
     * @param touchCancelable
     * @param backCancelable
     * @param clickListener
     * @return
     */
    public static Dialog showSingleNotDismissOnclick(Activity activity, String title, String content,
                                                     String button, boolean touchCancelable,
                                                     boolean backCancelable, final SingleClickListener clickListener) {
        View root = LayoutInflater.from(activity).inflate(R.layout.common_dialog_single_btn1, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity).setView(root).create();
        TextView tvTitle = (TextView) root.findViewById(R.id.dialog_single_title);
        TextView tvContent = (TextView) root.findViewById(R.id.dialog_single_content);
        final TextView btn = (TextView) root.findViewById(R.id.dialog_single_btn);
        tvTitle.setText(title);
        if (TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(content);
        }
        btn.setText(button);
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    if (v == btn) {
                        clickListener.onClicked();
                    }
                }
            }
        };
        btn.setOnClickListener(listener);
        dialog.setCanceledOnTouchOutside(touchCancelable);
        dialog.setCancelable(backCancelable);
        dialog.show();
        dialog.getWindow().setLayout(SizeUtil.dpToPx(270, activity), LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public static abstract class ClickListener {
        public void onLeftClicked() {
        }

        public abstract void onRightClicked();
    }

    public static interface SingleClickListener {
        void onClicked();
    }

    public static abstract class ChooseListener {
        public abstract void onClicked1();

        public abstract void onClicked2();

        public abstract void onClickedCancle();
    }

    public static Dialog showChoose(Activity activity, String option1, String option2, final ChooseListener chooseListener) {
        final Dialog dialog = new Dialog(activity, R.style.Theme_Light_Dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(activity).inflate(R.layout.commom_dialog_choose, null);
        //初始化控件
        TextView commonChoose1 = (TextView) inflate.findViewById(R.id.common_choose1);
        TextView commonChoose2 = (TextView) inflate.findViewById(R.id.common_choose2);
        TextView commonBtnCancle = (TextView) inflate.findViewById(R.id.common_btn_cancle);
        commonChoose1.setText(option1);
        commonChoose2.setText(option2);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseListener != null) {
                    if (v.getId() == R.id.common_choose1) {
                        chooseListener.onClicked1();
                    } else if (v.getId() == R.id.common_choose2) {
                        chooseListener.onClicked2();
                    } else {
                        chooseListener.onClickedCancle();
                    }
                }
                dialog.dismiss();
            }
        };
        commonChoose1.setOnClickListener(listener);
        commonChoose2.setOnClickListener(listener);
        commonBtnCancle.setOnClickListener(listener);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
//        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(inflate);
        dialog.show();
        return dialog;

    }

    public static Dialog showChooseMore(Context context, View view) {
        Dialog dialog = new Dialog(context, R.style.Theme_Light_Dialog);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
//        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.setContentView(view);
        dialog.show();
        return dialog;

    }


    public static void closeGpsErrorPopupWindows() {
        try {
            if (mPopupWindow != null && mPopupWindow.isShowing())
                mPopupWindow.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PopupWindow mPopupWindow;
}
