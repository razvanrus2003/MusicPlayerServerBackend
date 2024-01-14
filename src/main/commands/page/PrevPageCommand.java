package main.commands.page;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

public class PrevPageCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (user.getPrevPagesCommand().isEmpty()) {
            output.setMessage("There are no pages left to go back.");
            return output;
        }

        user.prevPage();
        output.setMessage("The user " + username + " has navigated successfully to the previous page.");

        return output;
    }
}
