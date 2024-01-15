package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.items.Album;
import main.items.Item;
import main.items.Playlist;
import main.items.Podcast;
import main.items.Song;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
/** Library class
 * Singleton class that contains all the data of the program
 */
public final class Library {
    private static Library library = null;
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<User> users;
    private ArrayList<User> artists;
    private ArrayList<User> hosts;
    private ArrayList<Playlist> playlists;
    private ArrayList<Album> albums;
    private static final int HUNDRED = 100;

    private Library() { }

    /**
     * returns the instance of the library
     */
    public static Library getInstance() {
        return library;
    }

    /**
     * returns the instance of the library
     */
    public static Library getInstance(final LibraryInput input) {
        if (library == null) {
            library = new Library();
            library.songs = Song.getSongs(input.getSongs());
            library.podcasts = Podcast.getPodcasts(input.getPodcasts());
            library.users = User.getUsers(input.getUsers());
            library.playlists = new ArrayList<Playlist>();
            library.albums = new ArrayList<Album>();
            library.artists = new ArrayList<>();
            library.hosts = new ArrayList<>();
        }
        return library;
    }

    /**
     * returns the user with the given username
     */
    public User getUser(final String input) {
        if (library == null) {
            return null;
        }
        for (User user : library.users) {
            if (Objects.equals(user.getUsername(), input)) {
                return user;
            }
        }
        for (User user : library.artists) {
            if (Objects.equals(user.getUsername(), input)) {
                return user;
            }
        }
        for (User user : library.hosts) {
            if (Objects.equals(user.getUsername(), input)) {
                return user;
            }
        }
        return null;
    }

    /**
     * resets the library
     */
    public static void reset() {
        library = null;
    }

    /**
     * returns the most profitable song of an artist
     */
    public String mostProfitableSong(final User artist) {
        Comparator<Map.Entry<String, Double>> comparator =
                Comparator.comparing(Map.Entry::getValue);
        comparator = comparator.reversed().thenComparing(Map.Entry.comparingByKey()).reversed();

        return artist.getPlaylists().stream()
                .map(Item::getContent)
                .flatMap(List::stream)
                .map(item -> (Song) item)
                .filter(song -> song.getRevenue() > 0)
                .collect(Collectors.groupingBy(
                        Item::getName,
                        Collectors.mapping(Song::getRevenue, Collectors.toList())))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        r -> r.getValue().stream().reduce((double) 0, Double::sum)))
                .entrySet()
                .stream()
                .sorted(comparator)
                .map(Map.Entry::getKey)
                .reduce("", (a, b) -> b);
    }

    /**
     * returns the output for the program end"
     */
    public ObjectNode endProgram(final ObjectMapper mapper, final int timestamp) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "endProgram");
        ObjectNode result = mapper.createObjectNode();
        ArrayList<User> topArtists = new ArrayList<>();

        // update the status of all users and cancel their premium
        for (User user : Library.getInstance().getUsers()) {
            if (user.getMusicPlayer() != null
                    && user.getMusicPlayer().getLoaded() != null
                    && user.isOnline()) {
                user.getMusicPlayer().checkStatus(timestamp);
            }
            user.cancelPremium();
        }

        // generate the artists ranking
        for (User artist : Library.getInstance().getArtists()) {
            if (artist.getPlaylists().stream()
                    .map(Item::getContent)
                    .flatMap(List::stream)
                    .map(Item::getListens)
                    .reduce(0, Integer::sum) != 0
                    || artist.getSongRevenue() != 0.0
                    || artist.getMerchRevenue() != 0.0) {
                topArtists.add(artist);
            }
        }
        Comparator<User> comparator = Comparator.comparing(artist -> artist.getMerchRevenue()
                + artist.getSongRevenue());
        comparator = comparator.reversed().thenComparing(User::getUsername);
        topArtists.sort(comparator);

        // generate the output
        int i = 1;
        for (User artist : topArtists) {
            ObjectNode a = mapper.createObjectNode();

            String mps = mostProfitableSong(artist);

            if (Objects.equals(mps, "")) {
                mps = "N/A";
            }
            double d = 0;
            a.put(
                    "merchRevenue",
                    (double) Math.round(artist.getMerchRevenue() * HUNDRED) / HUNDRED);
            a.put(
                    "songRevenue",
                    (double) Math.round(artist.getSongRevenue() * HUNDRED) / HUNDRED);
            a.put("ranking", i);
            a.put("mostProfitableSong", mps);
            i++;
            result.put(artist.getUsername(), a);
        }

        output.put("result", result);
        return output;
    }
}
