package org.server.util;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class FileUtil {


    public static boolean isFileExists(File file) {
        return file.exists() && !file.isDirectory();
    }


    public static final String zipExt = ".zip";

    public static final String txtRar = ".rar";

    public static final String txtExt = ".txt";

    public static final String txtJpg = ".jpg";

    public static final String txtJpeg = ".jpeg";


    public static final String txtPng = ".png";

    public static final List<String> supportedCompZip =
        Collections.unmodifiableList(Arrays.asList(zipExt));

    public static final List<String> supportedComImages =
        Collections.unmodifiableList(Arrays.asList(txtJpg,txtPng,txtJpeg));

    public static String getExtByFileName(String fileName) {
        String extension = "";

        if (fileName == null || fileName.isEmpty()) {
            return extension;
        }

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i);
        }

        return extension;
    }


    /** 刪除檔案
     * directory 檔案路徑
     * oldTime 要刪除的時間(離當前時間多久)
     * chronoUnit 時間單位
     * */
    public static void deleteOldFiles(File directory, int oldTime, ChronoUnit chronoUnit) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                long lastModifiedTime = file.lastModified();
                Instant lastModifiedDate = Instant.ofEpochMilli(lastModifiedTime);
                Instant oldInstant = Instant.now().minus(oldTime, chronoUnit);
                if (lastModifiedDate.isBefore(oldInstant)) {
                    boolean delete = file.delete();
                    if (delete) {
                        log.info("已刪除文件：{}",file.getAbsolutePath());
                    } else {
                        log.info("無法刪除文件：{}",file.getAbsolutePath());
                    }
                }
            } else if (file.isDirectory()) {
                deleteOldFiles(file, oldTime, chronoUnit);
            }
        }
    }


    /** 刪除路徑(最外層的路徑會留下)
     * directory 檔案路徑
     * */
    public static void deleteEmptyDirectories(File directory) {

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteEmptyDirectories(file);
                if (file.isDirectory() && file.listFiles().length == 0) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        log.info("已刪除空目錄：{}",file.getAbsolutePath());
                    } else {
                        log.info("無法刪除空目錄：{}",file.getAbsolutePath());
                    }
                }
            }
        }
    }
}
