package main.commands.stats;

import main.Library;
import main.User;
import main.commands.Command;
import main.items.Playlist;
import main.output.CommandOutput;
import main.output.GetTop5SongsOutput;

import java.util.ArrayList;

import static java.lang.Math.min;

public final class GetTop5PlaylistsCommand extends Command {
    private static final int FIVE = 5;

    @Override
    public CommandOutput execute() {
        User user = Library.getInstance().getUser(username);
        GetTop5SongsOutput output = new GetTop5SongsOutput(this);

        ArrayList<Playlist> top5 = new ArrayList<Playlist>();

        ArrayList<Playlist> playlists = Library.getInstance().getPlaylists();
        for (Playlist playlist : playlists) {
            if (playlist.isVisible()) {
                if (top5.isEmpty()) {
                    top5.add(0, playlist);
                } else {
                    int i;
                    for (i = 0; i < min(FIVE, top5.size()); i++) {
                        if (playlist.getFollewers() > top5.get(i).getFollewers()) {
                            top5.add(i, playlist);
                            i = FIVE;
                        }
                    }
                    if (i != FIVE + 1) {
                        top5.add(min(FIVE, top5.size()), playlist);
                    }
                }
            }
        }

        for (int i = 0; i < min(FIVE, top5.size()); i++) {
            output.getResults().add(top5.get(i).getName());
        }
        return output;
    }
}
