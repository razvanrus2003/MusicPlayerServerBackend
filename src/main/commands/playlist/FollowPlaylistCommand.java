package main.commands.playlist;

import main.Library;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.items.Playlist;
import main.output.CommandOutput;

import java.util.ArrayList;

public final class FollowPlaylistCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);

        if (user.getPlaylists() == null || user.getMusicPlayer().getSrcStatus() == null) {
            output.setMessage("Please select a source before following or unfollowing.");
            return output;
        }

        if (user.getMusicPlayer() == null
                || user.getMusicPlayer().getSrcStatus().getName().isEmpty()) {
            output.setMessage("Please select a source before following or unfollowing.");
            return output;
        }

        if (!user.getMusicPlayer().getType().equals("playlists")) {
            output.setMessage("The selected source is not a playlist.");
            return output;
        }
        if (user.getUsername().equals(((Playlist) user.getMusicPlayer().getSrc()).getOwner())) {
            output.setMessage("You cannot follow or unfollow your own playlist.");
            return output;
        }

        Playlist playlist = (Playlist) user.getMusicPlayer().getSrc();
        ArrayList<Item> followedPlaylists = user.getFollowedPlayLists();
        if (followedPlaylists.contains(playlist)) {
            output.setMessage("Playlist unfollowed successfully.");
            followedPlaylists.remove(playlist);
            playlist.setFollewers(playlist.getFollewers() - 1);
        } else {
            output.setMessage("Playlist followed successfully.");
            followedPlaylists.add(playlist);
            playlist.setFollewers(playlist.getFollewers() + 1);
        }

        return output;
    }
}
