package StepDefinitions;

import Utilities.*;
import BaseTest.*;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

import static java.lang.Thread.sleep;


public class WaitSteps extends BaseTest {


    public static void waitForPageLoad(WebDriver driver) throws InterruptedException {
        sleep(50);
        try {
            Log.info(" Sayfa Yuklenene Kadar Bekleniyor...");
            (new WebDriverWait(driver, Duration.ofSeconds(10)))
                    .until(new ExpectedCondition<Boolean>() {
                        public Boolean apply(WebDriver driver) {
                            return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                        }
                    });
        } catch (TimeoutException ex) {
            Log.info(" Sayfa Yuklenene Kadar Bekle Fonksiyonda Hata Alindi !!!");
            ((JavascriptExecutor) driver).executeScript("window.stop();");
            sleep(500);
        }
    }


    @Step("<link> linkine gidilir ve sayfa yüklenene kadar beklenir")
    public void gotoLink(String link) throws IOException, InterruptedException {
        try {
            Log.info(link + " Adresine Gidiliyor...");
            driver.get(link);
            waitForPageLoad(driver);
            Log.info(link + " Adresine Gidildi...");
        } catch (Exception e) {


            ScreenshotHelper.takeScreenShot();
            StringLogError = Log.error(link + " Adresine Gidilemedi !!! " + e.getMessage());
            LogCountManager(StringLogError);

        }
    }

    @Step("<seconds> saniye beklenir")
    public void waitSeconds(int seconds) throws InterruptedException {
        Log.info(" " + seconds + " Saniye Bekleniyor...");
        sleep(1000L * seconds);
    }


}
