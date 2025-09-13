package StepDefinitions;

import BaseTest.BaseTest;
import com.github.javaparser.utils.Log;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utilities.ScreenshotHelper;
import java.io.IOException;
import java.time.Duration;

public class AssertionSteps extends BaseTest {

    @Step("<xpath> xpath'li elementin var olduğu kontrol edilir")
    public void checkElementExists(String xpath) throws IOException {
        try {
            Log.info("'" + xpath + "' xpath'li elementin varlığı kontrol ediliyor...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            Log.info("Element başarıyla bulundu.");
        } catch (Exception e) {
            ScreenshotHelper.takeScreenShot();
            Log.error("'" + xpath + "' xpath'li element bulunamadı! " + e.getMessage());
            AssertCountManager(StringAssertError);
        }
    }
}