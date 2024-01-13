package main.commands.artist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.artist.Event;
import main.commands.Command;
import main.output.CommandOutput;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class RemoveEventCommand extends Command {
    private String name;

    @Override
    public CommandOutput execute() {
        User user = Library.getUser(username);
        CommandOutput output = new CommandOutput(this);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.isOnline()) {
            output.setMessage(username + " is offline.");
            return output;
        }

        if (!user.getType().equals("artist")) {
            output.setMessage(username + " is not an artist.");
            return output;
        }

        if (user.getPosts() == null) {
            user.setPosts(new ArrayList<>());
        }

        Event toRemove = null;
        for (Event event : user.getEvents()) {
            if (event.getName().equals(name)) {
                toRemove = event;
                output.setMessage(username + " deleted the event successfully.");
            }
        }
        if (toRemove != null) {
            user.getEvents().remove(toRemove);
            return output;
        }

        output.setMessage(username + " has no events with the given name.");
        return output;
    }
}
