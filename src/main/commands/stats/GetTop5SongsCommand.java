package main.commands.stats;

import main.Library;
import main.User;
import main.commands.Command;
import main.items.Song;
import main.output.CommandOutput;
import main.output.GetTop5SongsOutput;

import java.util.ArrayList;

import static java.lang.Math.min;

public final class GetTop5SongsCommand extends Command {
    private static final int FIVE = 5;

    @Override
    public CommandOutput execute() {
        User user = Library.getInstance().getUser(username);
        GetTop5SongsOutput output = new GetTop5SongsOutput(this);

        ArrayList<Song> top5 = new ArrayList<Song>();

        ArrayList<Song> songs = Library.getInstance().getSongs();
        for (Song song : songs) {
            if (top5.isEmpty()) {
                top5.add(0, song);
            } else {
                int i;
                for (i = 0; i < min(FIVE, top5.size()); i++) {
                    if (song.getLikes() > top5.get(i).getLikes()) {
                        top5.add(i, song);
                        i = FIVE;
                    }
                }
                if (i != FIVE + 1) {
                    top5.add(min(FIVE, top5.size()), song);
                }
            }

        }

        for (int i = 0; i < min(FIVE, top5.size()); i++) {
            output.getResults().add(top5.get(i).getName());
        }
        return output;
    }
}
