package lib;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Rafal\\Documents\\GitHub\\Examples\\Selenium9gagVideoDownloader\\src\\main\\resources\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();

        try {
            script(webDriver);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        webDriver.quit();
    }

    private static void script(WebDriver webDriver) throws IOException {
        webDriver.get("https://9gag.com/u/srmaryhermanngo/posts");
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.parse("PT10S"));
        WebElement cookiesButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("onetrust-accept-btn-handler")));
        cookiesButton.click();

        getSourcesAndTitles(webDriver);

//        downloadFiles(sourcesAndTitles);
    }

    private static void getSourcesAndTitles(WebDriver webDriver) {
        Map<String, String> sourcesAndTitles = new HashMap<>();

        findSourcesAndTitles(sourcesAndTitles, webDriver);

    }

    private static void findSourcesAndTitles(Map<String, String> sourcesAndTitles, WebDriver webDriver) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        List<WebElement> videos = webDriver.findElements(By.xpath(".//video[@preload='auto']"));

        for (WebElement video : videos) {
            WebElement headerElement = video.findElement(By.xpath("./../../../../../header/a/h2"));
            String title = fixTitle(headerElement.getText());
            if (!sourcesAndTitles.containsValue(title)) {
                js.executeScript("arguments[0].scrollIntoView(true);", headerElement);
            }

            List<WebElement> sourcesElements = video.findElements(By.xpath("./source"));

            for (WebElement sourceElement : sourcesElements) {
                String sourceSrc = sourceElement.getAttribute("src");
                if (sourceSrc.endsWith(".mp4")) {
                    if(!sourcesAndTitles.containsKey(sourceElement.getAttribute("src"))){
                        sourcesAndTitles.put(sourceElement.getAttribute("src"), title);
                    }
                    if (sourcesAndTitles.size() == videos.size()) {
                        js.executeScript("window.scrollBy(0,1000)", "");
                        findSourcesAndTitles(sourcesAndTitles, webDriver);
                    }
                }
            }
        }
    }

    private static String fixTitle(String title) {
        if(title.contains("\"")) {
            title = title.replace("\"", "'");
        }
        return title;
    }


    private static void downloadFiles(Map<String, String> sourcesAndTitles) throws IOException {

        for (Map.Entry<String, String> sourceAndTitle : sourcesAndTitles.entrySet()) {
            String source = sourceAndTitle.getKey();
            String title = sourceAndTitle.getValue();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(source).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Rafal\\Desktop\\VIDEO\\" + title + ".mp4");

            int count;
            byte[] b = new byte[100];

            while ((count = bufferedInputStream.read(b)) != -1) {
                fileOutputStream.write(b, 0, count);
            }
        }
    }
}

