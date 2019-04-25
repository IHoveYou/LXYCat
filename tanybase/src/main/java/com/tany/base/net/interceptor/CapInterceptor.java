package com.tany.base.net.interceptor;

import android.support.annotation.NonNull;

import com.tany.base.utils.SignVerification;

import java.util.HashMap;


public class CapInterceptor extends ParamsInterceptor {

    private SignVerification.CapParams mCapParams;

    public CapInterceptor(@NonNull SignVerification.CapParams params) {
        this.mCapParams = params;
    }

    public SignVerification.CapParams getCapParams(){
        return mCapParams;
    }

    @Override
    protected HashMap<String, String> decorateParams(HashMap<String, String> originalParamsMap) {
        return SignVerification.decorate(originalParamsMap, mCapParams);
    }
}
