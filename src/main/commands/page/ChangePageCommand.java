package main.commands.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.items.Podcast;
import main.items.Song;
import main.output.CommandOutput;
import main.pageControl.ChangePage;
import main.pageControl.PageCommand;

/**
 * Concrete command class: ChangePageCommand.
 */
@Getter
@Setter
@ToString
public final class ChangePageCommand extends Command {
    private String nextPage;

    @Override
    public CommandOutput execute() {
        User user = Library.getInstance().getUser(username);
        CommandOutput output = new CommandOutput(this);

        if (user.getMusicPlayer() != null
                && user.getMusicPlayer().getLoaded() != null && user.isOnline()) {
            user.getMusicPlayer().checkStatus(timestamp);
        }

        output.setMessage(username + " is trying to access a non-existent page.");
        if (nextPage.equals("Home")) {
            output.setMessage(username + " accessed Home successfully.");

            PageCommand pageCommand = new ChangePage(user, null, nextPage);
            user.getNextPagesCommand().clear();
            user.getNextPagesCommand().add(pageCommand);
            user.nextPage();

        }
        if (nextPage.equals("LikedContent")) {
            output.setMessage(username + " accessed LikedContent successfully.");

            PageCommand pageCommand = new ChangePage(user, null, nextPage);
            user.getNextPagesCommand().clear();
            user.getNextPagesCommand().add(pageCommand);
            user.nextPage();

        }
        if (nextPage.equals("Artist")) {
            user.getMusicPlayer().setSelected(
                    Library.getInstance().getUser(
                            ((Song) user.getMusicPlayer().getLoaded()).getArtist()
                    ));

            output.setMessage(username + " accessed Artist successfully.");

            PageCommand pageCommand = new ChangePage(
                    user,
                    user.getMusicPlayer().getSelected(),
                    nextPage);
            user.getNextPagesCommand().clear();
            user.getNextPagesCommand().add(pageCommand);
            user.nextPage();

        }
        if (nextPage.equals("Host")) {
            user.getMusicPlayer().setSelected(
                    Library.getInstance().getUser(
                            ((Podcast) user.getMusicPlayer().getSrc()).getOwner()));

            PageCommand pageCommand = new ChangePage(
                    user,
                    user.getMusicPlayer().getSelected(),
                    nextPage);
            user.getNextPagesCommand().clear();
            user.getNextPagesCommand().add(pageCommand);
            user.nextPage();

            output.setMessage(username + " accessed Host successfully.");
        }

        return output;
    }
}
