package main.output.Formats;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@ToString
/**
 * WrappedStat class
 * Contains the stats of a user
 */
public class WrappedStat {
    private ArrayList<ArrayList<Map.Entry<String, Integer>>> pairStats = new ArrayList<>();
    private ArrayList<String> fans = null;
    private Integer listeners = null;
}
