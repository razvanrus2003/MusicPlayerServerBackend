package main.items;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * abstract class for all items
 */
public class Ad extends Song {
    private int price;
}
