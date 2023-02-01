package utils;

import data.RecipeVO;
import data.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class RecipeLoader {
    public static void load() {
        List<String> uniqueComponents = new ArrayList<>();
        List<RecipeVO> recipes = new ArrayList<>();
        try (Scanner scanner = new Scanner(Objects.requireNonNull(RecipeLoader.class.getClassLoader().getResourceAsStream("recipes.csv")))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] separated = line.split(",");
                List<String> components = new ArrayList<>();
                for (int i = 1; i < separated.length; i += 1) {
                    String componentName = separated[i];
                    if (!uniqueComponents.contains(componentName)) {
                        uniqueComponents.add(componentName);
                    }
                    components.add(componentName);
                }
                RecipeVO recipe = new RecipeVO(separated[0], components);
                recipes.add(recipe);
            }
        }
        Storage.components = uniqueComponents;
        Storage.recipes = recipes;
    }
}
