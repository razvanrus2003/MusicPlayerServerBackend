package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.commands.Command;
import main.items.*;

import java.util.*;

@Getter
@Setter
@ToString
public final class Library {
    private static Library library = null;
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<User> users;
    private ArrayList<User> artists;
    private ArrayList<User> hosts;
    private ArrayList<Playlist> playlists;
    private ArrayList<Album> albums;

    private Library() {
    }

    /**
     * for coding style
     */
    public static Library getInstance() {
        return library;
    }

    /**
     * for coding style
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
     * for coding style
     */
    public static User getUser(final String input) {
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
     * for coding style
     */
    public static void reset() {
        library = null;
    }

    public String mostProfitableSong(User artist) {
        return artist.getPlaylists().stream()
                .map(Item::getContent)
                .flatMap(List::stream)
                .map(item -> (Song)item)
                .filter(song -> song.getRevenue() > 0)
                .sorted(Comparator.comparing(Song::getRevenue))
                .map(Item::getName)
                .reduce("", (a , b) -> b);
    }

    public ObjectNode endProgram(ObjectMapper mapper) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "endProgram");

        ObjectNode result = mapper.createObjectNode();

        ArrayList<User> artists = new ArrayList<>();

        for (User user : Library.getInstance().getUsers()) {
            user.cancelPremium();
        }

        for (User artist : Library.getInstance().getArtists()) {
            if (artist.getPlaylists().stream()
                    .map(Item::getContent)
                    .flatMap(List::stream)
                    .map(Item::getListens)
                    .reduce(0, Integer::sum) != 0
                    || artist.getSongRevenue() != 0.0
                    || artist.getMerchRevenue() != 0.0) {
                artists.add(artist);
            }
        }
        Comparator<User> comparator = Comparator.comparing(artist -> artist.getMerchRevenue() + artist.getSongRevenue());
        comparator = comparator.reversed().thenComparing(User::getUsername);

        artists.sort(comparator);
        int  i = 1;
        for (User artist : artists) {
            ObjectNode a = mapper.createObjectNode();
            String mps = mostProfitableSong(artist);
            if (Objects.equals(mps, ""))
                mps = "N/A";
            double d = 0;
            a.put("merchRevenue", (double) Math.round(artist.getMerchRevenue() * 100) / 100);
            a.put("songRevenue", (double) Math.round(artist.getSongRevenue() * 100) /100);
            a.put("ranking", i);
            a.put("mostProfitableSong", mps);
            i++;
            result.put(artist.getUsername(), a);
        }

        output.put("result", result);
        return  output;
    }
}
