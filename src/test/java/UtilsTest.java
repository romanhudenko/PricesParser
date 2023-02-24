import com.codeborne.selenide.WebDriverRunner;
import data.HardCodeData;
import data.PriceVO;
import data.Storage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import page.SearchPage;
import utils.RecipeLoader;
import utils.URLBuilder;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

public class UtilsTest {

    @Test
    public void recipesLoading() {
        RecipeLoader.load(RecipeLoader.class.getClassLoader().getResourceAsStream("recipes.csv"));
        Assertions.assertEquals(4, Storage.components.size());
        Assertions.assertEquals(2, Storage.recipes.size());
    }

    @Test
    public void urlBuilding() {
        String expected = "https://eu.tamrieltradecentre.com/pc/Trade/SearchResult?ItemNamePattern=1&AmountMin=1&PriceMax=1";
        Assertions.assertEquals(expected, URLBuilder.buildTTCSearchURL("1", 1, 1));
    }

    @Test
    public void pageParsing() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--proxy-server=socks5://127.0.0.1:1081");
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        WebDriverRunner.setWebDriver(driver);
        open(HardCodeData.TTC_URL);
        ClassLoader classLoader = SearchPage.class.getClassLoader();
        String filePath = "file://" + Objects.requireNonNull(classLoader.getResource("search_page.html")).getPath();
        SearchPage page = new SearchPage(filePath);
        List<PriceVO> prices = page.getPrices();
        Assertions.assertEquals(10, prices.size());
    }
}
