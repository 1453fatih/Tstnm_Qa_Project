package StepDefinitions;

import BaseTest.*;

import com.thoughtworks.gauge.Step;
import org.openqa.selenium.*;
import Utilities.*;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.IOException;
import java.time.Duration;


public class ClickSteps extends BaseTest {


    @Step("<xpath> xpath'li elemente tıklanır")
    public void clicktoXpathWithoutWait(String xpath) throws IOException {
        try {
            Log.info(" XPath ile Elemente Tiklama Adimi Basladi...");
            WebElement element = driver.findElement(By.xpath(xpath));
            element.click();
            Log.info(" " + xpath + " XPath'li Elemente Tiklandi...");
        } catch (Exception e) {
            ScreenshotHelper.takeScreenShot();
            StringLogError = Log.error(" XPath'li Elemente Tiklama Adiminda Hata Alindi !!! " + e.getMessage());
            LogCountManager(StringLogError);
        }
    }


    @Step("<xpath> xpath'li element görünene kadar bekleyip tıklanır")
    public void clicktoXpathVisibilityOfElementLocated(String xpath) throws IOException {
        try {
            Log.info(" XPath'li Element Gorunene Kadar Bekleyip Tiklama Adimi Basladi...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            element.click();
            Log.info(" " + xpath + " XPath'li Element Gorunene Kadar Bekleyip Tiklama Adimi Basarili...");
        } catch (Exception e) {
            ScreenshotHelper.takeScreenShot();
            StringLogError = Log.error(" XPath'li Element Gorunene Kadar Bekleyip Tiklama Adiminda Hata Alindi !!! " + e.getMessage());
            LogCountManager(StringLogError);
        }
    }


}
