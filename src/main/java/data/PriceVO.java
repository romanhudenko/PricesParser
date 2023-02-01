package data;

import lombok.Value;

@Value
public class PriceVO {
    String name;
    double price;
    String location;
    String guild;
    String time;
}
