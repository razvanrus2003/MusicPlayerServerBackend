package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.items.Item;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class Status {

    private String name = "";
    private int remainedTime = 0;
    private String repeat = "No Repeat";
    private boolean shuffle = false;
    private boolean paused;
    private int playingSince;
    private int position = 0;
    private ArrayList<Integer> order = null;

    /**
     * for coding style
     */
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("name", name);
        node.put("remainedTime", remainedTime);
        node.put("repeat", repeat);
        node.put("shuffle", shuffle);
        node.put("paused", paused);
    }

    /**
     * for coding style
     */
    public void clearStatus() {
        name = "";
        remainedTime = 0;
        repeat = "No Repeat";
        shuffle = false;
        paused = true;
        position = 0;
    }

    /**
     * for coding style
     */
    public void setNewItemStatus(final Item item, final int timestamp) {
        name = item.getName();
        repeat = "No Repeat";
        shuffle = false;
        paused = false;
        remainedTime = item.getDuration();
        playingSince = timestamp;
        position = 0;
    }

    /**
     * for coding style
     */
    public boolean checkPlayStatus(final int timestamp) {
        if (remainedTime <= timestamp - playingSince) {
            return true;
        } else if (!paused) {
            remainedTime = remainedTime - timestamp + playingSince;
            playingSince = timestamp;
            return false;
        }
        return false;
    }
}
