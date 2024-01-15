package main.commands;

import main.Library;
import main.User;
import main.output.CommandOutput;

/**
 * Command for switching the connection status of a user.
 */
public final class SwitchConnectionStatusCommand extends Command {
    @Override
    public CommandOutput execute() {

        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.getType().equals("user")) {
            output.setMessage(username + " is not a normal user.");
            return output;
        }

        if (user.isOnline()
                && user.getMusicPlayer() != null
                && user.getMusicPlayer().getLoaded() != null) {
            user.getMusicPlayer().checkStatus(timestamp);
        }
        if (user.isOnline()) {
            user.setStatus("offline");
        } else {
            user.setStatus("online");
            if (user.getMusicPlayer().getLoaded() != null) {
                user.getMusicPlayer().getLoadedStatus().setPlayingSince(timestamp);
                user.getMusicPlayer().getSrcStatus().setPlayingSince(timestamp);
            }
        }
        output.setMessage(user.getUsername() + " has changed status successfully.");

        return output;
    }
}
