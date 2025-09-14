package Utilities;

import BaseTest.*;

import java.io.File;
import java.io.IOException;

import static Utilities.ErrorHelpers.sendErrorMessageAndTakeScreenShot;
import static java.lang.Thread.sleep;

public class FileHelper extends BaseTest {

    static String StringLogError;

    public static String FilePath;

    public static void fileUpload(String pathname) throws IOException {

        try {
            File file = new File(pathname);
            if (file.exists()) {
                Log.info(" Dosya Bulundu...");
                Log.info(" Dosya ->>> " + file.getAbsolutePath());
                sleep(5000L);
            } else {
                Log.info(" Dosya Bulunamadi ->>> " + file);
            }
            FilePath = file.getAbsolutePath();

        } catch (Exception e) {
            FileHelper.FilePath = null;
            Log.info(" Dosya Path'î Sifirlandi");
            sendErrorMessageAndTakeScreenShot(" Dosyayı Ekleme Adiminda Hata Alindi " + e.getMessage());
        }

    }

}
