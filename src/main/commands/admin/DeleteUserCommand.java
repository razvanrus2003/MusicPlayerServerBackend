package main.commands.admin;

import main.Library;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.items.Playlist;
import main.items.Song;
import main.output.CommandOutput;

public final class DeleteUserCommand extends Command {
    @Override
    public CommandOutput execute() {
        User user = Library.getUser(username);
        CommandOutput output = new CommandOutput(this);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (user.getType().equals("artist")) {
            for (User user1 : Library.getInstance().getUsers()) {
                if (user1.getMusicPlayer().getSelected() != null
                        && user1.getMusicPlayer().getSelected().equals(user)) {
                    output.setMessage(username + " can't be deleted.");
                    return output;
                }
            }

            output.setMessage(username + " was successfully deleted.");
            boolean delete = true;
            for (User user1 : Library.getInstance().getUsers()) {
                if (user1.getMusicPlayer().getLoaded() != null) {
                    user1.getMusicPlayer().checkStatus(timestamp);
                }
                if (user1.getMusicPlayer().getLoaded() != null
                        && !user1.getMusicPlayer().getType().equals("podcasts") &&
                        ((Song) user1.getMusicPlayer().getLoaded()).getArtist().equals(username)) {
                    output.setMessage(username + " can't be deleted.");
                    delete = false;
                    break;
                }
            }
            if (delete) {
                Library.getInstance().getArtists().remove(user);
            } else {
                return output;
            }

            for (User user1 : Library.getInstance().getUsers()) {
                for (int i = 0; i < user1.getLikedSongs().size(); i++) {
                    Item song = user1.getLikedSongs().get(i);
                    if (((Song) song).getArtist().equals(username)) {
                        user1.getLikedSongs().remove(song);
                        i--;
                    }
                }
            }

            for (int i = 0; i < Library.getInstance().getSongs().size(); i++) {
                Song song = Library.getInstance().getSongs().get(i);
                if ((song).getArtist().equals(username)) {
                    Library.getInstance().getSongs().remove(song);
                    i--;
                }
            }
        } else if (user.getType().equals("user")) {
            for (User user1 : Library.getInstance().getUsers()) {
                if (user1.getMusicPlayer().getLoaded() != null
                        && user1.getMusicPlayer().getLoadedStatus().getRemainedTime() > 0) {
                    user1.getMusicPlayer().checkStatus(timestamp);
                }
            }
            output.setMessage(username + " was successfully deleted.");
            boolean delete = true;
            for (Item playlist : user.getPlaylists()) {
                if (!playlist.canDelete()) {
                    delete = false;
                    output.setMessage(username + " can't be deleted.");
                }
            }
            if (delete) {
                Library.getInstance().getArtists().remove(user);
            } else {
                return output;
            }

            for (User user1 : Library.getInstance().getUsers()) {
                for (int i = 0; i < user1.getFollowedPlayLists().size(); i++) {
                    Item playlist = user1.getFollowedPlayLists().get(i);
                    if (((Playlist) playlist).getOwner().equals(username)) {
                        user1.getFollowedPlayLists().remove(playlist);
                        i--;
                    }
                }
            }

            for (Item playlist : user.getFollowedPlayLists()) {
                ((Playlist) playlist).setFollewers(((Playlist) playlist).getFollewers() - 1);
            }

            for (Item playlist : user.getPlaylists()) {
                Library.getInstance().getPlaylists().remove(playlist);
            }

        } else if (user.getType().equals("host")) {
            for (User user1 : Library.getInstance().getUsers()) {
                if (user1.getMusicPlayer().getLoaded() != null
                        && user1.getMusicPlayer().getLoadedStatus().getRemainedTime() > 0) {
                    user1.getMusicPlayer().checkStatus(timestamp);
                }
            }

            for (User user1 : Library.getInstance().getUsers()) {
                if (user1.getMusicPlayer().getSelected() != null
                        && user1.getMusicPlayer().getSelected().equals(user)) {
                    output.setMessage(username + " can't be deleted.");
                    return output;
                }
            }

            output.setMessage(username + " was successfully deleted.");
            boolean delete = true;
            for (Item podcast : user.getPlaylists()) {
                if (!podcast.canDelete()) {
                    delete = false;
                    output.setMessage(username + " can't be deleted.");
                }
            }
            if (delete) {
                Library.getInstance().getHosts().remove(user);
            } else {
                return output;
            }
            for (Item podcast : user.getPlaylists()) {
                Library.getInstance().getPodcasts().remove(podcast);
            }
        }

        return output;
    }
}
