package utils;

import com.codeborne.selenide.WebDriverRunner;
import data.ComponentVO;
import data.HardCodeData;
import data.PriceVO;
import data.Storage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import page.SearchPage;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

@Service
public class PricesParser {
    public PricesParser() {
        RecipeLoader.load(RecipeLoader.class.getClassLoader().getResourceAsStream("recipes.csv"));
    }

    public List<PriceVO> getPrices(List<ComponentVO> components, int minCount, int maxPrice) {
        List<PriceVO> output = new ArrayList<>();
        Storage.currentProgressTotal = components.size();
        Storage.currentProgress = 0;
        for (ComponentVO component : components) {
            String url = URLBuilder.buildTTCSearchURL(component.getName(), minCount, maxPrice);
            SearchPage page = new SearchPage(url);
            List<PriceVO> prices = page.getPrices();
            output.addAll(prices);
            sleep(HardCodeData.PARSING_DELAY_MS);
            Storage.currentProgress += 1;
        }
        return output;
    }

    @Async
    public void endlessLoad() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--proxy-server=socks5://127.0.0.1:1081");
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        WebDriverRunner.setWebDriver(driver);
        open(HardCodeData.TTC_URL);
        while (true) {
            Storage.prices = getPrices(Storage.components, HardCodeData.MINIMAL_STACK_SIZE, HardCodeData.MAXIMAL_ITEM_PRICE);
            Storage.lastScanTimestamp = System.currentTimeMillis() / 1000L;
        }
    }
}