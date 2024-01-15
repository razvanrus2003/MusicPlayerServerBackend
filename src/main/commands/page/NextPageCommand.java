package main.commands.page;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

public final class NextPageCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (user.getNextPagesCommand().isEmpty()) {
            output.setMessage("There are no pages left to go forward.");
            return output;
        }

        user.nextPage();
        output.setMessage("The user " + username + " has navigated successfully to the next page.");

        return output;
    }
}
