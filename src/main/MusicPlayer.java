package main;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.items.Item;
import main.items.Song;

import java.util.ArrayList;

@Getter
@Setter
//@ToString
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
    private ArrayList<User> queue = new ArrayList<>();

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
//        System.out.println(loadedStatus.getPlayingSince() + loadedStatus.getRemainedTime());
        if (loadedStatus.checkPlayStatus(timestamp)) {
//            System.out.println(loadedStatus.getPlayingSince() + loadedStatus.getRemainedTime() - timestamp + " " + type
//            +  "  " + user.getUsername() + ((Song)loaded).getArtist());
            if (!user.getFreeSong().isEmpty() && user.getFreeSong().get(user.getFreeSong().size() - 1).getDuration() == 0) {
                loadedStatus.setRemainedTime(loadedStatus.getRemainedTime() + 10);
                srcStatus.setRemainedTime(srcStatus.getRemainedTime() + 10);
                if (loadedStatus.checkPlayStatus(timestamp)) {
                    loaded = src.next(this, timestamp);
                } else {
                    user.monetizeUser();
                }
            } else {
                loaded = src.next(this, timestamp);
            }
        } else {
            srcStatus.setOrder(order);
            srcStatus.checkPlayStatus(timestamp);
        }
    }
}
