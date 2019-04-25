package com.tany.base.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.tany.base.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class FileHelper {

    public enum CacheType {

        GLIDE_IMAGE,//glide框架图片的缓存地址
        IMAGE,//手动保存到本地的图片
        TEM,//临时文件的缓存（用完以后自动删除）
        ALL,//所有的
        LOG,//日志缓存
        APP,//更新app临时存放地址
        VOICE,
        WEBVIEW,//网页
        BATEBASE,//数据库

    }


    public static String getBasePath() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/" +
                    "tany" + "/cache";
        }

        return null;
    }

    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param c
     * @param fileName 文件名称
     * @param bitmap   图片
     * @return
     */

    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }
    public static String saveFile( String fileName, byte[] bytes) {
        String filePath = "";
        String fileFullName = "";
        FileOutputStream fos = null;
//        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
//                .format(new Date());
        try {
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/卖客达/" ;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }

        return fileFullName;
    }


    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
//        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
//                .format(new Date());
        try {
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/下载/" + "tany" + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }

        return fileFullName;
    }

    /**
     * 获取文件(文件夹)缓存地址的工厂
     *
     * @param type     类型
     * @param isFolder 是否是文件夹
     * @return
     */
    public static String getPathFactory(CacheType type, boolean isFolder) {

        String basePath = getBasePath();

        deleteEmptyFiles(new File(basePath).getAbsolutePath());

        if (basePath == null) return null;

        File file = null;
        String fileName = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String time = formatter.format(new Date());

        switch (type) {
            case ALL:
                file = new File(basePath);
                break;
            case IMAGE:
                file = new File(basePath, "image");
                fileName = time + ".jpg";
                break;
            case VOICE:
                file = new File(basePath, "voice");
                fileName = time + ".acc";
                break;
            case LOG:
                file = new File(basePath, "log");
                fileName = time + ".log";
                break;
            case WEBVIEW:
                file = new File(basePath, "webview");
                break;
            case BATEBASE:
                file = new File(basePath, "database");
                break;
            case GLIDE_IMAGE:
                file = new File(basePath, "glide_image");
                break;
            case APP:
                file = new File(basePath, "app");
                break;
            case TEM:
                file = new File(basePath, "temp");//存放临时保存的网络图片，需要手动删除
                fileName = time + ".jpg";
                break;

        }

        if (!file.exists() && !file.mkdirs())
            return null;

        if (isFolder)
            return file.toString();

        File dir = new File(file, fileName);
        if (!dir.exists()) {
            try {
                dir.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dir.toString();
    }

    // 创建一个图片文件
    public static File getImgFile(Context context) {
        File file = new File(getPathFactory(CacheType.ALL,true));
        if (!file.exists()) {
            file.mkdirs();
        }
        String imgName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File imgFile = new File(file.getAbsolutePath() + File.separator
                + "IMG_" + imgName + ".jpg");
        return imgFile;
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize() {
        String path = FileHelper.getPathFactory(CacheType.ALL, true);
        if (TextUtils.isEmpty(path)) return "";
        File file = new File(path);
        if (!file.exists()) return "";
        long blockSize = 0;

        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return FormetFileSize(blockSize);
    }


    /**
     * 清除app缓存
     */
    public static void clearCache() {
        String path = FileHelper.getPathFactory(CacheType.ALL, true);
        if (TextUtils.isEmpty(path)) return;
        File file = new File(path);
        if (!file.exists()) return;
        deleteFolderFile(file.getAbsolutePath(), true);
    }

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        if (f.exists()) {
            File flist[] = f.listFiles();
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
        } else {
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 2      * 删除指定目录下文件及目录
     * 3      * @param deleteThisPath
     * 4      * @param filepath
     * 5      * @return
     * 6
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除

                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    //删除空白文件
    public static void deleteEmptyFiles(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteEmptyFiles(files[i].getAbsolutePath());
                    }
                }
                if (!file.isDirectory() && file.length() == 0) {// 如果是文件，删除

                    file.delete();

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 拍照临时文件(带时间戳)
     *
     * @return
     */
    public static File createTempFile(Context context, String postfix) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //已挂载
            File pic = createTempFolder();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "xinglian" + timeStamp;
            File tempFile = new File(pic, fileName + postfix);
            return tempFile;
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "xinglian" + timeStamp;
            File tempFile = new File(cacheDir, fileName + postfix);
            return tempFile;
        }
    }


    /**
     * 创建临时文件夹
     *
     * @return
     */
    public static File createTempFolder() {
        StringBuilder dirPath = new StringBuilder();
        dirPath.append(
                SDCardUtils.getSDCardRootDir())
                .append(File.separator).append("xinglian");
        File saveDir = new File(dirPath.toString());
        if (saveDir.exists() == false) {
            saveDir.mkdir();
        }

        // 如果生成不了文件夹，可能是外部SD卡需要写入特定目录/storage/sdcard1/Android/data/包名/
        if (saveDir.exists() == false) {
            dirPath.delete(0, dirPath.length());
            dirPath.append(
                    SDCardUtils.getSDCardRootDir())
                    .append(File.separator).append("Android")
                    .append(File.separator).append("data")
                    .append(File.separator)
                    .append("com.guangmo.xinglian")
                    .append(File.separator)
                    .append("xinglian");
            saveDir = new File(dirPath.toString());
            BaseApplication.getInstance().getExternalFilesDir(null); // 生成包名目录
            saveDir.mkdirs();
        }
        return saveDir;
    }

    /**
     * 针对非系统文件夹下的文件,使用该方法
     * 插入时初始化公共字段
     *
     * @param filePath 文件
     * @param time     ms
     * @return ContentValues
     */
    private static ContentValues initCommonContentValues(String filePath, long time) {
        ContentValues values = new ContentValues();
        File saveFile = new File(filePath);
        long timeMillis = getTimeWrap(time);
        values.put(MediaStore.MediaColumns.TITLE, saveFile.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, timeMillis);
        values.put(MediaStore.MediaColumns.DATE_ADDED, timeMillis);
        values.put(MediaStore.MediaColumns.DATA, saveFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, saveFile.length());
        return values;
    }

    /**
     * 保存到视频到本地，并插入MediaStore以保证相册可以查看到,这是更优化的方法，防止读取的视频获取不到宽高
     *
     * @param context    上下文
     * @param filePath   文件路径
     * @param createTime 创建时间 <=0时为当前时间 ms
     */
    public static void insertVideoToMediaStore(Context context, String filePath, long createTime) {
        if (!fileIsExists(filePath))
            return;
        createTime = getTimeWrap(createTime);
        ContentValues values = initCommonContentValues(filePath, createTime);
        values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, createTime);
//        if (duration > 0)
//            values.put(MediaStore.Video.VideoColumns.DURATION, duration);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
//            if (width > 0) values.put(MediaStore.Video.VideoColumns.WIDTH, width);
//            if (height > 0) values.put(MediaStore.Video.VideoColumns.HEIGHT, height);
//        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, getVideoMimeType(filePath));
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * 获取video的mine_type,暂时只支持mp4,3gp
     *
     * @param path
     * @return
     */
    private static String getVideoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("mp4") || lowerPath.endsWith("mpeg4")) {
            return "video/mp4";
        } else if (lowerPath.endsWith("3gp")) {
            return "video/3gp";
        }
        return "video/mp4";
    }

    /**
     * 获得转化后的时间
     */
    private static long getTimeWrap(long time) {
        if (time <= 0) {
            return System.currentTimeMillis();
        }
        return time;
    }

    /**
     * 判断文件是否存在
     *
     * @param strFile
     * @return
     */
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 是不是系统相册
     */
    public static boolean isSystemDcim(String path) {
        return path.toLowerCase().contains("dcim") || path.toLowerCase().contains("camera");
    }

    // 判断文件夹是否存在
    public static void judeDirExists(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                System.out.println("dir exists");
            } else {
                System.out.println("the same name file exists, can not create dir");
            }
        } else {
            System.out.println("dir not exists, create it ...");
            file.mkdir();
        }

    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     * * @param delpath
     *
     * @return boolean
     * @throws FileNotFoundException IOException
     */
    public static boolean deletefile(String delpath) throws Exception {
        File file = new File(delpath);
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (String delFile : filelist) {
                File delfile = new File(delpath + File.separator + delFile);
                if (delfile.isDirectory()) {
                    deletefile(delpath + File.separator + delFile);
                } else
                    System.out.println("正在删除文件：" + delfile.getPath() + ",删除是否成功：" + delfile.delete());
            }
            System.out.println("正在删除空文件夹：" + file.getPath() + ",删除是否成功：" + file.delete());
        } else
            System.out.println("正在删除文件：" + file.getPath() + ",删除是否成功：" + file.delete());
        return true;
    }

    /**
     * 创建文件夹
     *
     * @param path
     * @return
     */
    public static boolean mkDirectory(String path) {
        File file = null;
        try {
            file = new File(path);
            if (!file.exists()) {
                return file.mkdirs();
            } else {
                return false;
            }
        } catch (Exception e) {
        } finally {
            file = null;
        }
        return false;
    }

}
