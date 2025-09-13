package StepDefinitions;

import BaseTest.Log;
import Utilities.ScreenshotHelper;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import static BaseTest.BaseTest.LogCountManager;
import static BaseTest.BaseTest.driver;

public class BlaBlaSteps {

    @Step("<row> satır <col> sütunundaki excel verisini <xpath> xpath'li elemente yaz")
    public void writeExcelDataToXpath(int row, int col, String xpath) throws IOException {
        try {
            Log.info(" Excel'den veri okunup Xpath'li elemente yazma adımı başladı...");

            // Excel dosyasının yolu (proje dizinindeki data.xlsx örnek)
            String excelPath = "src/test/resources/data.xlsx";
            FileInputStream fis = new FileInputStream(excelPath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // İlk sayfa

            // Excel hücresinden veri oku
            Row excelRow = sheet.getRow(row - 1); // 1 tabanlı index -> 0 tabanlıya çevir
            Cell cell = excelRow.getCell(col - 1);
            String cellValue = cell.toString();

            workbook.close();
            fis.close();

            // Element bulunup veri yazılır
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            element.clear();
            element.sendKeys(cellValue);

            Log.info(" " + row + ". satır " + col + ". sütundaki değer [" + cellValue +
                    "] " + xpath + " xpath'li elemente başarıyla yazıldı...");
        }
        catch (Exception e) {
            ScreenshotHelper.takeScreenShot();
            xpath = Log.error(" Excel'den veri okunup Xpath'li elemente yazma adımında hata alındı !!! " + e.getMessage());
            LogCountManager(xpath);
        }
    }

}
