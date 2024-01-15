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
/**
 * Song class
 */
public class Song extends Item {
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private String artist;
    private int likes = 0;
    private double revenue = 0;
    private static final int THIRTYONE = 31;

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
    /**
     * equals method
     */
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        Song song = (Song) o;
        return song.getName().equalsIgnoreCase(name)
                && song.getArtist().equals(artist)
                && song.getAlbum().equals(album)
                && song.getGenre().equals(genre);
    }

    @Override
    public final int hashCode() {
        int result = name.hashCode();
        result = THIRTYONE * result + artist.hashCode();
        result = THIRTYONE * result + genre.hashCode();
        result = THIRTYONE * result + album.hashCode();
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
     * validate song
     */
    @Override
    public final boolean validate(final Filters filters) {
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
    public final ArrayList<Item> getContent() {
        return null;
    }

    @Override
    public final void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
    }

    @Override
    public final Item next(final MusicPlayer musicPlayer, final int timestamp) {
        Status srcStatus = musicPlayer.getSrcStatus();
        Status loadedStatus = musicPlayer.getLoadedStatus();
        if (srcStatus.getRepeat().equals("No Repeat")) {
            User user = musicPlayer.getUser();
            if (!user.getFreeSong().isEmpty()
                    && user.getFreeSong().get(user.getFreeSong().size() - 1).getDuration() == 0) {
                musicPlayer.getUser().monetizeUser();
            }
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
    public final Item prev(final Status srcStatus, final Status loadedStatus, final int timestamp) {
        return null;
    }

    @Override
    public final boolean canDelete() {
        Library library = Library.getInstance();

        for (User user : library.getUsers()) {
            if (user.getMusicPlayer().getLoaded() != null
                    && user.getMusicPlayer().getLoaded().equals(this)) {
                return false;
            }
            for (Item playlist : user.getPlaylists()) {
                if (playlist.getContent().contains(this)) {
                    return false;
                }
            }
            if (user.getMusicPlayer().getLastResults() != null
                    && user.getMusicPlayer().getLastResults().contains(this)) {
                return false;
            }
        }
        return true;
    }

}
