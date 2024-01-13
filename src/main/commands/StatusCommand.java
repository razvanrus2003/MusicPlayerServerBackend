package main.commands;

import main.Library;
import main.User;
import main.output.CommandOutput;
import main.output.StatusOutput;

public final class StatusCommand extends Command {
    @Override
    public CommandOutput execute() {
        StatusOutput output = new StatusOutput(this);

        User user = Library.getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }
        if (user.getMusicPlayer() != null && user.getMusicPlayer().getLoaded() != null && user.isOnline()) {
            user.getMusicPlayer().checkStatus(timestamp);
        }
        output.setStatus(user.getMusicPlayer().getLoadedStatus());
        return output;
    }
}
