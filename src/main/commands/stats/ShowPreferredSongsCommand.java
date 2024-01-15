package main.commands.stats;

import main.Library;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.output.CommandOutput;
import main.output.ShowPreferredSongsOutput;

import java.util.ArrayList;

public final class ShowPreferredSongsCommand extends Command {
    @Override
    public CommandOutput execute() {
        User user = Library.getInstance().getUser(username);

        ArrayList<Item> results = user.getLikedSongs();

        ShowPreferredSongsOutput output = new ShowPreferredSongsOutput(this);
        for (Item song : results) {
            output.getResults().add(song.getName());
        }

        return output;
    }
}
