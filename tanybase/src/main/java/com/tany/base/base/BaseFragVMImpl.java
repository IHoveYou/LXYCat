package com.tany.base.base;

import android.content.Context;

/**
 * 作者: Tany
 * 日期: 2017/9/20
 * 描述:
 */


public class BaseFragVMImpl<V extends BaseFragment> implements BaseVM {
    protected V mView;
    protected Context mContext;

    public BaseFragVMImpl(V view, Context context) {
        mView = view;
        mContext = context;
    }
}
