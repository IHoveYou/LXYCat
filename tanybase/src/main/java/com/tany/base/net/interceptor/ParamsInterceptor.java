package com.tany.base.net.interceptor;

import android.text.TextUtils;

import com.tany.base.utils.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static com.tany.base.utils.Utils.getParamsKeyFromCDHeader;
import static com.tany.base.utils.Utils.isFilePart;



public abstract class ParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String method = request.method();
        //get请求的封装
        if (method.equals("GET")) {
            request = buildParamsWithGET(request);
        } else if (method.equals("POST")) {
            request = buildParamsWithPOST(request);
        }
        return chain.proceed(request);
    }

    private Request buildParamsWithPOST(Request request) {
        RequestBody originalBody = request.body();
        if (originalBody instanceof FormBody) {
            return buildWithFormBody(request, (FormBody) originalBody);
        } else if (originalBody instanceof MultipartBody) {
            // 格式 是 multipart/mixed
            return buildWithMultipartBody(request, (MultipartBody) originalBody);
        } else if (originalBody.contentType().toString().contains("multipart")) {
            // 格式  里面包含  multipart字样
            Class<? extends RequestBody> aClass = originalBody.getClass();
            try {
                Field field = aClass.getDeclaredField("delegate");
                field.setAccessible(true);
                MultipartBody multipartBody = (MultipartBody) field.get(originalBody);
                return buildWithMultipartBody(request, multipartBody);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            //TODO：如果反射调用失败直接返回，不再添加拦截参数
            return request;

        } else {
            return buildWithOtherBody(request, originalBody);
        }
    }

    private Request buildWithOtherBody(Request request, RequestBody originalBody) {
        return request;
    }

    private Request buildWithMultipartBody(Request request, MultipartBody originalBody) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        ArrayList<MultipartBody.Part> fileParts = new ArrayList<>();
        MultipartBody multipartBody = originalBody;
        List<MultipartBody.Part> parts = multipartBody.parts();
        HashMap<String, String> paramsMap = new HashMap<>();
        for (MultipartBody.Part part : parts) {
            String cdHeader = part.headers().get("Content-Disposition");
            if (isFilePart(cdHeader)) {
                fileParts.add(part);
                continue;
            }
            String key = getParamsKeyFromCDHeader(cdHeader);
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            Buffer bs = new Buffer();
            try {
                part.body().writeTo(bs);
                bs.flush();
                String value = new String(bs.readUtf8());
                paramsMap.put(key, value);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        paramsMap = decorateParams(paramsMap);
        addParams(builder, paramsMap);
        for (MultipartBody.Part part : fileParts) {
            builder.addPart(part);
        }
        return request.newBuilder().method(request.method(), builder.build()).build();
    }

    private Request buildWithFormBody(Request request, FormBody originalBody) {
        HashMap<String, String> paramsMap = new HashMap<>();
        for (int i = 0; i < originalBody.size(); i++) {
            try {
                paramsMap.put(URLDecoder.decode(originalBody.encodedName(i), "UTF-8"), URLDecoder.decode(originalBody.encodedValue(i), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        paramsMap = decorateParams(paramsMap);
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        request = request.newBuilder().post(builder.build()).build();
        return request;
    }

    private void addParams(MultipartBody.Builder builder, HashMap<String, String> paramsMap) {
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
    }


    private Request buildParamsWithGET(Request request) {
        HttpUrl httpUrlurl = request.url();
        HashMap<String, String> paramsMap = new HashMap<>();
        Set<String> parameterNames = httpUrlurl.queryParameterNames();
        for (String key : parameterNames) {
            String value = httpUrlurl.queryParameter(key);
            if (TextUtils.isEmpty(value) || value.equalsIgnoreCase("null")) {
                value = "";
            }
            try {
                value = URLDecoder.decode(value, "UTF-8");//先decode再encode，防止已经被encode过一次后重复encode
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            paramsMap.put(key, value);
        }
        paramsMap = decorateParams(paramsMap);
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            try {
                paramsMap.put(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Request.Builder builder = request.newBuilder();
        String url = Utils.getUrlPath(request.url().toString());
        url = url + "?" + Utils.getUrlParamsFromMap(paramsMap);
        builder.url(url);
        return builder.build();
    }

    protected abstract HashMap<String, String> decorateParams(HashMap<String, String> originalParamsMap);
}
