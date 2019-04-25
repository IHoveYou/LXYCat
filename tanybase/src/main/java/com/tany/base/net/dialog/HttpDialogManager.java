package com.tany.base.net.dialog;

import android.app.Activity;
import android.app.Dialog;


public abstract class HttpDialogManager {

    public abstract Dialog showLoadingDialog(Activity activity);

    public void cancelLoadingDialog(Dialog dialog) {
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }
}
