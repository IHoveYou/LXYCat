package com.tany.base.utils;

import android.text.TextUtils;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yao.dang on 2018/5/9.
 */

public class Utils {



    /**
     * 获取url的path，不包括参数
     *
     * @param strURL
     * @return
     */
    public static String getUrlPath(String strURL) {
        if (TextUtils.isEmpty(strURL)) {
            return "";
        }
        strURL = strURL.trim();
        return strURL.split("[?]")[0];
    }

    /**
     * 根据Part 中key = Content-Disposition 的header的value判断是否是文件
     *
     * @param cdHead
     * @return
     */
    public static boolean isFilePart(String cdHead) {
        if (!TextUtils.isEmpty(cdHead) && cdHead.contains("filename")) {
            return true;
        }
        return false;
    }

    /**
     * 从Part 中key = 'Content-Disposition' 的header的value获取参数的key
     *
     * @param cdHeader
     * @return
     */
    public static String getParamsKeyFromCDHeader(String cdHeader) {
        Pattern pattern = Pattern.compile("name=\"(.*?)\"");
        Matcher matcher = pattern.matcher(cdHeader);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * 将map改成url参数的格式
     * e.g:key1=value1&key2=value2
     *
     * @param requestParams
     * @return
     */
    public static String getUrlParamsFromMap(Map<String, String> requestParams) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }

    public static String getMD5(String source) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.update(source.getBytes("UTF-8"));
            byte[] hash = md5.digest();
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            // type is a normal class.
            return (Class<?>) type;

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class.
            // Neal isn't either but suspects some pathological case related
            // to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            checkArgument(rawType instanceof Class);
            return (Class<?>) rawType;

        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType)type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();

        } else if (type instanceof TypeVariable) {
            // we could use the variable's bounds, but that won't work if there are multiple.
            // having a raw type that's more general than necessary is okay
            return Object.class;

        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);

        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + className);
        }

    }

    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }
}
