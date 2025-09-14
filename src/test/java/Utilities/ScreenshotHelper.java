package Utilities;

import BaseTest.*;
import BaseTest.Log;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;

public class ScreenshotHelper extends BaseTest {
    static File screenshot;


    public static void takeScreenShot() throws IOException {


        try {
            Log.info(" Ekran Goruntusu Aliniyor ...");
            screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File("Screenshots/" + screenshot.getName()));
            Log.info(" Ekran Goruntusu Alindi  ... " + screenshot.getName());
            getScreenShot(screenshot);


        } catch (IOException e) {
            StringLogError = Log.error(" Ekran Goruntusu Alinamadi !!! " + e.getMessage());
            LogCountManager(StringLogError);
        }
    }


    // ScreenShots temizleme işlemi her koşum öncesinde çalışıyor ve buradan yönetiliyor
    public static void clearScreenShots() {
        try {
            File directory = new File("Screenshots/");

            if (directory.exists() && directory.isDirectory()) {

                File[] files = directory.listFiles();
                if (files != null) {
                    boolean isDeleted = false;
                    int imageCount = files.length;
                    for (File file : files) {

                        if (file.isFile()) {
                            isDeleted = file.delete();
                        }
                    }
                    Log.info("Toplam Silinen Ekran Görüntüsü Sayısı:" + imageCount);
                }
            }
        } catch (NullPointerException e) {
            Log.info("Dizin Mevcut Degil Veya Bir Dizin Degil." + e.getMessage());
        }
    }

    // java.exe.out Dosyaları her koşumdan önce siliniyor ve buradan yönetiliyor
    public static void clearJavaExeOutLogs() {
        try {
            File directory = new File("logs/");

            if (directory.exists() && directory.isDirectory()) {

                File[] files = directory.listFiles();
                if (files != null) {
                    boolean isDeleted = false;
                    int logCount = 0;
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith("-java.exe.out")) {
                            isDeleted = file.delete();
                            logCount++;
                        }
                    }
                    Log.info("Toplam Silinen Log Dosyası Sayisi:" + logCount);
                }
            }
        } catch (NullPointerException e) {
            Log.info("Logs Dizini Mevcut Değil Veya Bir Dizin Değil." + e.getMessage());
        }
    }

}



