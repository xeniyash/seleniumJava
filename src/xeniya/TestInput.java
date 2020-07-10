package xeniya;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestInput extends Base {

    @Test(dataProvider = "simpleDataProvider", dataProviderClass = SimpleDataProvider.class)
    public void secondTest(String name, String year, String item, String price) throws Exception {
        startReport("xeniya\\reports\\" + name + ".report");

        WebDriver driver = getDriver();
        driver.get("http://www.softslate.com/demo/page/welcome");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.findElement(By.id("subcategory-YEARS")).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        try {
            if (year != null) {
                driver.findElement(By.id("subcategory-" + year)).click();
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            } else {
                report("Not enough data to test");
                throw new RuntimeException("Not enough data to test");

            }

            String buf = driver.findElement(new By.ByCssSelector(".productListPagination li")).getText();

            int totalPages = Integer.valueOf(buf.split("of")[1].trim());
            int currentPage = 1;
            int totalColumns = 3;

            for (int page = currentPage; page <= totalPages; page++) {
                List<WebElement> products = driver.findElements(By.cssSelector(".productLink"));
                for (int i = 0; i < products.size(); i++) {
                    String elName = products.get(i).findElement(By.cssSelector("div .name")).getText();
                    String elPrice = products.get(i).findElement(By.cssSelector("div .price")).getText();
                    int column = (i % totalColumns);
                    if (item != null && !item.isEmpty() && price != null && !price.isEmpty()) {
                        if (elName.matches("(?i:.*" + item + ".*)") && elPrice.contains(price.replace(".00", ""))) {
                            report("found on a page " + currentPage + " column: " + (column + 1) + " row: " + (((i + 1) / (3)) + 1));
                            return;
                        }
                    } else if (item != null && !item.isEmpty()) {
                        if (elName.matches("(?i:.*" + item + ".*)")) {
                            report("found on a page " + currentPage + " column: " + (column + 1) + " row: " + (((i + 1) / (3)) + 1));
                            return;
                        }
                    } else if (price != null && !price.isEmpty()) {
                        if (elPrice.contains(price.replace(".00", ""))) {
                            report("found on a page " + currentPage + " column: " + (column + 1) + " row: " + (((i + 1) / (3)) + 1));
                            return;
                        }
                    } else {
                        report("Not enough data to test");
                        return;
                    }
                }
                WebElement link = driver.findElement(By.linkText("Next »"));
                currentPage++;
                link.click();
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            }
        } catch (RuntimeException re) {
            throw new RuntimeException("Not enough data to test");
        }
        report(name, year, item, price);
    }

}
