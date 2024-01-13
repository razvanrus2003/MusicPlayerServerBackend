package main.commands.user;

import main.Library;
import main.User;
import main.artist.Merch;
import main.commands.Command;
import main.output.CommandOutput;
import main.output.SearchOutput;
import main.output.SeeMerchOutput;

import java.util.ArrayList;

public class SeeMerchCommand extends Command {
    @Override
    public CommandOutput execute() {
        User user = Library.getUser(username);
        SeeMerchOutput output = new SeeMerchOutput(this);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        ArrayList<String> result = new ArrayList<>();
        for (Merch merch : user.getMerches()) {
            result.add(merch.getName());
        }
        output.setResults(result);
        return output;
    }
}
