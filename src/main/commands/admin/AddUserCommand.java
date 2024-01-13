package main.commands.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public final class AddUserCommand  extends Command {
    private int age;
    private String city;
    private String type;

    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        if (user != null) {
            output.setMessage("The username " + username + " is already taken.");
            return output;
        }

        User newUser = new User(this);
        if (type.equals("artist")) {
            newUser.setMerches(new ArrayList<>());
            newUser.setEvents(new ArrayList<>());
            Library.getInstance().getArtists().add(newUser);
        } else if (type.equals("host")) {
            newUser.setPosts(new ArrayList<>());
            Library.getInstance().getHosts().add(newUser);
            Library.getInstance().getPodcasts().stream()
                    .filter(podcast -> podcast.getOwner().equals(username))
                    .forEach(podcast -> newUser.getPlaylists().add(podcast));

        } else {
            Library.getInstance().getUsers().add(newUser);
        }

        output.setMessage("The username " + username + " has been added successfully.");
        return output;
    }
}
