package main.commands.notifications;

import main.Library;
import main.User;
import main.commands.Command;
import main.items.Collection;
import main.output.CommandOutput;
import main.output.GetNotificationsOutput;

import java.util.ArrayList;

public class GetNotificationsCommand extends Command {
    @Override
    public CommandOutput execute() {
        GetNotificationsOutput output = new GetNotificationsOutput(this);

        User user = Library.getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        output.setNotifications(user.getNotifications());
        user.setNotifications(new ArrayList<>());
        return output;
    }
}
