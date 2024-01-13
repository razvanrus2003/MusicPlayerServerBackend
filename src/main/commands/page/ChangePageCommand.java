package main.commands.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

@Getter
@Setter
@ToString
public final class ChangePageCommand extends Command {
    private String nextPage;

    @Override
    public CommandOutput execute() {
        User user = Library.getUser(username);
        CommandOutput output = new CommandOutput(this);

        output.setMessage(username + " is trying to access a non-existent page.");
        if (nextPage.equals("Home")) {
            user.getMusicPlayer().setSelected(null);
            user.setPage(nextPage);
            output.setMessage(username + " accessed Home successfully.");

        }
        if (nextPage.equals("LikedContent")) {
            user.getMusicPlayer().setSelected(null);

            user.setPage(nextPage);
            output.setMessage(username + " accessed LikedContent successfully.");

        }

        return output;
    }
}
