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
public final class AddAnnouncementCommand extends Command {
    private String name;
    private String description;

    @Override
    public CommandOutput execute() {
        User user = Library.getInstance().getUser(username);
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

        for (Post post : user.getPosts()) {
            if (post.getName().equals(name)) {
                output.setMessage(username + " has already added an announcement with this name.");
                return output;
            }
        }

        user.getPosts().add(new Post(this));
        output.setMessage(username + " has successfully added new announcement.");
        return output;
    }
}
