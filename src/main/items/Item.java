package main.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.MusicPlayer;
import main.Status;
import main.filters.Filters;

import java.util.ArrayList;

@Getter
@Setter
@ToString
/**
 * abstract class for all items
 */
public abstract class Item {
    protected String name;
    protected int duration = 0;
    protected int listens = 0;

    /**
     * @return true if item is valid
     */
    public abstract boolean validate(Filters filters);

    /**
     * @return content of the item
     */
    public abstract ArrayList<Item> getContent();

    /**
     * add item to json
     */
    public abstract void addToObjectNode(ObjectNode node, ObjectMapper objectMapper);

    /**
     * @return next item in queue if exists or null
     */
    public abstract Item next(MusicPlayer musicPlayer, int timestamp);

    /**
     * @return previous item in queue if exists or null
     */
    public abstract Item prev(Status srcStatus, Status loadedStatus, int timestamp);

    /**
     * @return true if item can be deleted
     */
    public abstract boolean canDelete();

    /**
     * increment listens
     */
    public void incListens() {
        listens++;
    }

    /**
     * increment duration
     */
    public void addDuration(final int val) {
        duration += val;
    }

    /**
     * decrement duration
     */
    public void subDuration(final int val) {
        duration -= val;
    }
}
