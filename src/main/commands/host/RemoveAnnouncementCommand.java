package main.commands.host;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.host.Post;
import main.output.CommandOutput;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class RemoveAnnouncementCommand extends Command {
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
            output.setMessage(username + " is not a host.");
            return output;
        }

        if (user.getPosts() == null) {
            user.setPosts(new ArrayList<>());
        }

        Post toRemove = null;
        for (Post post : user.getPosts()) {
            if (post.getName().equals(name)) {
                toRemove = post;
                output.setMessage(username + " has successfully deleted the announcement.");
            }
        }
        if (toRemove != null) {
            user.getPosts().remove(toRemove);
            return output;
        }

        output.setMessage(username + " has no announcement with the given name.");
        return output;
    }
}
