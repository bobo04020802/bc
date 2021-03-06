package com.zltel.broadcast.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

/**
 * 文件相关处理 FileUtil class
 *
 * @author Touss
 * @date 2018/5/10
 */
public class FileUtil {
    
    private FileUtil() {}

    public static void createDir(String dir) {
        createDir(new File(dir));
    }

    public static void createDir(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 获取文件后缀
     * 
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
    }

    /**
     * 文件复制
     * 
     * @param source
     * @param dest
     * @param sourceDel
     * @throws IOException
     */
    public static void copy(File source, File dest, boolean sourceDel) throws IOException {
        FileUtils.copyFile(source, dest);
        if (sourceDel) {
            Files.delete(source.toPath());
        }
    }

    public static void copy(String source, String dest, boolean sourceDel) throws IOException {
        copy(new File(source), new File(dest), sourceDel);
    }

    public static String getYMD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }
}
