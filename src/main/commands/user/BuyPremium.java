package main.commands.user;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

import java.util.ArrayList;

/**
 * Command class that is responsible for buying the premium subscription.
 * It extends the Command class.
 */
public final class BuyPremium extends Command {

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

        if (user.isPremium()) {
            output.setMessage(username + " is already a premium user.");
            return output;
        }

        user.setPremium(true);
        user.setPremiumSong(new ArrayList<>());
        output.setMessage(username + " bought the subscription successfully.");

        return output;
    }
}
