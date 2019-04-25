package com.tany.base.base;

import android.content.Context;

/**
 * Created by Tany on 2018/3/23.
 * Desc:
 */


public class BaseVMImpl<V extends BaseActivity> implements BaseVM {
    protected V mView;
    protected Context mContext;

    public BaseVMImpl(V view, Context context) {
        mView = view;
        mContext = context;
    }
}
