package YMTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class YandexMarketTest {
    private static WebDriver webDriver;
    private String model;
    private MainPage mainPage;
    private List<Item> itemList;

    @BeforeTest
    public void setup() {
        //model = "Samsung Galaxy J5 Prime";
        model = "iphone x";

        System.setProperty("webdriver.chrome.driver", "D:\\renat\\java\\chromedriver\\chromedriver.exe");
        webDriver = new ChromeDriver();
        mainPage = new MainPage(webDriver);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.get("https://market.yandex.ru/");
    }


    @Test
    public void testCellphoneSearch() throws InterruptedException {

        itemList = mainPage.search(model);

        mainPage.hasItemInTheList(model, itemList);

        mainPage.clickSortPriceAndWait();

        mainPage.clickFirstItem();

        mainPage.showShopNameAndPrice();
    }

    @AfterTest
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}