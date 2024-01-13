package main.commands.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.artist.Merch;
import main.commands.Command;
import main.output.CommandOutput;
import main.output.SeeMerchOutput;
import net.sf.saxon.expr.Component;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class BuyMerchCommand extends Command {
    private String name;
    @Override
    public CommandOutput execute() {
        User user = Library.getUser(username);
        CommandOutput output = new CommandOutput(this);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.getPage().equals("Artist")) {
            output.setMessage("Cannot buy merch from this page.");
            return output;
        }

        ArrayList<Merch> merches = user.getMusicPlayer().getSelected().getMerches();
        if (!merches.stream().map(Merch::getName).toList().contains(name)) {
            output.setMessage("The merch " + name + " doesn't exist.");
            return output;
        }

        Merch merch = merches.stream().filter(merch1 -> merch1.getName().equals(name)).findFirst().get();

        user.getMusicPlayer().getSelected().setMerchRevenue(
                user.getMusicPlayer().getSelected().getMerchRevenue()
                + merch.getPrice()
        );

        user.getMerches().add(merch);
        output.setMessage(username + " has added new merch successfully.");

        return output;
    }
}
