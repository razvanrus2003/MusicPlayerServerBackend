package main.commands.artist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.artist.Event;
import main.commands.Command;
import main.output.CommandOutput;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class AddEventCommand extends Command {
    private String name;
    private String date;
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

        if (user.getEvents() == null) {
            user.setEvents(new ArrayList<>());
        }

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(date);
        } catch (ParseException dtpe) {
            output.setMessage("Event for " + username + " does not have a valid date.");
            return output;
        }

        for (Event event : user.getEvents()) {
            if (event.getName().equals(name)) {
                output.setMessage(username + " has another event with the same name.");
                return output;
            }
        }

        user.getEvents().add(new Event(this));
        output.setMessage(username + " has added new event successfully.");
        user.notifyObservers("New Event");
        return output;
    }
}
