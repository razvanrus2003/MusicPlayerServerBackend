package main.output.WrappedFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@ToString
public class WrappedStat {
    private ArrayList<ArrayList<Map.Entry<String, Integer>>> pairStats = new ArrayList<>();
    private ArrayList<String> fans = null;
    private Integer listeners = null;
}
