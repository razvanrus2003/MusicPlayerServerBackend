package main.commands.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.items.Ad;
import main.items.Collection;
import main.items.Song;
import main.output.CommandOutput;

@Getter
@Setter
@ToString
public class AdBreakCommand extends Command {
    private int price;
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (user.getMusicPlayer() != null && user.getMusicPlayer().getLoaded() != null && user.isOnline()) {
            user.getMusicPlayer().checkStatus(timestamp);
        }

        if (user.getMusicPlayer() == null || user.getMusicPlayer().getLoaded() == null) {
            output.setMessage(username + " is not playing any music.");
            return output;
        }

        Ad ad = new Ad();
        ad.setPrice(price);
        ad.setDuration(0);
        user.getFreeSong().add(ad);
//        for (Song song1 : user.getFreeSong()) {
//            System.out.print(song1.getDuration() + " ");
//        }
//        System.out.println();

        output.setMessage("Ad inserted successfully.");
        return output;
    }
}
