package main;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.items.Item;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class MusicPlayer {
    private User user;
    private Item src = null;
    private Item loaded = null;
    private Status srcStatus;
    private Status loadedStatus;
    private int loadedPosition;
    private ArrayList<Integer> order = new ArrayList<Integer>();
    private ArrayList<Item> lastResults = null;
    private String type;
    private User selected;
    private ArrayList<User> lastResultsUsers = null;

    public MusicPlayer(final User user) {
        this.user = user;
        this.srcStatus = new Status();
        this.loadedStatus = new Status();
//        this.lastResults = new ArrayList<>();
        this.selected = null;
    }

    /**
     * for coding style
     */
    public void updateStatus(final boolean paused) {
        srcStatus.setPaused(paused);
        loadedStatus.setPaused(paused);
    }

    /**
     * for coding style
     */
    public void checkStatus(final int timestamp) {
        if (loadedStatus == null) {
            return;
        }
        if (loadedStatus.isPaused()) {
            srcStatus.setPlayingSince(timestamp);
            loadedStatus.setPlayingSince(timestamp);
            return;
        }
        if (loadedStatus.checkPlayStatus(timestamp)) {
            loaded = src.next(this, timestamp);
        } else {
            srcStatus.setOrder(order);
            srcStatus.checkPlayStatus(timestamp);
        }
    }
}
