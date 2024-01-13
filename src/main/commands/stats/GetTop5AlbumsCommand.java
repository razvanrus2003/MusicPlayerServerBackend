package main.commands.stats;

import main.Library;
import main.User;
import main.commands.Command;
import main.items.Album;
import main.output.CommandOutput;
import main.output.GetTop5AlbumsOutput;

import java.util.ArrayList;

import static java.lang.Math.min;

public final class GetTop5AlbumsCommand extends Command {
    private static final int FIVE = 5;

    @Override
    public CommandOutput execute() {
        User user = Library.getUser(username);
        GetTop5AlbumsOutput output = new GetTop5AlbumsOutput(this);

        ArrayList<Album> top5 = new ArrayList<>();

        ArrayList<Album> albums = Library.getInstance().getAlbums();
        for (Album album : albums) {
            if (top5.isEmpty()) {
                top5.add(0, album);
            } else {
                int i;
                for (i = 0; i < min(FIVE, top5.size()); i++) {
                    if (album.getLikes() > top5.get(i).getLikes()) {
                        top5.add(i, album);
                        i = FIVE;
                    } else if (album.getLikes() == top5.get(i).getLikes() && album.getName().compareTo(top5.get(i).getName()) < 0) {
                        top5.add(i, album);
                        i = FIVE;
                    }
                }
                if (i != FIVE + 1) {
                    top5.add(min(FIVE, top5.size()), album);
                }
            }

        }

        for (int i = 0; i < min(FIVE, top5.size()); i++) {
            output.getResults().add(top5.get(i).getName());
        }
        return output;
    }
}
