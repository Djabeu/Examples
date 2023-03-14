import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {

        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        HtmlPage searchPage = client.getPage("https://new.uschess.org/player-search");
        HtmlForm form = (HtmlForm) searchPage.getByXPath("//form").get(0);
        HtmlInput displayNameField = form.getInputByName("display_name");
        HtmlInput submitButton = form.getInputByName("op");
        displayNameField.type("Smith");
        HtmlPage resultsPage = submitButton.click();

        List<Player> players = parseResults(resultsPage);

        for (Player player : players) {
            System.out.println(player);
        }
    }

    private static List<Player> parseResults(HtmlPage resultsPage) {
        HtmlTable table = (HtmlTable) resultsPage.getByXPath("//table").get(0);

        return table.getBodies().get(0).getRows().stream()
                .map(r -> {
                    String rating = r.getCell(2).getTextContent();
                    String qRating = r.getCell(3).getTextContent();

                    return new Player(
                            r.getCell(0).getTextContent(),
                            r.getCell(1).getTextContent(),
                            rating.length() == 0 ? null : Integer.parseInt(rating),
                            qRating.length() == 0 ? null : Integer.parseInt(qRating)
                            );

                }).collect(Collectors.toList());
    }
}

