package main.commands.stats;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;
import main.output.GetOnlineUsersOutput;

import java.util.ArrayList;

public final class GetAllUsersCommand extends Command {
    @Override
    public CommandOutput execute() {
        GetOnlineUsersOutput output = new GetOnlineUsersOutput(this);
        ArrayList<User> users = Library.getInstance().getUsers();
        ArrayList<User> artists = Library.getInstance().getArtists();
        ArrayList<User> hosts = Library.getInstance().getHosts();

        ArrayList<String> result = new ArrayList<>();
        for (User user : users) {
            result.add(user.getUsername());
        }
        for (User user : artists) {
            result.add(user.getUsername());
        }
        for (User user : hosts) {
            result.add(user.getUsername());
        }
        output.setResults(result);
        return output;
    }
}
