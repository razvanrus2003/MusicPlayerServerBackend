package main.commands;

import lombok.Getter;
import lombok.Setter;
import main.Library;
import main.MusicPlayer;
import main.User;
import main.items.Item;
import main.output.CommandOutput;

import java.util.ArrayList;

@Getter
@Setter
public final class AddRemoveInPlaylistCommand extends Command {
    private int playlistId;

    /**
     * Execute command and generates an output
     */
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);

        if (user.getMusicPlayer() != null) {
            user.getMusicPlayer().checkStatus(timestamp);
        }
        MusicPlayer musicPlayer = user.getMusicPlayer();

        if (musicPlayer.getLoadedStatus().getName().isEmpty()) {
            String msg = "Please load a source before adding to or removing from the playlist.";
            output.setMessage(msg);
            return output;
        }
        if (user.getMusicPlayer().getType().equals("podcasts")) {
            output.setMessage("The loaded source is not a song.");
            return output;
        }
        if (user.getPlaylists().size() < playlistId) {
            output.setMessage("The specified playlist does not exist.");
            return output;
        }
        ArrayList<Item> playlist = user.getPlaylists().get(playlistId - 1).getContent();
        for (Item song : playlist) {
            if (song.getName().equals(user.getMusicPlayer().getSrcStatus().getName())) {
                output.setMessage("Successfully removed from playlist.");

                Item playlist1 = user.getPlaylists().get(playlistId - 1);
                playlist1.subDuration(musicPlayer.getSrc().getDuration());
                playlist.remove(song);

                return output;
            }
        }
        output.setMessage("Successfully added to playlist.");

        playlist.add(user.getMusicPlayer().getLoaded());
        user.getPlaylists().get(playlistId - 1).addDuration(musicPlayer.getLoaded().getDuration());
        return output;
    }
}
