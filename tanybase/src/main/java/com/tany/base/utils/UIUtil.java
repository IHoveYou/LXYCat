package com.tany.base.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tany.base.net.dialog.HttpDialogManager;
import com.tany.base.net.dialog.HttpToastManager;


/**
 * Created by yao.dang on 2018/5/9.
 */

public class UIUtil {


    private static final String TAG = "UIUtil";
    private static HttpDialogManager sHttpDialogManager;
    private static HttpToastManager sHttpToastManager;
    private static Dialog sDialog;

    public static void setDialogManager(HttpDialogManager dialog) {
        if (dialog == null) {
            return;
        }
        sHttpDialogManager = dialog;
    }

    public static void removeDialogManager() {
        sHttpDialogManager = null;
    }

    public static void setToastManager(HttpToastManager toast) {
        if (toast == null) {
            return;
        }
        sHttpToastManager = toast;
    }

    public static void removeToastManager() {
        sHttpToastManager = null;
    }

    public static void showToast(String message) {
        if (sHttpToastManager == null) {
            Log.w(TAG, "showLoadingDialog-->you should give the HttpToastManager instance to showToast");
            return;
        }
        sHttpToastManager.showToast(message);
    }

    public static void showLoadingDialog(Activity activity) {
        if (sHttpDialogManager == null) {
            Log.w(TAG, "showLoadingDialog-->you should give the HttpDialogManager instance to showLoadingDialog");
            return;
        }
        if (sDialog != null) {
            sDialog.dismiss();
        }
        sDialog = sHttpDialogManager.showLoadingDialog(activity);
    }

    public static void cancelLoadingDiaoog() {
        if (sDialog == null) {
            return;
        }
        sDialog.dismiss();
        sDialog = null;
    }

    public static void showDownloadDialog(Activity activity) {
//        if (sHttpDialogManager == null) {
//            Log.w(TAG, "showLoadingDialog-->you should give the HttpDialogManager instance to showLoadingDialog");
//            return;
//        }
//        sHttpDialogManager.showDownloadDialog(activity);
    }

    public static void cancelDownloadDiaoog() {
//        if (sHttpDialogManager == null) {
//            Log.w(TAG, "showLoadingDialog-->you should give the HttpDialogManager instance to cancelLoadingDiaoog");
//            return;
//        }
//        sHttpDialogManager.cancelDownloadDialog();
    }

    public static void setProgress(int progress) {
//        if (sHttpDialogManager == null) {
//            Log.w(TAG, "showLoadingDialog-->you should give the HttpDialogManager instance to cancelLoadingDiaoog");
//            return;
//        }
//        sHttpDialogManager.onProgress(progress);
    }

    /**
     * 解决NestedScrollView中嵌套RecyclerView滑动不流畅以及头部被顶出的bug
     */
    public static void setSmooth(RecyclerView recyclerView, Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
//        android:descendantFocusability="blocksDescendants"
    }

    public static void setGridLayoutSmooth(RecyclerView recyclerView, Context context, int count) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, count);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
//        android:descendantFocusability="blocksDescendants"
    }
}
