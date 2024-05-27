import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        getCartoonsFrom9gag();
    }

    private static void getCartoonsFrom9gag() throws IOException {

        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setCssEnabled(false);

        HtmlPage postsPage = client.getPage("https://9gag.com/u/srmaryhermanngo/posts");
        String source = postsPage.getWebResponse().getContentAsString();
        List<HtmlElement> posts = postsPage.getByXPath("//div[contains(@id, 'page')]");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println();

    }

    private static void getLegoOffers() throws IOException {

        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);

        HtmlPage searchPage = client.getPage("https://www.lego.com/pl-pl/categories/sales-and-deals?page=2&sort.key=PRICE&sort.direction=DESC&filters.i0.key=variants.attributes.availabilityStatus.zxx-PL&filters.i0.values.i0=%22E_AVAILABLE%22&inStockOnly=1&offset=0");
        HtmlUnorderedList htmlUnorderedList = searchPage.getFirstByXPath("//*[@id=\"product-listing-grid\"]");

        DomNodeList<HtmlElement> elementsByTagName = htmlUnorderedList.getElementsByTagName("li");

        ArrayList<HtmlElement> legoSets = new ArrayList<>();

        for (HtmlElement element : elementsByTagName) {
            if (element.getAttributes().getLength() == 2)
                legoSets.add(element);
        }

        for (HtmlElement htmlElement : legoSets) {
            DomElement nameSpan = htmlElement.getFirstByXPath("./article/h3/a/span");
            String name = nameSpan.getTextContent();
            DomElement priceSpan = htmlElement.getFirstByXPath("./article/div[2]/span[2]");
            String price = priceSpan.getTextContent();
            DomElement discountSpan = htmlElement.getFirstByXPath("./article/div[2]/span[3]/span");
            String discount = discountSpan.getTextContent();

            System.out.println(name);
            System.out.println(price);
            System.out.println(discount);

            System.out.println();
        }

    }

}

