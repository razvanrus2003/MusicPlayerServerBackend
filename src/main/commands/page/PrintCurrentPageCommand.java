package main.commands.page;

import main.Library;
import main.User;
import main.commands.Command;
import main.items.Episode;
import main.items.Item;
import main.items.Playlist;
import main.items.Song;
import main.output.CommandOutput;
import main.output.PrintCurrentPageOutput;

import java.util.ArrayList;

import static java.lang.Math.min;

public final class PrintCurrentPageCommand extends Command {
    private static final int FIVE = 5;

    @Override
    public CommandOutput execute() {
        PrintCurrentPageOutput output = new PrintCurrentPageOutput(this);

        User user = Library.getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.isOnline()) {
            output.setMessage(username + " is offline.");
            return output;
        }

        String message = new String();
        if (user.getPage().equals("Home")) {
            message = new String("Liked songs:\n\t[");
            ArrayList<Song> top5 = new ArrayList<Song>();

            ArrayList<Item> songs = user.getLikedSongs();
            for (Item song : songs) {
                if (top5.isEmpty()) {
                    top5.add(0, (Song) song);
                } else {
                    int i;
                    for (i = 0; i < min(FIVE, top5.size()); i++) {
                        if (((Song) song).getLikes() > top5.get(i).getLikes()) {
                            top5.add(i, (Song) song);
                            i = FIVE;
                        }
                    }
                    if (i != FIVE + 1) {
                        top5.add(min(FIVE, top5.size()), (Song) song);
                    }
                }

            }

            for (int i = 0; i < min(FIVE, user.getLikedSongs().size()); i++) {
                if (i > 0) {
                    message = message.concat(", ");
                }
                message = message.concat(top5.get(i).getName());
            }
            message = message.concat("]\n\nFollowed playlists:\n\t[");
            for (int i = 0; i < FIVE && i < user.getFollowedPlayLists().size(); i++) {
                if (i > 0) {
                    message = message.concat(",");
                }
                message = message.concat(user.getFollowedPlayLists().get(i).getName());
            }
            message = message.concat("]\n\nSong recommendations:\n\t[");
            int j = 0;
            for (int i = 0; j < FIVE && i < user.getRecommendation().size(); i++) {
                if (user.getRecommendationType().get(i).equals("song")) {
                    if (i > 0) {
                        message = message.concat(",");
                    }
                    j++;
                    message = message.concat(user.getRecommendation().get(i).getName());
                }
            }
            message = message.concat("]\n\nPlaylists recommendations:\n\t[");
            j = 0;
            for (int i = 0; j < FIVE && i < user.getRecommendation().size(); i++) {
                if (user.getRecommendationType().get(i).equals("playlist")) {
                    if (j > 0) {
                        message = message.concat(",");
                    }
                    j++;
                    message = message.concat(user.getRecommendation().get(i).getName());
                }
            }
            message = message.concat("]");
        } else if (user.getPage().equals("Artist")) {
            User artist = user.getPageOwner();;
            message = new String("Albums:\n\t[");
            for (int i = 0; i < artist.getPlaylists().size(); i++) {
                if (i > 0) {
                    message = message.concat(", ");
                }
                message = message.concat(artist.getPlaylists().get(i).getName());
            }
            message = message.concat("]\n\nMerch:\n\t[");
            for (int i = 0; i < artist.getMerches().size(); i++) {
                if (i > 0) {
                    message = message.concat(", ");
                }
                message = message.concat(artist.getMerches().get(i).getName()
                        + " - "
                        + artist.getMerches().get(i).getPrice()
                        + ":\n\t"
                        + artist.getMerches().get(i).getDescription());
            }
            message = message.concat("]\n\nEvents:\n\t[");
            for (int i = 0; i < artist.getEvents().size(); i++) {
                if (i > 0) {
                    message = message.concat(", ");
                }
                message = message.concat(artist.getEvents().get(i).getName()
                        + " - "
                        + artist.getEvents().get(i).getDate()
                        + ":\n\t"
                        + artist.getEvents().get(i).getDescription());
            }
            message = message.concat("]");
        } else if (user.getPage().equals("Host")) {
            User artist = user.getPageOwner();
            message = new String("Podcasts:\n\t[");
            for (int i = 0; i < artist.getPlaylists().size(); i++) {
                if (i > 0) {
                    message = message.concat("]\n, ");
                }
                message = message.concat(artist.getPlaylists().get(i).getName() + ":\n\t[");
                for (int j = 0; j < artist.getPlaylists().get(i).getContent().size(); j++) {
                    if (j > 0) {
                        message = message.concat(", ");
                    }
                    message = message.concat(artist.getPlaylists().get(i).getContent().get(j).getName() + " - " + ((Episode) artist.getPlaylists().get(i).getContent().get(j)).getDescription());
                }

            }
            message = message.concat("]\n]\n\nAnnouncements:\n\t[");
            for (int i = 0; i < artist.getPosts().size(); i++) {
                if (i > 0) {
                    message = message.concat(", ");
                }
                message = message.concat(artist.getPosts().get(i).getName()
                        + ":\n\t"
                        + artist.getPosts().get(i).getDescription());
            }
            message = message.concat("]");
        } else if (user.getPage().equals("LikedContent")) {

            message = new String("Liked songs:\n\t[");
            for (int i = 0; i < user.getLikedSongs().size(); i++) {
                if (i > 0) {
                    message = message.concat(", ");
                }
                message = message.concat(user.getLikedSongs().get(i).getName()
                        + " - "
                        + ((Song) user.getLikedSongs().get(i)).getArtist());

            }
            message = message.concat("]\n\nFollowed playlists:\n\t[");
            for (int i = 0; i < user.getFollowedPlayLists().size(); i++) {
                if (i > 0) {
                    message = message.concat(", ");
                }
                message = message.concat(user.getFollowedPlayLists().get(i).getName()
                        + " - "
                        + ((Playlist) user.getFollowedPlayLists().get(i)).getOwner());
            }
            message = message.concat("]");
        }

        output.setMessage(message);

        return output;
    }
}
