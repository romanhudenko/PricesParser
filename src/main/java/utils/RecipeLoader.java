package utils;

import data.ComponentVO;
import data.RecipeVO;
import data.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class RecipeLoader {
    public static void load() {
        List<ComponentVO> uniqueComponents = new ArrayList<>();
        List<RecipeVO> recipes = new ArrayList<>();
        try (Scanner scanner = new Scanner(Objects.requireNonNull(RecipeLoader.class.getClassLoader().getResourceAsStream("recipes.csv")))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] separated = line.split(",");
                List<ComponentVO> components = new ArrayList<>();
                for (int i = 1; i < separated.length; i += 2) {
                    String componentName = separated[i];
                    String componentRussianName = separated[i + 1];
                    ComponentVO component = new ComponentVO(componentName, componentRussianName);
                    boolean flag = true;
                    for (int j = 0; j < uniqueComponents.size(); j += 1) {
                        if (uniqueComponents.get(j).getName().equals(componentName)) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        uniqueComponents.add(component);
                    }
                    components.add(component);
                }
                RecipeVO recipe = new RecipeVO(separated[0], components);
                recipes.add(recipe);
            }
        }
        Storage.components = uniqueComponents;
        Storage.recipes = recipes;
    }
}
