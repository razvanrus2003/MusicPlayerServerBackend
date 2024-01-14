package main.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.filters.Filters;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Ad extends Song {
    private int price;
}
