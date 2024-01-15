package main.commands.notifications;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

/**
 * SubscribeCommand class is responsible for subscribing to an artist or host.
 * <p>
 * It extends the Command class.
 */
public final class SubscribeCommand extends Command {

    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.getPage().equals("Artist") && !user.getPage().equals("Host")) {
            output.setMessage("To subscribe you need to be on the page of an artist or host.");
            return output;
        }

        if (user.getMusicPlayer().getSelected().getSubscribers().contains(user.getNewsObserver())) {
            user.getMusicPlayer().getSelected().getSubscribers().remove(user.getNewsObserver());
            output.setMessage(username
                    + " unsubscribed from "
                    + user.getMusicPlayer().getSelected().getUsername()
                    + " successfully.");
        } else {
            user.getMusicPlayer().getSelected().getSubscribers().add(user.getNewsObserver());
            output.setMessage(username
                    + " subscribed to "
                    + user.getMusicPlayer().getSelected().getUsername()
                    + " successfully.");
        }
        return output;
    }
}
