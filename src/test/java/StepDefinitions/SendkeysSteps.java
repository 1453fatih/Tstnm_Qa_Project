package StepDefinitions;

import BaseTest.*;
import Utilities.ScreenshotHelper;

import com.thoughtworks.gauge.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.time.Duration;

import static java.lang.Thread.sleep;
import static StepDefinitions.WaitSteps.*;
import static org.reflections.Reflections.log;

public class SendkeysSteps extends BaseTest {




    @Step("<xpath> xpath'li field temizlenir")
    public void clearFieldXPathWithoutWait(String xpath) throws IOException {
        try{
            Log.info(" XPath'li Field'ın Temizlenmesi Basladi...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated (By.xpath(xpath)));
            Log.info(" Field'in Ici Temizleniyor...");
            element.clear();
            Log.info(" Field'in Ici Temizlendi...");
        }
        catch (Exception e){
            ScreenshotHelper.takeScreenShot();
            StringLogError = Log.error(" XPath'li Element Temizlenemedi !!! " + e.getMessage());
            LogCountManager(StringLogError);
        }
    }

    @Step("<xpath> xpath'li elemente <string> yazılır")
    public void sendKeystoXPathWithoutWait(String xpath ,String string) throws IOException {
        try{
            Log.info(" XPath'li Elemente String Degeri Yazdirma Islemi Basladi...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated (By.xpath(xpath)));
            Log.info(" Field'in Ici Temizleniyor...");
            element.clear();
            Log.info(" Field'in Ici Temizlendi...");
            element.sendKeys(string);
            Log.info(" Field'a "+ string + " Yazildi...");
        }
        catch (Exception e){
            ScreenshotHelper.takeScreenShot();
            StringLogError = Log.error(" XPath'li Elemente String Yazilamadi !!! " + e.getMessage());
            LogCountManager(StringLogError);
        }
    }






    @Step("<xpath> xpath'li elemente <string> Yazılamadığı Kontrol Edilir")
    public void sendKeystoXPathCanNotWriteCheck(String xpath, String string) throws IOException {
        try {
            Log.info(" XPath'li Elemente String Degeri Yazdirma Islemi Basladi...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            element.clear();
            element.sendKeys(string);
            Log.info(" Field'a " + string + " Yazilamadigi Kontrol Ediliyor");

            throw new Exception("Elemente string değer yazılabildi!");

        } catch (Exception e) {
            if (e.getMessage().equals("Elemente string değer yazılabildi!")) {
                ScreenshotHelper.takeScreenShot();
                StringLogError = Log.error("XPath'li Elemente String Yazılabiliyor: " + e.getMessage());
                LogCountManager(StringLogError);
            } else {
                Log.info(" Field'a "+ string + " Yazilamadi...");

            }
        }
    }


    @Step("Enter tuşuna basılır")
    public void pressEnterKey() {
        try {
            WebElement activeElement = driver.switchTo().activeElement();
            activeElement.sendKeys(Keys.ENTER);
            log.info("Klavyeden Enter tuşuna basıldı.");
        } catch (Exception e) {
            log.error("Enter tuşuna basılırken hata oluştu !!! " + e.getMessage());
            org.junit.Assert.fail("Senaryoda Hata Alındı");
        }
    }





        @Step("<xpath> alanındaki yazılar silinene kadar Backspace tuşuna basılır")
        public void clearTextByBackspace(String xpath) {
            try {
                WebElement element = driver.findElement(By.xpath(xpath));
                String value = element.getAttribute("value");

                while (value != null && !value.isEmpty()) {
                    element.sendKeys(Keys.BACK_SPACE);
                    Thread.sleep(50); // çok hızlı olmaması için küçük bekleme
                    value = element.getAttribute("value");
                }

                log.info(xpath + " alanındaki tüm yazılar Backspace ile temizlendi.");
            } catch (Exception e) {
                log.error("Text alanını Backspace ile temizlerken hata oluştu !!! " + e.getMessage());
                org.junit.Assert.fail("Senaryoda Hata Alındı");
            }
        }


}








