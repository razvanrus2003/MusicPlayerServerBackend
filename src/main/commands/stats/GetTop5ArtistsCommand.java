package main.commands.stats;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;
import main.output.GetTop5ArtistOutput;

import java.util.ArrayList;

import static java.lang.Math.min;

public final class GetTop5ArtistsCommand extends Command {
    private static final int FIVE = 5;

    @Override
    public CommandOutput execute() {
        GetTop5ArtistOutput output = new GetTop5ArtistOutput(this);

        ArrayList<User> top5 = new ArrayList<>();

        ArrayList<User> artists = Library.getInstance().getArtists();
        for (User artist : artists) {
            if (top5.isEmpty()) {
                top5.add(0, artist);
            } else {
                int i;
                for (i = 0; i < min(FIVE, top5.size()); i++) {
                    if (artist.getLikes() > top5.get(i).getLikes()) {
                        top5.add(i, artist);
                        i = FIVE;
                    }
                }
                if (i != FIVE + 1) {
                    top5.add(min(FIVE, top5.size()), artist);
                }
            }

        }

        for (int i = 0; i < min(FIVE, top5.size()); i++) {
            output.getResults().add(top5.get(i).getUsername());
        }
        return output;
    }
}
