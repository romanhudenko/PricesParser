package utils;

import data.HardCodeData;

public class URLBuilder {
    public static String buildTTCSearchURL(String item, int minCount, int maxPrice) {
        return String.format(HardCodeData.TTC_SEARCH_URL, item, minCount, maxPrice);
    }
}