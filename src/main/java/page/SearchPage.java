package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import data.PriceVO;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class SearchPage {

    private final ElementsCollection rows = $$("tr.cursor-pointer");

    public SearchPage(String url) {
        open(url);
        try {
            $("table.trade-list-table.max-width").shouldBe(Condition.visible);
        } catch (AssertionError error) {
            System.out.println(error.getMessage());
        }
    }

    public List<PriceVO> getPrices() {
        ArrayList<PriceVO> output = new ArrayList<>();
        for (SelenideElement node : rows) {
            String itemName = node.$("td[data-bind=\"using: TradeAsset.Item\"] div").getText();
            String time = node.$("td.bold.hidden-xs").getText();
            double price = Double.parseDouble(node.$("span[data-bind=\"localizedNumber: UnitPrice\"]").getText().replace(",", ".").replace(" ", ""));
            String guild = node.$("div[data-bind=\"text: GuildName\"]").getText();
            String place = node.$("div[data-bind=\"text: StringResource['TraderLocation' + DBData.GuildKioskLocation[GuildKioskLocationID]]\"]").getText();
            output.add(new PriceVO(itemName, price, place, guild, time));
        }
        return output;
    }
}