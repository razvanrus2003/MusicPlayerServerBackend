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
public final class CreatePlaylistCommand extends Command {
    protected String playlistName;

    /**
     * for coding style
     */
    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        ArrayList<Item> playlists = user.getPlaylists();

        for (Item playlist : playlists) {
            if (playlist.getName().equals(playlistName)) {
                output.setMessage("A playlist with the same name already exists.");
                return output;
            }
        }
        Playlist newPlaylist = new Playlist(this);
        Library.getInstance().getPlaylists().add(newPlaylist);
        playlists.add(newPlaylist);

        output.setMessage("Playlist created successfully.");
        return output;
    }
}
