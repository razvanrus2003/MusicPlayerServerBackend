package main.commands.host;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.output.CommandOutput;

@Getter
@Setter
@ToString
public final class RemovePodcastCommand extends Command {
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

        if (!user.getType().equals("host")) {
            output.setMessage(username + " is not an host.");
            return output;
        }

        Item toDelete = null;
        for (Item podcast : user.getPlaylists()) {
            if (podcast.getName().equals(name)) {
                toDelete = podcast;
            }
        }

        if (toDelete == null) {
            output.setMessage(username + " doesn't have a podcast with the given name.");
            return output;
        }

        if (!toDelete.canDelete()) {
            output.setMessage(username + " can't delete this podcast.");
            return output;
        }

        Library.getInstance().getPodcasts().remove(toDelete);
        user.getPlaylists().remove(toDelete);
        output.setMessage(username + " deleted the podcast successfully.");

        return output;
    }
}
