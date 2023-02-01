package data;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    public static List<RecipeVO> recipes = new ArrayList<>();
    public static List<PriceVO> prices = new ArrayList<>();
    public static List<ComponentVO> components = new ArrayList<>();
    public static long lastScanTimestamp = 0;
    public static int currentProgressTotal = 0;
    public static int currentProgress = 0;
}
