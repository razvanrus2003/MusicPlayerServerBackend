package main;

import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;
import main.artist.Event;
import main.artist.Merch;
import main.commands.admin.AddUserCommand;
import main.filters.Filters;
import main.host.Post;
import main.items.Ad;
import main.items.Album;
import main.items.Episode;
import main.items.Item;
import main.items.Song;
import main.observer.NewsObserver;
import main.observer.Observer;
import main.output.Formats.Notification;
import main.pageControl.PageCommand;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
/**
 * User class
 * This class contains all the information about a user, artist or host.
 */
public final class User {
    private String username;
    private int age;
    private String city;
    private MusicPlayer musicPlayer = null;
    private ArrayList<Item> likedSongs = new ArrayList<Item>();
    private ArrayList<Item> playlists = new ArrayList<Item>();
    private ArrayList<Item> followedPlayLists = new ArrayList<Item>();
    private HashMap<String, Integer> playedPodcasts = new HashMap<String, Integer>();
    private String page = null;
    private User pageOwner = null;
    private String status;
    private String type;
    private ArrayList<Event> events;
    private ArrayList<Merch> merches = new ArrayList<>();
    private ArrayList<Post> posts;
    private HashMap<Song, Integer> songHistory = new HashMap<>();
    private HashMap<Episode, Integer> episodeHistory = new HashMap<>();
    private HashMap<String, Integer> artistHistory = new HashMap<>();
    private double merchRevenue = 0;
    private double songRevenue = 0;
    private boolean premium = false;
    private ArrayList<Song> premiumSong = new ArrayList<>();
    private ArrayList<Song> freeSong = new ArrayList<>();
    private ArrayList<Notification> notifications = new ArrayList<>();
    private ArrayList<Observer> subscribers = new ArrayList<>();
    private NewsObserver newsObserver = new NewsObserver(this);
    private ArrayList<PageCommand> nextPagesCommand = new ArrayList<>();
    private ArrayList<PageCommand> prevPagesCommand = new ArrayList<>();
    private ArrayList<Item> recommendation = new ArrayList<>();
    private ArrayList<String> recommendationType = new ArrayList<>();
    private static final int SUBSCRIPTION_COST = 1000000;


    public User(final UserInput user) {
        this.username = user.getUsername();
        this.age = user.getAge();
        this.city = user.getCity();
        this.musicPlayer = new MusicPlayer(this);
        this.status = "online";
        this.type = "user";
        this.page = "Home";
    }

    public User(final AddUserCommand command) {
        this.username = command.getUsername();
        this.age = command.getAge();
        this.city = command.getCity();
        this.musicPlayer = new MusicPlayer(this);
        this.status = "online";
        this.type = command.getType();
        this.page = "Home";
    }

    /**
     * get the users from the input
     */
    public static ArrayList<User> getUsers(final ArrayList<UserInput> input) {
        ArrayList<User> users = new ArrayList<User>();
        for (UserInput user : input) {
            users.add(new User(user));
        }
        return users;
    }

    /**
     * clear all data stored in the player
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
     * save the time of the podcast
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

    /**
     * return if the user respects the filters
     */
    public boolean validate(final Filters filters) {
        return filters.getName() == null || username.startsWith(filters.getName());
    }

    /**
     * return if the user respects the filters
     */
    public boolean isOnline() {
        return status.equals("online");
    }

    /**
     * return the number of likes of the artist
     */
    public int getLikes() {
        int s = 0;
        if (type.equals("artist")) {
            for (Item album : playlists) {
                s += ((Album) album).getLikes();
            }
        }
        return s;
    }

    /**
     * monetize artists for the free songs listened by the user
     * monetization will remove all songs from the free queue
     * monetization is called if there is an ad in queue when trying to play a new song
     */
    public void monetizeUser() {
        int price = ((Ad) freeSong.remove(freeSong.size() - 1)).getPrice();
        double revenue = (double) price / freeSong.size();
        for (Song song : freeSong) {

            User artist = Library.getInstance().getUser(song.getArtist());
            if (artist != null) {
                artist.setSongRevenue(artist.getSongRevenue() + revenue);
            }
            song.setRevenue(song.getRevenue() + revenue);
        }
        freeSong.clear();

    }

    /**
     * update the history of the user and call for a monetization if needed
     * this function is called when a new song is played
     * monetization is called if there is an ad in queue when trying to play a new song
     */
    public void updateHistory(final Item song) {
        if (songHistory.containsKey(song)) {
            songHistory.put((Song) song, songHistory.get(song) + 1);
        } else {
            songHistory.put((Song) song, 1);
        }

        String artist = ((Song) song).getArtist();
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

        if (!musicPlayer.getType().equals("albums") && !musicPlayer.getType().equals("songs")) {
            musicPlayer.getSrc().incListens();
        }
        song.incListens();

        if (premium) {
            premiumSong.add((Song) song);
        } else {
            if (!freeSong.isEmpty() && freeSong.get(freeSong.size() - 1).getDuration() == 0) {
                monetizeUser();
            }
            freeSong.add((Song) song);
        }
    }

    /**
     * update the history of the user
     * this function is called when a new episode is played
     */
    public void updateHistoryEpisodes(final Item episode) {
        if (episodeHistory.containsKey(episode)) {
            episodeHistory.put((Episode) episode, episodeHistory.get(episode) + 1);
        } else {
            episodeHistory.put((Episode) episode, 1);
        }

        episode.incListens();
    }

    /**
     * update the history of the user
     * this function is called when a song has been played multiple times
     */
    public void updateHistory(final Item song, final int listens) {
        for (int i = 0; i < listens; i++) {
            updateHistory(song);
        }
    }

    /**
     * cancel the premium subscription and generate revenue for the artists
     */
    public void cancelPremium() {
        if (premium) {
            double revenue = (double) SUBSCRIPTION_COST / premiumSong.size();
            for (Song song : premiumSong) {

                User artist = Library.getInstance().getUser(song.getArtist());
                if (artist != null) {
                    artist.setSongRevenue(artist.getSongRevenue() + revenue);
                }
                song.setRevenue(song.getRevenue() + revenue);
            }
            premiumSong.clear();
            premium = false;
        }
    }

    /**
     * add a new notification to the user by notifying all the observers
     */
    public void notifyObservers(final String news) {
        for (Observer observer : subscribers) {
            observer.update(news, this);
        }
    }

    /**
     * this function acts as the invoker for the page commands
     * execute the next page command
     * also acts as a redo function because of the 2 array lists used as stacks
     */
    public void nextPage() {
        if (nextPagesCommand.isEmpty()) {
            return;
        }
        PageCommand command = nextPagesCommand.remove(nextPagesCommand.size() - 1);

        prevPagesCommand.add(command);
        command.execute();
    }

    /**
     * this function acts as the invoker for the page commands
     * undo the last page command
     */
    public void prevPage() {
        if (prevPagesCommand.isEmpty()) {
            return;
        }
        PageCommand command = prevPagesCommand.remove(prevPagesCommand.size() - 1);

        nextPagesCommand.add(command);
        command.undo();
    }
}
