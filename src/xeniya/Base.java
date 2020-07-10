package xeniya;


import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Base {

    private static Logger log = Logger.getLogger(Base.class);

    public static final String DRIVER_TYPE = "driver.type";
    public static final String IE_DRIVER = "ie";
    public static final String FIREFOX_DRIVER = "firefox";
    public static final String CHROME_DRIVER = "chrome";
    public static final String HTML_DRIVER = "html";
    public static final String CHROME_LOCATION = "chrome.location";
    public static final String IE_LOCATION = "ie.location";
    public static final String CHROME_PROPERTY = "webdriver.chrome.driver";
    public static final String IE_PROPERTY = "webdriver.ie.driver";

    private boolean initialized = false;
    private FileWriter reporter = null;
    private boolean isReported = false;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private WebDriver driver = null;
    private Properties props = null;

    public void init() {
        if (!initialized)
            try {
                log.debug("Initialization ...");
                props = new Properties();
                props.load(new FileInputStream(new File("config.properties")));
                switch (props.getProperty(DRIVER_TYPE, HTML_DRIVER)) {
                    case CHROME_DRIVER: {
                        System.setProperty(CHROME_PROPERTY, props.getProperty(CHROME_LOCATION));
                        driver = new ChromeDriver();
                        break;
                    }
                    case FIREFOX_DRIVER: {
                        driver = new FirefoxDriver();
                        break;
                    }
                    case IE_DRIVER: {
                        System.setProperty(IE_PROPERTY, props.getProperty(IE_LOCATION));
                        driver = new InternetExplorerDriver();
                    }
                    case HTML_DRIVER: {
                        driver = new HtmlUnitDriver();
                    }
                }
                log.debug("Success.");
            } catch (Exception e) {
                log.error("Cannot find configuration file. Tests stated with default options");
                driver = new HtmlUnitDriver();
            } finally {
                initialized = true;
            }
    }

    public WebDriver getDriver() {
        if (driver == null) {
            init();
            if (driver == null) {
                log.error("Could not init WebDriver!");
                System.exit(1);
            }
        }
        return driver;
    }

    @AfterSuite
    public void cleanUp() {
        try {
            driver.quit();
        } finally {
            initialized = false;
        }

    }

    public void startReport(String fileName) {
        try {
            reporter = new FileWriter(new File(fileName));
            reporter.write("\n" + dateFormat.format(new Date()) + " - Test started" + System.lineSeparator());
            reporter.flush();
            isReported = true;
        } catch (IOException e) {
            log.error(e);
            isReported = false;
        }
    }

    public void report(Object... args) {
        if (isReported) {
            try {
                reporter.write(dateFormat.format(new Date()) + " ");
                for (Object str : args) {
                    reporter.write(str.toString() + " ");
                }
                reporter.write(System.lineSeparator());
                reporter.flush();
            } catch (IOException e) {
                log.error(e);
                isReported = false;
            }
        }
    }

    public void report(Object obj) {
        if (isReported) {
            try {
                reporter.write(dateFormat.format(new Date()) + " ");
                reporter.write(obj.toString() + System.lineSeparator());
                reporter.flush();
            } catch (IOException e) {
                log.error(e);
                isReported = false;
            }
        }
    }

    @AfterMethod
    public void finishReport() {
        if (reporter != null || isReported)
            try {
                reporter.close();
            } catch (IOException | NullPointerException e) {
                log.error(e);
            } finally {
                reporter = null;
                isReported = false;
            }
    }

}
