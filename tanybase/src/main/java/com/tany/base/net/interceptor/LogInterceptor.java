package com.tany.base.net.interceptor;

import android.text.TextUtils;

import com.tany.base.utils.LogUtil;
import com.tany.base.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

import static com.tany.base.utils.Utils.getParamsKeyFromCDHeader;


public class LogInterceptor implements Interceptor {
    private static final String TAG = "-----HTTP-----";
    private boolean mNeedLog = false;

    public LogInterceptor(boolean needLog) {
        this.mNeedLog = needLog;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!mNeedLog) {
            return chain.proceed(chain.request());
        }
        Request request = chain.request();
        switch (request.method()) {
            case "GET":
                String url = request.url().toString();
                LogUtil.i(TAG, "---request---\r\nmethod:GET\r\nurl:" + url);
                break;
            case "POST":
                logPost(request);
                break;
        }
        Response response;
        try {
            response = chain.proceed(request);
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtil.i(TAG, "---response---\r\nurl:" + request.url() + "\r\nresponse:" + content);
            ResponseBody responseBody = ResponseBody.create(mediaType, content);
            Response res = response.newBuilder()
                    .body(responseBody)
                    .build();
            return res;
        } catch (Exception e) {
            LogUtil.i(TAG, "---response---\r\nurl:" + request.url() + "\r\nerrpr:" + e.getMessage());
        }
        return chain.proceed(request);
    }

    private void logPost(Request request) {
        RequestBody requestBody = request.body();
        if (requestBody instanceof FormBody) {
            HashMap<String, String> paramsMap = new HashMap<>();
            for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
                paramsMap.put(((FormBody) requestBody).encodedName(i), ((FormBody) requestBody).encodedValue(i));
            }
            LogUtil.i(TAG, "---request---\r\nmethod:POST\r\nurl:" + request.url() + "\r\nparams:" + paramsMap.toString());
        } else if (requestBody instanceof MultipartBody) {
            HashMap<String, String> paramsMap = getParamsFromMultipartBldy((MultipartBody) requestBody);
            LogUtil.i(TAG, "---request---\r\nmethod:POST(Multipart)\r\nurl:" + request.url() + "\r\nparams:" + paramsMap.toString());
        } else {
            LogUtil.i(TAG, "---request---\r\nmethod:POST\r\nurl:" + request.url() + "\r\nRequestBody:" + requestBody);
        }
    }

    private HashMap<String, String> getParamsFromMultipartBldy(MultipartBody multipartBody) {
        List<MultipartBody.Part> parts = multipartBody.parts();
        HashMap<String, String> paramsMap = new HashMap<>();
        for (MultipartBody.Part part : parts) {
            String cdHeader = part.headers().get("Content-Disposition");
            if (Utils.isFilePart(cdHeader)) {
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
        return paramsMap;
    }
}
