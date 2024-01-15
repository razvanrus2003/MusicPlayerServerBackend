package main.commands.user;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

/**
 * Command class that is responsible for cancelling the premium subscription.
 * It extends the Command class.
 */
public final class CancelPremium extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (user.getMusicPlayer() != null
                && user.getMusicPlayer().getLoaded() != null
                && user.isOnline()) {
            user.getMusicPlayer().checkStatus(timestamp);
        }

        if (!user.isPremium()) {
            output.setMessage(username + " is not a premium user.");
            return output;
        }
        user.cancelPremium();
        output.setMessage(username + " cancelled the subscription successfully.");
        return output;
    }
}
