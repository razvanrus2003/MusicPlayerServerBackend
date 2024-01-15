package main.commands;

import main.Library;
import main.MusicPlayer;
import main.User;
import main.items.Item;
import main.items.Song;
import main.output.CommandOutput;

import java.util.ArrayList;

public final class LikeCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.isOnline()) {
            output.setMessage(username + " is offline.");
            return output;
        }

        MusicPlayer musicPlayer = user.getMusicPlayer();
        if (user.getMusicPlayer() != null) {
            user.getMusicPlayer().checkStatus(timestamp);
        }
        if (musicPlayer == null || user.getMusicPlayer().getLoaded() == null) {
            output.setMessage("Please load a source before liking or unliking.");
            return output;
        }
        if (user.getMusicPlayer().getType().equals("podcasts")) {
            output.setMessage("Loaded source is not a song.");

            return output;
        }
        Item loadedSong = user.getMusicPlayer().getLoaded();
        ArrayList<Item> likedSongs = user.getLikedSongs();


        if (likedSongs.contains(loadedSong)) {
            output.setMessage("Unlike registered successfully.");
            ((Song) loadedSong).setLikes(((Song) loadedSong).getLikes() - 1);
            likedSongs.remove(loadedSong);
        } else {
            output.setMessage("Like registered successfully.");
            ((Song) loadedSong).setLikes(((Song) loadedSong).getLikes() + 1);
            likedSongs.add(loadedSong);
        }


        return output;
    }
}
