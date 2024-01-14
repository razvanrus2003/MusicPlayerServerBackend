package main.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.MusicPlayer;
import main.Status;
import main.User;
import main.commands.playlist.CreatePlaylistCommand;

import javax.swing.plaf.multi.MultiSeparatorUI;
import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class Playlist extends Collection {
    private ArrayList<Item> songs = new ArrayList<Item>();
    private boolean visible;
    private int follewers = 0;

    public Playlist() {
    }

    public Playlist(final CreatePlaylistCommand command) {
        this.name = command.getPlaylistName();
        this.duration = 0;
        this.owner = command.getUsername();
        this.visible = true;
    }

    @Override
    public ArrayList<Item> getContent() {
        return songs;
    }

    @Override
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("name", name);
        ArrayNode subnode = objectMapper.createArrayNode();
        for (Item song : songs) {
            subnode.add(song.getName());
        }
        node.set("songs", subnode);
        if (visible) {
            node.put("visibility", "public");
        } else {
            node.put("visibility", "private");
        }
        node.put("followers", follewers);
    }

    private int getNextSong(final int curr, final ArrayList<Integer> order) {
        for (int i = 0; i < order.size(); i++) {
            if (order.get(i) == curr && i < order.size() - 1) {
                return order.get(i + 1);
            } else if (i == order.size() - 1) {
                return order.size();
            }
        }
        return -1;
    }

    /**
     * for coding style
     */
    @Override
    public Item next(final MusicPlayer musicPlayer, final int timestamp) {
        Status srcStatus = musicPlayer.getSrcStatus();
        Status loadedStatus = musicPlayer.getLoadedStatus();

        if (srcStatus.getRepeat().equals("No Repeat")) {
            if (srcStatus.checkPlayStatus(timestamp)) {
                int pos = loadedStatus.getPosition();
                Item curr = songs.get(pos);
                while (loadedStatus.getRemainedTime() + loadedStatus.getPlayingSince() <= timestamp) {
                    pos = getNextSong(pos, srcStatus.getOrder());
                    if (pos == srcStatus.getOrder().size()) {
                        User user = musicPlayer.getUser();
                        if (!user.getFreeSong().isEmpty()
                                && user.getFreeSong().get(user.getFreeSong().size() - 1).getDuration() == 0) {
                            musicPlayer.getUser().monetizeUser();
                        }
                        loadedStatus.clearStatus();

                        break;
                    }

                    curr = songs.get(pos);
                    musicPlayer.getUser().updateHistory(curr);

                    loadedStatus.setName(curr.getName());
                    loadedStatus.setPlayingSince(loadedStatus.getPlayingSince()
                            + loadedStatus.getRemainedTime());
                    loadedStatus.setRemainedTime(curr.getDuration());
                }

                loadedStatus.clearStatus();
                srcStatus.setRemainedTime(0);
                return null;
            }
            int pos = loadedStatus.getPosition();
            Item curr = songs.get(pos);

            while (loadedStatus.getRemainedTime() + loadedStatus.getPlayingSince() <= timestamp) {
                pos = getNextSong(pos, srcStatus.getOrder());
                if (pos == srcStatus.getOrder().size()) {
                    User user = musicPlayer.getUser();
                    if (!user.getFreeSong().isEmpty()
                            && user.getFreeSong().get(user.getFreeSong().size() - 1).getDuration() == 0) {
                        musicPlayer.getUser().monetizeUser();
                    }
                    loadedStatus.clearStatus();

                    loadedStatus.clearStatus();

                    return null;
                }

                curr = songs.get(pos);
                musicPlayer.getUser().updateHistory(curr);

                loadedStatus.setName(curr.getName());
                loadedStatus.setPlayingSince(loadedStatus.getPlayingSince()
                        + loadedStatus.getRemainedTime());
                loadedStatus.setRemainedTime(curr.getDuration());
            }
            loadedStatus.setRemainedTime(loadedStatus.getPlayingSince()
                    + loadedStatus.getRemainedTime()
                    - timestamp);
            loadedStatus.setPlayingSince(timestamp);
            loadedStatus.setPosition(pos);
            return curr;
        }
        if (srcStatus.getRepeat().equals("Repeat Once")) {
            int pos = loadedStatus.getPosition();
            Item curr = songs.get(pos);

            while (loadedStatus.getRemainedTime() + loadedStatus.getPlayingSince() <= timestamp) {
                pos = getNextSong(pos, srcStatus.getOrder());
                if (pos == srcStatus.getOrder().size()) {
                    pos = srcStatus.getOrder().get(0);
                }
                curr = songs.get(pos);
                musicPlayer.getUser().updateHistory(curr);

                loadedStatus.setName(curr.getName());
                loadedStatus.setPlayingSince(loadedStatus.getPlayingSince()
                        + loadedStatus.getRemainedTime());
                loadedStatus.setRemainedTime(curr.getDuration());
            }
            loadedStatus.setRemainedTime(loadedStatus.getPlayingSince()
                    + loadedStatus.getRemainedTime()
                    - timestamp);
            loadedStatus.setPlayingSince(timestamp);
            loadedStatus.setPosition(pos);
            return curr;
        }

        int pos = loadedStatus.getPosition();
        Item curr = songs.get(pos);

        while (loadedStatus.getRemainedTime() + loadedStatus.getPlayingSince() <= timestamp) {
            loadedStatus.setName(curr.getName());
            loadedStatus.setPlayingSince(loadedStatus.getPlayingSince()
                    + loadedStatus.getRemainedTime());
            loadedStatus.setRemainedTime(curr.getDuration());
            musicPlayer.getUser().updateHistory(curr);
        }
        loadedStatus.setRemainedTime(loadedStatus.getPlayingSince()
                + loadedStatus.getRemainedTime()
                - timestamp);
        loadedStatus.setPlayingSince(timestamp);
        loadedStatus.setPosition(pos);
        return curr;
    }

    /**
     * @param srcStatus
     * @param loadedStatus
     * @param timestamp
     * @return
     */
    @Override
    public Item prev(final Status srcStatus, final Status loadedStatus, final int timestamp) {
        if (loadedStatus.getPosition() == srcStatus.getOrder().get(0)) {
            srcStatus.setRemainedTime(duration);
            srcStatus.setPlayingSince(timestamp);

            loadedStatus.setPaused(false);
            loadedStatus.setRemainedTime(songs.get(srcStatus.getOrder().get(0)).getDuration());
            loadedStatus.setPlayingSince(timestamp);
            return songs.get(0);
        }
        int i = 0;
        while (srcStatus.getOrder().get(i + 1) != loadedStatus.getPosition()) {
            i++;
        }
        Item prev = songs.get(srcStatus.getOrder().get(i));

        srcStatus.setRemainedTime(srcStatus.getRemainedTime()
                - loadedStatus.getRemainedTime()
                + songs.get(loadedStatus.getPosition()).getDuration()
                + prev.getDuration());
        srcStatus.setPlayingSince(timestamp);

        loadedStatus.setPosition(srcStatus.getOrder().get(i));
        loadedStatus.setName(prev.getName());
        loadedStatus.setRemainedTime(prev.getDuration());
        loadedStatus.setPlayingSince(timestamp);
        srcStatus.setPaused(false);
        loadedStatus.setPaused(false);
        return prev;
    }

    /**
     * for coding style
     */
    public void updateTime(final Status srcStatus, final Status loadedStatus) {
        int i = 0;
        while (srcStatus.getOrder().get(i) != loadedStatus.getPosition()) {
            i++;
        }
        int newtime = loadedStatus.getRemainedTime();
        i++;
        while (i < srcStatus.getOrder().size()) {
            newtime += songs.get(srcStatus.getOrder().get(i)).getDuration();
            i++;
        }

        srcStatus.setRemainedTime(newtime);
        srcStatus.setPlayingSince(loadedStatus.getPlayingSince());
    }

    @Override
    public boolean canDelete() {
        Library library = Library.getInstance();

        for (User user : library.getUsers()) {
            if (user.getMusicPlayer().getLastResults() != null && user.getMusicPlayer().getLastResults().contains(this)) {

                return false;
            }
            if (user.getMusicPlayer().getSrc() != null
                    && user.getMusicPlayer().getSrc().getName().equals(name)
                    && user.getMusicPlayer().getLoadedStatus().getRemainedTime() > 0
            ) {

                return false;
            }
        }
        return true;
    }
}
