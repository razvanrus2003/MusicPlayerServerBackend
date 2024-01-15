package main.commands;

import lombok.Getter;
import lombok.Setter;
import main.Library;
import main.User;
import main.filters.Filters;
import main.items.Item;
import main.items.Playlist;
import main.items.Podcast;
import main.items.Song;
import main.output.CommandOutput;
import main.output.SearchOutput;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class SearchCommand extends Command {
    private static final int FIVE = 5;
    private String type;
    private Filters filters;

    public SearchCommand() {
    }

    @Override
    public CommandOutput execute() {
        User user = Library.getInstance().getUser(username);
        SearchOutput output = new SearchOutput(this);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.isOnline()) {
            output.setMessage(username + " is offline.");
            return output;
        }
//        if (!user.getFreeSong().isEmpty()) {
//            for (Song song1 : user.getFreeSong()) {
//                System.out.print(song1.getDuration() + " ");
//            }
//            System.out.println();
//        }

        if (user.getMusicPlayer().getSrc() != null) {
            if (user.getMusicPlayer().getLoadedStatus().getPlayingSince()
                    + user.getMusicPlayer().getLoadedStatus().getRemainedTime() - timestamp != 0) {
                user.getMusicPlayer().checkStatus(timestamp);
            }
        }
        user.saveTime(timestamp);
        user.clearPlayer();
        if (type.equals("artist")) {
            ArrayList<User> users = Library.getInstance().getArtists();

            ArrayList<User> results;

            results = new ArrayList<>();
            for (User user1 : users) {

                if (user1.getType().equals(type) && user1.validate(filters)) {
                    results.add(user1);
                    output.getResults().add(user1.getUsername());
                }
                if (results.size() == FIVE) {
                    break;
                }
            }
            user.getMusicPlayer().setLastResultsUsers(results);
            output.setMessage("Search returned " + results.size() + " results");
            return output;
        }

        if (type.equals("host")) {
            ArrayList<User> users = Library.getInstance().getHosts();

            ArrayList<User> results;

            results = new ArrayList<>();
            for (User user1 : users) {

                if (user1.getType().equals(type) && user1.validate(filters)) {
                    results.add(user1);
                    output.getResults().add(user1.getUsername());
                }
                if (results.size() == FIVE) {
                    break;
                }
            }
            user.getMusicPlayer().setLastResultsUsers(results);
            output.setMessage("Search returned " + results.size() + " results");
            return output;
        }
        user.getMusicPlayer().setLastResultsUsers(null);

        if (user.getMusicPlayer().getLastResults() == null) {
            user.getMusicPlayer().setLastResults(new ArrayList<Item>());
        }
        ArrayList<Item> results = user.getMusicPlayer().getLastResults();
        results.clear();


        switch (type) {
            case "song":
                user.getMusicPlayer().setType("songs");
                ArrayList<Song> songs = Library.getInstance().getSongs();
                for (Song song : songs) {
                    if (song.validate(filters)) {
                        results.add(song);
                        output.getResults().add(song.getName());
                    }
                    if (results.size() == FIVE) {
                        break;
                    }
                }
                break;
            case "podcast":
                user.getMusicPlayer().setType("podcasts");
                ArrayList<Podcast> podcasts = Library.getInstance().getPodcasts();
                for (Podcast podcast : podcasts) {
                    if (podcast.validate(filters)) {
                        results.add(podcast);
                        output.getResults().add(podcast.getName());
                    }
                    if (results.size() == FIVE) {
                        break;
                    }
                }
                break;
            case "playlist":
                user.getMusicPlayer().setType("playlists");
                ArrayList<Playlist> playlists = Library.getInstance().getPlaylists();
                for (Playlist playlist : playlists) {
                    if ((playlist.isVisible()
                            || playlist.getOwner().equals(username))
                            && playlist.validate(filters)) {
                        results.add(playlist);
                        output.getResults().add(playlist.getName());
                    }
                    if (results.size() == FIVE) {
                        break;
                    }
                }
                break;
            case "album":

                user.getMusicPlayer().setType("albums");
                ArrayList<Item> albums = new ArrayList<>(Library.getInstance().getArtists().stream()
                        .map(User::getPlaylists)
                        .flatMap(List::stream).toList());
                for (Item album : albums) {
                    if (album.validate(filters)) {
                        results.add(album);
                        output.getResults().add(album.getName());
                    }
                    if (results.size() == FIVE) {
                        break;
                    }
                }
                break;
            default:
                break;
        }

        output.setMessage("Search returned " + results.size() + " results");
        return output;
    }
}
