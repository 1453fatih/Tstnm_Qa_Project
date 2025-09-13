package StepDefinitions;

import Utilities.*;
import BaseTest.*;

import WebAutomationBase.helper.ElementHelper;
import WebAutomationBase.helper.StoreHelper;
import WebAutomationBase.model.ElementInfo;
import com.thoughtworks.gauge.Logger;
import com.thoughtworks.gauge.Step;

import com.thoughtworks.gauge.datastore.SuiteDataStore;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

import static java.lang.Thread.sleep;



public class WaitSteps extends BaseTest {


    public static WebElement findElement(String key) {
        try {
            ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
            By infoParam = ElementHelper.getElementInfoToBy(elementInfo);

            WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(infoParam));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                    webElement);
            return webElement;
        } catch (Exception e) {
            return null;
        }
    }

    public static void waitForPageLoad(WebDriver driver) throws InterruptedException {
        sleep(50);
        try {
            Log.info(" Sayfa Yuklenene Kadar Bekleniyor...");
            (new WebDriverWait(driver, Duration.ofSeconds(10)))
                    .until(new ExpectedCondition<Boolean>() {
                        public Boolean apply(WebDriver driver) {
                            return  ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                        }
                    });
        } catch (TimeoutException ex) {
            Log.info(" Sayfa Yuklenene Kadar Bekle Fonksiyonda Hata Alindi !!!");
            ((JavascriptExecutor)driver).executeScript("window.stop();");
            sleep(500);
        }
    }
    Integer unloadedPageCount = (Integer) SuiteDataStore.get("unloadedPageCount");

    @Step("<link> linkine gidilir ve sayfa yüklenene kadar beklenir")
    public void gotoLink(String link) throws IOException, InterruptedException {
        try {
            Log.info(link + " Adresine Gidiliyor...");
            driver.get(link);
            waitForPageLoad(driver);
            Log.info(link + " Adresine Gidildi...");
        } catch (Exception e) {
            // SuiteDataStore'dan unloadedPageCount değerini al ve artır

            unloadedPageCount++;

            // Artırılmış değeri tekrar SuiteDataStore'a kaydet
            SuiteDataStore.put("unloadedPageCount", unloadedPageCount);

            ScreenshotHelper.takeScreenShot();
            StringLogError = Log.error(link + " Adresine Gidilemedi !!! " + e.getMessage());
            LogCountManager(StringLogError);
            sleep(90000);


            // Sayfaya Erisememe sayısını kontrol et
            if (unloadedPageCount >= 5) {
                Logger.error("Belirtilen Url'e "+ unloadedPageCount + " Kez Gidilemedigi Icin Otomasyon Durduruldu");
                // BaseTest.driver.quit();
                // Runtime.getRuntime().exit(0);
            }
        }
    }

    @Step("<seconds> saniye beklenir")
    public void waitSeconds(int seconds) throws InterruptedException {
        Log.info(" " + seconds + " Saniye Bekleniyor...");
        sleep(1000L * seconds);
    }


}
