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
public abstract class Item {
    protected String name;
    protected int duration = 0;
    protected int listens = 0;

    /**
     * for coding style
     */
    public abstract boolean validate(Filters filters);

    /**
     * for coding style
     */
    public abstract ArrayList<Item> getContent();

    /**
     * for coding style
     */
    public abstract void addToObjectNode(ObjectNode node, ObjectMapper objectMapper);

    /**
     * for coding style
     */
    public void addDuration(final int val) {
        duration += val;
    }

    /**
     * for coding style
     */
    public void subDuration(final int val) {
        duration -= val;
    }

    /**
     * for coding style
     */
    public Item next(final MusicPlayer musicPlayer, final int timestamp) {
        return null;
    }

    /**
     * for coding style
     */
    public Item prev(final Status srcStatus, final Status loadedStatus, final int timestamp) {
        return null;
    }

    public abstract boolean canDelete();

    public void incListens() {
        listens++;
    }
}
