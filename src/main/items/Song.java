package main.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.MusicPlayer;
import main.Status;
import main.User;
import main.filters.Filters;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class Song extends Item {
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private String artist;
    private int likes = 0;
    private double revenue = 0;

    public Song() {
    }

    public Song(final SongInput song) {
        this.name = song.getName();
        this.duration = song.getDuration();
        this.album = song.getAlbum();
        this.tags = song.getTags();
        this.genre = song.getGenre();
        this.lyrics = song.getLyrics();
        this.releaseYear = song.getReleaseYear();
        this.artist = song.getArtist();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song song)) return false;

        if (!getName().equalsIgnoreCase(song.getName())) return false;
        return getArtist().equals(song.getArtist());
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + artist.hashCode();
        result = 31 * result + genre.hashCode();
        result = 31 * result +  album.hashCode();
        return result;
    }

    /**
     * Convert the input object to an object that the program can work with.
     *
     * @param input input object
     */
    public static ArrayList<Song> getSongs(final ArrayList<SongInput> input) {
        ArrayList<Song> songs = new ArrayList<Song>();
        for (SongInput song : input) {
            songs.add(new Song(song));
        }
        return songs;
    }

    /**
     * for coding style
     */
    public boolean validate(final Filters filters) {
        if (filters.getName() != null
                && !name.toLowerCase().startsWith(filters.getName().toLowerCase())) {
            return false;
        }
        if (filters.getAlbum() != null && !album.equals(filters.getAlbum())) {
            return false;
        }
        if (filters.getArtist() != null && !artist.equals(filters.getArtist())) {
            return false;
        }
        if (filters.getGenre() != null && !filters.getGenre().equalsIgnoreCase(genre)) {
            return false;
        }
        if (filters.getLyrics() != null
                && !lyrics.toLowerCase().contains(filters.getLyrics().toLowerCase())) {
            return false;
        }
        String year = filters.getReleaseYear();
        if (year != null && year.startsWith("<")
                && Integer.parseInt(year.substring(1)) < releaseYear) {
            return false;
        }
        if (year != null && year.startsWith(">")
                && Integer.parseInt(year.substring(1)) > releaseYear) {
            return false;
        }
        if (filters.getTags() != null) {
            for (String tag : filters.getTags()) {
                if (!tags.contains(tag)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ArrayList<Item> getContent() {
        return null;
    }

    @Override
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
    }

    @Override
    public Item next(final MusicPlayer musicPlayer, final int timestamp) {
        Status srcStatus = musicPlayer.getSrcStatus();
        Status loadedStatus = musicPlayer.getLoadedStatus();
        if (srcStatus.getRepeat().equals("No Repeat")) {
            loadedStatus.clearStatus();
            return null;
        }
        if (srcStatus.getRepeat().equals("Repeat Once")) {
            loadedStatus.setPlayingSince(loadedStatus.getPlayingSince()
                    + loadedStatus.getRemainedTime());
            loadedStatus.setRemainedTime(duration
                    + loadedStatus.getPlayingSince()
                    - timestamp);
            loadedStatus.setPlayingSince(timestamp);

            loadedStatus.setRepeat("No Repeat");
            srcStatus.setRepeat("No Repeat");

            if (loadedStatus.getRemainedTime() <= 0) {
                loadedStatus.clearStatus();
                return null;
            }
            musicPlayer.getUser().updateHistory(this);
            return this;
        }
        musicPlayer.getUser().updateHistory(this, (loadedStatus.getRemainedTime()
                - timestamp
                + loadedStatus.getPlayingSince()) / duration);
        loadedStatus.setRemainedTime(duration
                + (loadedStatus.getRemainedTime()
                - timestamp
                + loadedStatus.getPlayingSince()) % duration);
        loadedStatus.setPlayingSince(timestamp);
        return this;
    }

    @Override
    public boolean canDelete() {
        Library library = Library.getInstance();

        for (User user : library.getUsers()) {
            if (user.getMusicPlayer().getLoaded() != null && user.getMusicPlayer().getLoaded().equals(this)) {
                return false;
            }
            for (Item playlist : user.getPlaylists()) {
                if (playlist.getContent().contains(this)) {
                    return false;
                }
            }
            if (user.getMusicPlayer().getLastResults() != null && user.getMusicPlayer().getLastResults().contains(this)) {
                return false;
            }
        }
        return true;
    }

}
