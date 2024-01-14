package main.commands.artist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.artist.Merch;
import main.commands.Command;
import main.output.CommandOutput;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class AddMerchCommand extends Command {
    private String name;
    private int price;
    private String description;

    @Override
    public CommandOutput execute() {
        User user = Library.getUser(username);
        CommandOutput output = new CommandOutput(this);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.isOnline()) {
            output.setMessage(username + " is offline.");
            return output;
        }

        if (!user.getType().equals("artist")) {
            output.setMessage(username + " is not an artist.");
            return output;
        }

        if (price < 0) {
            output.setMessage("Price for merchandise can not be negative.");
            return output;
        }

        if (user.getMerches() == null) {
            user.setMerches(new ArrayList<>());
        }

        for (Merch merch : user.getMerches()) {
            if (merch.getName().equals(name)) {
                output.setMessage(username + " has merchandise with the same name.");
                return output;
            }
        }

        user.getMerches().add(new Merch(this));
        output.setMessage(username + " has added new merchandise successfully.");
        user.notifyObservers("New Merchandise");
        return output;
    }
}
