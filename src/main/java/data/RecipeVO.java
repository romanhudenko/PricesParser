package data;

import lombok.Value;

import java.util.List;

@Value
public class RecipeVO {
    public String name;
    public List<ComponentVO> components;
}
