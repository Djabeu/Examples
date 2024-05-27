package lib;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.net.URL;
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
            System.out.println(exception.getMessage());
        }

        webDriver.quit();
    }

    private static void script(WebDriver webDriver) throws IOException {
        webDriver.get("https://9gag.com/u/srmaryhermanngo/posts");
        List<WebElement> videos = webDriver.findElements(By.xpath(".//video"));
        Map<String, String> sourcesAndTitles = new HashMap<>();

        for (WebElement video : videos) {
            WebElement headerElement = video.findElement(By.xpath("./../../../../../header/a/h2"));
            String title = fixTitle(headerElement.getText());

            List<WebElement> sourcesElements = video.findElements(By.xpath("./source"));

            for (WebElement sourceElement : sourcesElements) {
                String sourceSrc = sourceElement.getAttribute("src");
                if (sourceSrc.endsWith(".mp4")) {
                    sourcesAndTitles.put(sourceElement.getAttribute("src"), title);
                }
            }
        }

//        downloadFiles(sourcesAndTitles);
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

