package main.commands;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.items.Item;
import main.items.Playlist;
import main.items.Song;
import main.output.CommandOutput;
import main.pageControl.ChangePage;
import main.pageControl.PageCommand;

@Getter
@Setter
@ToString
public final class SelectCommand extends Command {
    protected int itemNumber;

    public SelectCommand() {
    }

    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.isOnline()) {
            output.setMessage(username + " is offline.");
            return output;
        }

//        if (!user.getFreeSong().isEmpty()) {
//            for (Song song1 : user.getFreeSong()) {
//                System.out.print(song1.getDuration() + " ");
//            }
//            System.out.println();
//        }

        if (user.getMusicPlayer().getLastResultsUsers() != null) {
            if (user.getMusicPlayer().getLastResultsUsers().size() < itemNumber) {
                output.setMessage("The selected ID is too high.");
                return output;
            }
            user.getMusicPlayer().setSelected(user.getMusicPlayer().getLastResultsUsers().get(itemNumber - 1));
            user.getMusicPlayer().setLastResultsUsers(null);

            String pageType;
            if (user.getMusicPlayer().getSelected().getType().equals("artist")) {
                pageType ="Artist";
            } else {
                pageType = "Host";
            }

            PageCommand pageCommand = new ChangePage(user, user.getMusicPlayer().getSelected(), pageType);
            user.getNextPagesCommand().clear();
            user.getNextPagesCommand().add(pageCommand);
            user.nextPage();

            output.setMessage("Successfully selected "
                    + user.getMusicPlayer().getSelected().getUsername()
                    + "'s page.");
            return output;
        }

        if (user.getMusicPlayer().getLastResults() == null) {
            output.setMessage("Please conduct a search before making a selection.");
            return output;
        }
        if (user.getMusicPlayer().getLastResults().size() < itemNumber) {
            output.setMessage("The selected ID is too high.");
            return output;
        }

        if (user.getMusicPlayer().getType().equals("albums")) {
            Item album = user.getMusicPlayer().getLastResults().get(itemNumber - 1);
            Playlist playlist = new Playlist();
            playlist.setVisible(true);
            playlist.setSongs(album.getContent());

            playlist.setDuration(album.getDuration());

//            user.getMusicPlayer().setType("playlists");

            user.getMusicPlayer().setSrc(playlist);


            user.getMusicPlayer().getSrcStatus().setNewItemStatus(
                    user.getMusicPlayer().getSrc(),
                    timestamp);
        } else {

            user.getMusicPlayer().setSrc(user.getMusicPlayer().getLastResults().get(itemNumber - 1));
            user.getMusicPlayer().getSrcStatus().setNewItemStatus(
                    user.getMusicPlayer().getSrc(),
                    timestamp);
        }


        output.setMessage("Successfully selected "
                + user.getMusicPlayer().getLastResults().get(itemNumber - 1).getName()
                + ".");
        user.getMusicPlayer().getLastResults().clear();
        return output;
    }
}
