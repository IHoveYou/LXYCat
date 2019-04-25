package com.tany.base.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tany.base.tanyutils.TanyDeviceUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tany on 2018/8/30
 * 验签
 */

public class SignVerification {

    public static final String KEY = "c605e6955";

    public static HashMap<String, String> decorate(HashMap<String, String> params, CapParams capParams) {
        if (params == null) {
            params = new HashMap();
        }
        addSign(params, capParams, System.currentTimeMillis() / 1000);
        return params;
    }


    private static void addSign(Map params, CapParams capParams, long time) {
        params.put("timestamp", (String.valueOf(time)));
        params.put("deviceId", stringValue(capParams.deviceId));
        params.put("appVersion", stringValue(capParams.appVersion));
        params.put("deviceType", stringValue(capParams.deviceType));
        params.put("phoneSystemVersion", stringValue(capParams.phoneSystemVersion));
        params.put("ip", stringValue(capParams.ip));

    }

    public static String getSign(Map<String, String> map, String str, long time) {
        ArrayList keys = new ArrayList(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!TextUtils.isEmpty(entry.getKey())) {
                keys.add(entry.getKey());
            }
        }
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (Object key : keys) {
            sb.append(key).append('=').append(stringValue(map.get(key))).append('&');
        }
        sb.append("apikey=").append(str).append("&timestamp=" + time);
        try {
            String encode = URLEncoder.encode(sb.toString().replace(" ", ""), "UTF-8");
            return getMD5Str(encode.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return getMD5Str(sb.toString().getBytes(Charset.forName("UTF-8")));
        }
    }

    private static String stringValue(String s) {
        return TextUtils.isEmpty(s) ? "" : s;
    }

    public static String getMD5Str(byte[] btInput) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 网关需要的参数类
     */
    public static class CapParams {
        /**
         * 设备标识
         */
        public String deviceId;

        /**
         * app版本号
         */
        public String appVersion;

        /**
         * 客户端类型
         */
        public String deviceType;
        /**
         * 手机版本
         */
        public String phoneSystemVersion;
        /**
         * 手机当前ip
         */
        public String ip;

        public CapParams(Context context) {
            this.deviceId = TanyDeviceUtil.getUniquePsuedoID();
            this.appVersion = TanyDeviceUtil.getAppVersionNo(context) + "";
            this.deviceType = 2 + "";
            this.phoneSystemVersion = TanyDeviceUtil.getBuildBrandModel();
            this.ip = TanyDeviceUtil.getIPAddress(context);
        }
    }
}
