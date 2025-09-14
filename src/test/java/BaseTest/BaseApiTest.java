package BaseTest;

import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.AfterScenario;
import io.restassured.RestAssured;

public class BaseApiTest {

    protected static final String BASE_URL = "https://api.trello.com/1";
    public static final String KEY = "xx";
    public static final String TOKEN = "xx";

    @BeforeScenario
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        System.out.println("API Test başlatıldı.");
    }

    @AfterScenario
    public void tearDown() {
        System.out.println("API Test bitti.");
    }
}
