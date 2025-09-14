package BaseTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.gauge.*;
import Utilities.ScreenshotHelper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.junit.Assert.fail;

public class BaseTest {

    public static ChromeDriver driver;
    protected static int LogCounter;
    protected static int AssertCounter;
    static File Screenshott;
    static String PathofScreenshot;
    static boolean isErrorDetect;
    static int ScreenshotCounter;
    protected static String StringLogError;
    protected static String StringAssertError;


    public static void LogCountManager(String StringLogError) {

        Log.info(" -------------------------- ");
        Log.info(" Log Counter Cagirildi !!! ");


        Log.info(" Log Counter Onceki Deger ->>> " + LogCounter);
        LogCounter++;

        Log.info(" Log Counter Guncel Deger ->>> " + LogCounter);
        Log.info(" -------------------------- ");
    }

    public static void AssertCountManager(String StringAssertError) {

        Log.info(" -------------------------- ");
        Log.info(" Assert Counter Cagirildi !!! ");

        Log.info(" Assert Counter Onceki Deger ->>> " + AssertCounter);
        AssertCounter++;

        Log.info(" Assert Counter Guncel Deger ->>> " + AssertCounter);
        Log.info(" -------------------------- ");
    }






    public static void getScreenShot(File GetScreenshot) {

        if (ScreenshotCounter == 0) {
            Screenshott = GetScreenshot;
            Log.info(" Screenshot ->>> " + Screenshott.getName());
            Log.info(" Screenshot Counter Onceki Deger ->>> " + ScreenshotCounter);
            ScreenshotCounter++;
            Log.info(" Screenshot Counter Onceki Deger ->>> " + ScreenshotCounter);
        } else {
            Log.info(" -------------------------- ");
            Log.info(" Screenshot Counter Onceden Cagirildi !!! ->>> " + ScreenshotCounter);
            Log.info(" -------------------------- ");

        }
    }



    public static void resetAll() {

        try {
            Log.info(" Sıfırlama Islemleri Basladi");
            Screenshott = null;
            ScreenshotCounter = 0;
            Log.info(" Sıfırlama Islemleri Tamamlandi");

        } catch (Exception e) {
            Log.error(" Sifirlama Adiminda Hata Alindi !!! " + e.getMessage());
        }
    }

    @BeforeSuite
    public void cleanPhase() {

        ScreenshotHelper.clearScreenShots();
        ScreenshotHelper.clearJavaExeOutLogs();

    }



    @BeforeScenario
    public static void setUp() {

        // Initialize default values for test variables
        PathofScreenshot = " ";
        isErrorDetect = false;

        try {
            // Step 1: WebDriverManager dinamik olarak kurulur.
            // System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--lang=tr");
            options.addArguments("--test-type");
            options.addArguments("--disable-extensions");
            options.addArguments("start-maximized");
            //options.addArguments("--window-size=2524,1094");


            ChromeDriverService driverService = ChromeDriverService.createDefaultService();
            driver = new ChromeDriver(driverService, options);

            Map<String, Object> commandParams = new HashMap<>();
            commandParams.put("cmd", "Page.setDownloadBehavior");

            Map<String, String> params = new HashMap<>();
            String path = "Screenshots";
            params.put("behavior", "allow");
            params.put("downloadPath", path);


            commandParams.put("params", params);
            ObjectMapper objectMapper = new ObjectMapper();
            HttpClient httpClient = HttpClientBuilder.create().build();
            String command = objectMapper.writeValueAsString(commandParams);
            String u = driverService.getUrl().toString() + "/session/" + driver.getSessionId() + "/chromium/send_command";
            HttpPost request = new HttpPost(u);
            request.addHeader("content-type", "application/json");
            request.setEntity(new StringEntity(command));
            httpClient.execute(request);
            // Log the screen resolution for debugging
            Log.info(" Cozunurluk ->>> " + driver.manage().window().getSize());

        } catch (Exception e) {
            Log.error(" Driver Ayaga Kalkmadi !!! " + e.getMessage());
        }

    }


    @AfterStep
    public final void checkStepHasError() throws IOException {

        if ((LogCounter > 0) || (AssertCounter > 0)) {
            isErrorDetect = true;
            fail("Senaryoda Hata Alındı");
            Log.info("isError: " + isErrorDetect);
            driver.quit();

        }
    }

    @AfterScenario
    public final void tearDown() throws IOException {

        driver.quit();
        try {
            if (isErrorDetect) {


                Log.info(" -------------------------- ");
                Log.info("Hatalı...");

                PathofScreenshot = "Screenshots/";
                try {
                    Screenshott = new File(Screenshott.getName());

                } catch (Exception e) {
                    Log.info("Screenshot dosyası Boş");
                }
            } else {



                PathofScreenshot = " ";
            }


        } catch (Exception e) {
            Log.error(" Hata Alindi !!! " + e.getMessage());
        }



        AssertCounter = 0;
        LogCounter = 0;
        resetAll();

        try {
            Thread.sleep(1000);
            Log.info(" Driver'dan Cikiliyor...");
            driver.quit();
            Log.info(" Driver'dan Kapatildi...");
        } catch (Exception e) {
            Log.error(" Driver Sonlandirma Adiminda Hata Alindi !!! " + e.getMessage());
        }

    }

    @AfterSuite
    public void afterSuite() throws IOException {



        try {
            Log.info("Başarıyla Koşum Sağlandı");
        } catch (Exception e) {
            Log.info("Koşumda Hata Alındı:" + e.getMessage());
        }








            }
        }











