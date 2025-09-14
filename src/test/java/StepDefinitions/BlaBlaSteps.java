package StepDefinitions;

import BaseTest.Log;
import Utilities.ScreenshotHelper;
import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static BaseTest.BaseTest.LogCountManager;
import static BaseTest.BaseTest.driver;
import static org.reflections.Reflections.log;

public class BlaBlaSteps {


    //Excel'den Veri Almak İçin
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
        } catch (Exception e) {
            ScreenshotHelper.takeScreenShot();
            xpath = Log.error(" Excel'den veri okunup Xpath'li elemente yazma adımında hata alındı !!! " + e.getMessage());
            LogCountManager(xpath);
        }
    }

    //Ürün Bilgileri TXT Olarak Kayıt Edilir
    @Step("<urunAciklamaXpath> ve <tutarXpath> bilgileri verilen klasöre txt olarak kaydedilir")
    public void saveTextFromXpaths(String urunAciklamaXpath, String tutarXpath) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement urunAciklamaElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(urunAciklamaXpath)));
            WebElement tutarElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(tutarXpath)));

            String urunAciklama = urunAciklamaElement.getText().trim();
            String tutar = tutarElement.getText().trim();

            log.info("Ürün ve tutar bilgisi alındı.");

            // Dosya yolu
            System.out.println("test1");
            String klasorYolu = "files"; // burayı kendine göre değiştir
            File klasor = new File(klasorYolu);
            if (!klasor.exists()) {
                klasor.mkdirs();
            }
            System.out.println("test2");
            // Benzersiz dosya adı
            String dosyaAdi = "urunBilgisi_" + System.currentTimeMillis() + ".txt";
            File dosya = new File(klasor, dosyaAdi);
            System.out.println("test3");
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(dosya), StandardCharsets.UTF_8))) {
                writer.write("Ürün Açıklaması: " + urunAciklama);
                writer.newLine();
                writer.write("Tutar: " + tutar);
            }

            log.info("Ürün açıklaması ve tutar başarıyla kaydedildi: " + dosya.getAbsolutePath());

        } catch (Exception e) {
            log.error("Xpathlerden veri alınıp txt dosyasına kaydedilirken hata oluştu !!! " + e.getMessage());
            org.junit.Assert.fail("Senaryoda Hata Alındı");
        }
    }


    @Step("<fiyatXpath> xpathinden alınan fiyat txt dosyasındaki fiyat ile karşılaştırılır")
    public void comparePriceWithTxt(String fiyatXpath) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fiyatElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(fiyatXpath)));

            String siteFiyat = fiyatElement.getText().trim();
            log.info("Siteden okunan fiyat: " + siteFiyat);

            // Daha önce kaydedilen son dosyayı bul (Kayitlar klasörü)
            String klasorYolu = "files"; // senin path
            File klasor = new File(klasorYolu);
            File[] dosyalar = klasor.listFiles((dir, name) -> name.startsWith("urunBilgisi_") && name.endsWith(".txt"));

            if (dosyalar == null || dosyalar.length == 0) {
                log.error("Klasörde hiç fiyat dosyası bulunamadı!");
                Assert.fail("Karşılaştırma için txt dosyası yok.");
                return;
            }

            // En son kaydedilen dosyayı al
            File sonDosya = Arrays.stream(dosyalar)
                    .max(Comparator.comparingLong(File::lastModified))
                    .orElseThrow();

            log.info("Karşılaştırma yapılan dosya: " + sonDosya.getAbsolutePath());

            String kayitliFiyat = null;
            try (BufferedReader reader = new BufferedReader(new FileReader(sonDosya))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Tutar:")) {
                        kayitliFiyat = line.replace("Tutar:", "").trim();
                        break;
                    }
                }
            }

            if (kayitliFiyat == null) {
                log.error("Txt dosyasında 'Tutar:' satırı bulunamadı!");
                Assert.fail("Dosyada tutar bilgisi yok.");
                return;
            }

            log.info("Txt dosyasından okunan fiyat: " + kayitliFiyat);

            // Karşılaştırma
            if (siteFiyat.equals(kayitliFiyat)) {
                log.info(" Fiyatlar aynıdır. (" + siteFiyat + ")");
            } else {
                log.error(" Fiyatlar farklı! Site: " + siteFiyat + " | Txt: " + kayitliFiyat);
                Assert.fail("Senaryoda Hata Alındı - Fiyatlar uyuşmuyor.");
            }

        } catch (Exception e) {
            log.error("Fiyat karşılaştırma adımında hata oluştu !!! " + e.getMessage());
            Assert.fail("Senaryoda Hata Alındı");
        }
    }


    @Step("<xpath> xpathine kadar scroll edilir")
    public void scrollToElement(String xpath) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

            log.info(" " + xpath + " xpath'ine kadar scroll edildi.");
        } catch (Exception e) {
            log.error("Scroll adımında hata oluştu !!! " + e.getMessage());
            Assert.fail("Senaryoda Hata Alındı");
        }
    }


}
