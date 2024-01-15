package main.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import main.MusicPlayer;
import main.Status;
import main.User;
import main.commands.artist.AddAlbumCommand;

import java.util.ArrayList;

@Getter
@Setter
public final class Album extends Collection {

    private int releaseYear;
    private String description;
    private ArrayList<Item> songs;

    public Album(final AddAlbumCommand command) {
        this.name = command.getName();
        this.description = command.getDescription();
        this.releaseYear = command.getReleaseYear();
        this.owner = command.getUsername();
        this.duration = 0;
        ArrayList<Song> newSongs = command.getSongs();
        this.songs = new ArrayList<>();
        for (Song song : newSongs) {
            this.duration += song.getDuration();
            this.songs.add(song);
        }
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
    }

    /**
     * @return the number of times the album was played
     */
    @Override
    public boolean canDelete() {
        Library library = Library.getInstance();

        for (Item song : songs) {
            if (!song.canDelete()) {
                return false;
            }
        }

        for (User user : library.getUsers()) {
            if (user.getMusicPlayer().getLastResults() != null
                    && user.getMusicPlayer().getLastResults().contains(this)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the number of likes of the album
     */
    public int getLikes() {
        int s = 0;
        for (Item song : songs) {
            s += ((Song) song).getLikes();
        }
        return s;
    }

    @Override
    public Item next(final MusicPlayer musicPlayer, final int timestamp) {
        return null;
    }

    @Override
    public Item prev(final Status srcStatus, final Status loadedStatus, final int timestamp) {
        return null;
    }
}
