package com.tany.base.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.math.BigDecimal;

/**
 * Desc:
 */

public class SDCardUtils {

    /**
     * 判断ＳＤ卡是否挂载
     */
    public static boolean isSDCardMounted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 返回ＳＤ卡根目录
     *
     * @return
     */
    public static String getSDCardRootDir() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获得SD卡空闲容量
     *
     * @return
     */
    public static double getSDAvailaSize() {
        if (isSDCardMounted()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            long blocSize = statfs.getBlockSize();
            long availaBlock = statfs.getAvailableBlocks();
            double dTotal = blocSize * availaBlock / 1024.00 / 1024 / 1024;
            BigDecimal bd = new BigDecimal(dTotal);
            return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//G
        }
        return 0;
    }

    /**
     * 获得SD卡总容量
     *
     * @return
     */
    public static double getSDTotalSize() {
        if (isSDCardMounted()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            long blocSize = statfs.getBlockSize();
            long totalBlocks = statfs.getBlockCount();
            double dTotal = blocSize * totalBlocks / 1024.00 / 1024 / 1024;
            BigDecimal bd = new BigDecimal(dTotal);
            return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//G
        }
        return 0;
    }

    /**
     * 取两位小数
     *
     * @return
     */
    public static double getDouble(double l) {
        BigDecimal bd = new BigDecimal(l);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
