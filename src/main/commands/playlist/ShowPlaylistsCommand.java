package main.commands.playlist;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;
import main.output.ShowPlaylistOutput;

public final class ShowPlaylistsCommand extends Command {
    @Override
    public CommandOutput execute() {
        User user = Library.getUser(username);

        ShowPlaylistOutput output = new ShowPlaylistOutput(this);
        output.setResult(user.getPlaylists());
        return output;
    }
}
