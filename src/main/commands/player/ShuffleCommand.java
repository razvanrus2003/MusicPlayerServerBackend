package main.commands.player;

import lombok.Getter;
import lombok.Setter;
import main.Library;
import main.MusicPlayer;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.items.Playlist;
import main.output.CommandOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter
@Setter
public final class ShuffleCommand extends Command {
    private int seed;

    /**
     * for coding style
     */
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        MusicPlayer musicPlayer = user.getMusicPlayer();
        user.getMusicPlayer().checkStatus(timestamp);
        if (musicPlayer == null || user.getMusicPlayer().getLoadedStatus().getName().isEmpty()) {
            output.setMessage("Please load a source before using the shuffle function.");
            return output;
        }
        if (!user.getMusicPlayer().getType().equals("playlists")
                && !user.getMusicPlayer().getType().equals("albums")) {
            output.setMessage("The loaded source is not a playlist or an album.");
            return output;
        }

        Item loadedSong = user.getMusicPlayer().getLoaded();
        ArrayList<Integer> order = user.getMusicPlayer().getOrder();
        if (user.getMusicPlayer().getSrcStatus().isShuffle()) {
            output.setMessage("Shuffle function deactivated successfully.");
            for (int i = 0; i < order.size(); i++) {
                order.set(i, i);
            }
            user.getMusicPlayer().getSrcStatus().setShuffle(false);
            user.getMusicPlayer().getLoadedStatus().setShuffle(false);
            ((Playlist) musicPlayer.getSrc()).updateTime(
                    musicPlayer.getSrcStatus(),
                    musicPlayer.getLoadedStatus()
            );
        } else {
            Collections.shuffle(order, new Random(seed));
            output.setMessage("Shuffle function activated successfully.");
            user.getMusicPlayer().getSrcStatus().setShuffle(true);
            user.getMusicPlayer().getLoadedStatus().setShuffle(true);
        }

        return output;
    }
}
