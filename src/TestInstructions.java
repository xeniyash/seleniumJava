import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestInstructions {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "drivers/Chrome/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.navigate().to("http://www.csexperts.com/CSETechnicalTest/instructions_external.html");
	}

}
