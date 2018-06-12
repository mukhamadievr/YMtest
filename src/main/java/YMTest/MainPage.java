package YMTest;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

public class MainPage {
    private WebDriver webDriver;
    private WebDriverWait wait;

    public MainPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        wait = new WebDriverWait(webDriver, 4, 500);

        PageFactory.initElements(webDriver, this);
    }

    @FindBy(id = "header-search")
    WebElement searchField;

    @FindBy(css = "span.search2__button")
    WebElement searchButton;

    @FindBy(css = "div.n-filter-sorter:nth-child(3) > a:nth-child(1)")
    WebElement sortPrice;

    @FindBy(css = "div.n-snippet-cell2:nth-child(1) > div:nth-child(4) > div:nth-child(2) > a:nth-child(1)")
    WebElement firstItem;

    @FindBy(css = ".n-shop-name__name")
    WebElement shopNameField;

    @FindBy(css = "div.n-product-price-cpa2 > span")
    WebElement priceField;

    @Step("Getting the list \"Product - Price\"")
    public List<Item> search(String name) {
        searchField.clear();
        //ввод в строку поиска:
        searchField.sendKeys(name);
        //клик по "Найти":
        searchButton.click();

        List<WebElement> webElements = webDriver.findElements(By.cssSelector("div.n-snippet-cell2"));
        List<Item> itemList = new ArrayList();

        for (WebElement element : webElements) {
            String model = element.findElement(By.className("n-snippet-cell2__title")).getText();
            String price;
            if (element.findElements(By.className("price")).size() > 0) {
                price = element.findElement(By.className("price")).getText();
            } else {
                price = element.findElement(By.className("n-snippet-cell2__main-price")).getText();
            }
            Item item = new Item(model, price);
            itemList.add(item);
        }
        makeScreenshot();
        return itemList;
    }

    @Step("Check: is an item in the list")
    public boolean hasItemInTheList(String name, List<Item> list) {
        if (list.size() > 0) {
            for (Item item : list) {
                if (item.getName().toLowerCase().contains(name.toLowerCase())) {
                    Assert.assertTrue(true, "There are no searchable item in the list");
                    return true;
                }
            }
        }
        return false;
    }

    @Step("Sort by lowest price")
    public void clickSortPriceAndWait() throws InterruptedException{
        sortPrice.click();
        Thread.sleep(3000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.n-snippet-cell2:nth-child(1) > div:nth-child(4) > div:nth-child(2) > a:nth-child(1)")));
        makeScreenshot();
    }

    @Step("Click the cheapest product")
    public void clickFirstItem() {
        firstItem.click();
        makeScreenshot();
    }

    @Step("Show store names and prices")
    public void showShopNameAndPrice() {
        String shop = shopNameField.getText();
        String price = priceField.getText();
        System.out.println("Shop: " + shop);
        System.out.println("Price: " + price);
        makeScreenshot();
    }

    @Attachment("Screenshot")
    public byte[] makeScreenshot() {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
    }
}
