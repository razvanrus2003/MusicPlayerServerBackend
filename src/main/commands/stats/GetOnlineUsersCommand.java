package main.commands.stats;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;
import main.output.GetOnlineUsersOutput;

import java.util.ArrayList;

public final class GetOnlineUsersCommand extends Command {
    @Override
    public CommandOutput execute() {
        GetOnlineUsersOutput output = new GetOnlineUsersOutput(this);
        ArrayList<User> users = Library.getInstance().getUsers();
        ArrayList<String> result = new ArrayList<>();
        for (User user : users) {
            if (user.isOnline() && user.getType().equals("user")) {
                result.add(user.getUsername());
            }
        }
        output.setResults(result);
        return output;
    }
}
