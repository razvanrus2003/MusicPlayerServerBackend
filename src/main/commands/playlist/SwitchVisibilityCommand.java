package main.commands.playlist;

import lombok.Getter;
import main.Library;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.items.Playlist;
import main.output.CommandOutput;

import java.util.ArrayList;

@Getter
public final class SwitchVisibilityCommand extends Command {
    private int playlistId;

    public void setPlaylistId(final int playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        ArrayList<Item> playlists = user.getPlaylists();

        if (playlistId > playlists.size() || playlistId <= 0) {
            output.setMessage("The specified playlist ID is too high.");
            return output;
        }

        if (((Playlist) playlists.get(playlistId - 1)).isVisible()) {
            output.setMessage("Visibility status updated successfully to private.");
            ((Playlist) playlists.get(playlistId - 1)).setVisible(false);
        } else {
            output.setMessage("Visibility status updated successfully to public.");
            ((Playlist) playlists.get(playlistId - 1)).setVisible(true);
        }
        return output;
    }
}
