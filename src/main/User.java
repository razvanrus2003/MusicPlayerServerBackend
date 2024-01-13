package main;

import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.artist.Event;
import main.artist.Merch;
import main.commands.admin.AddUserCommand;
import main.filters.Filters;
import main.host.Post;
import main.items.Album;
import main.items.Episode;
import main.items.Item;
import main.items.Song;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Getter
@Setter
@ToString
public final class User {
    private String username;
    private int age;
    private String city;
    private MusicPlayer musicPlayer = null;
    private ArrayList<Item> likedSongs = new ArrayList<Item>();
    private ArrayList<Item> playlists = new ArrayList<Item>();
    private ArrayList<Item> followedPlayLists = new ArrayList<Item>();
    private HashMap<String, Integer> playedPodcasts = new HashMap<String, Integer>();
    private String page;
    private String status;
    private String type;
    private ArrayList<Event> events;
    private ArrayList<Merch> merches;
    private ArrayList<Post> posts;
    private HashMap<Song, Integer> songHistory = new HashMap<>();
    private HashMap<Episode, Integer> episodeHistory = new HashMap<>();
    private HashMap<String, Integer> artistHistory = new HashMap<>();


    public User(final UserInput user) {
        this.username = user.getUsername();
        this.age = user.getAge();
        this.city = user.getCity();
        this.musicPlayer = new MusicPlayer(this);
        this.status = new String("online");
        this.type = "user";
        this.page = "Home";
    }

    public User(AddUserCommand command) {
        this.username = command.getUsername();
        this.age = command.getAge();
        this.city = command.getCity();
        this.musicPlayer = new MusicPlayer(this);
        this.status = new String("online");
        this.type = command.getType();
        this.page = "Home";
    }

    /**
     * for coding style
     */
    public static ArrayList<User> getUsers(final ArrayList<UserInput> input) {
        ArrayList<User> users = new ArrayList<User>();
        for (UserInput user : input) {
            users.add(new User(user));
        }
        return users;
    }

    /**
     * for coding style
     */
    public void clearPlayer() {
        if (musicPlayer != null) {
            musicPlayer.getSrcStatus().clearStatus();
            musicPlayer.setSrc(null);
            musicPlayer.getLoadedStatus().clearStatus();
            musicPlayer.setLoaded(null);
        }
    }

    /**
     * for coding style
     */
    public void saveTime(final int timestamp) {
        if (musicPlayer == null
                || musicPlayer.getSrc() == null
                || musicPlayer.getLoaded() == null) {
            return;
        }
        if (musicPlayer.getType().equals("podcasts")) {
            musicPlayer.checkStatus(timestamp);
            if (musicPlayer.getLoaded() == null) {
                return;
            }
            playedPodcasts.put(musicPlayer.getSrc().getName(),
                    musicPlayer.getSrcStatus().getRemainedTime());
        }
    }

    public boolean validate(final Filters filters) {
        if (filters.getName() != null && !username.startsWith(filters.getName())) {
            return false;
        }

        return true;
    }

    public boolean isOnline() {
        if (status.equals("online")) {
            return true;
        }
        return false;
    }

    public int getLikes() {
        int s = 0;
        if (type.equals("artist")) {
            for (Item album : playlists) {
                s += ((Album) album).getLikes();
            }
        }
        return s;
    }

    public void updateHistory(Item song) {
        if (songHistory.containsKey(song)) {
            songHistory.put((Song)song, songHistory.get(song) + 1);
        } else {
            songHistory.put((Song)song, 1);
        }

        String artist =((Song) song).getArtist();
        if (artistHistory.containsKey(artist)) {
            artistHistory.put(artist, artistHistory.get(artist) + 1);
        } else {
            artistHistory.put(artist, 1);
        }

        Item album = Library.getInstance().getUser(((Song) song).getArtist())
                .getPlaylists()
                .stream()
                .filter(a -> a.getName().equals(((Song) song).getAlbum()))
                .findFirst().get();
        album.incListens();

        if (!musicPlayer.getType().equals("albums") && !musicPlayer.getType().equals("songs"))
            musicPlayer.getSrc().incListens();
        song.incListens();

    }

    public void updateHistory(Item song, int listens) {
        while (listens != 0) {
            updateHistory(song);
            listens--;
        }
    }

}
