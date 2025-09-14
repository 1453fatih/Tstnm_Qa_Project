package Utilities;

import BaseTest.Log;

import java.io.IOException;

import static BaseTest.BaseTest.LogCountManager;

public class ErrorHelpers {

    public static void sendErrorMessageAndTakeScreenShot(String errorMessage) throws IOException {
        ScreenshotHelper.takeScreenShot();
        String StringLogError = Log.error(errorMessage);
        LogCountManager(StringLogError);


    }


}