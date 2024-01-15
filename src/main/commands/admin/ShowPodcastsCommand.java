package main.commands.admin;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;
import main.output.ShowPodcastsOutput;

public final class ShowPodcastsCommand extends Command {
    @Override
    public CommandOutput execute() {
        User user = Library.getInstance().getUser(username);
        ShowPodcastsOutput output = new ShowPodcastsOutput(this);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        output.setResults(user.getPlaylists());
        return output;
    }
}
