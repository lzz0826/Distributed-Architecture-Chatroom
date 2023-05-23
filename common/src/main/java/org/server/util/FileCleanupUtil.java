package org.server.util;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class FileCleanupUtil {





  /**
   * directory 檔案路徑
   * oldTime 要刪除的時間(離現在多久)
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
            System.out.println("已刪除文件：" + file.getAbsolutePath());
          } else {
            System.out.println("無法刪除文件：" + file.getAbsolutePath());
          }
        }
      } else if (file.isDirectory()) {
        deleteOldFiles(file, oldTime, chronoUnit);
      }
    }
  }


  /**
   * directory 檔案路徑
   * */
  private static void deleteEmptyDirectories(File directory) {

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
            System.out.println("已刪除空目錄：" + file.getAbsolutePath());

          } else {
            System.out.println("無法刪除空目錄：" + file.getAbsolutePath());
          }
        }
      }
    }
  }


  public static void main(String[] args) {

    File file = new File("");
    if(!file.exists() || !file.isDirectory()){
      System.out.println("指定路徑不存在或不是目錄。");
      return;
    }
    deleteOldFiles(file,200,ChronoUnit.SECONDS);
    deleteEmptyDirectories(file);


  }


}


