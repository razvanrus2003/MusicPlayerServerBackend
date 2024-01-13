package main.commands.admin;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;
import main.output.ShowAlbumsOutput;

public final class ShowAlbumsCommand extends Command {
    @Override
    public CommandOutput execute() {

        User user = Library.getUser(username);
        ShowAlbumsOutput output = new ShowAlbumsOutput(this);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        output.setResults(user.getPlaylists());
        return output;
    }
}
