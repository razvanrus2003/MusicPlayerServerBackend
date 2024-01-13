package main.artist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.commands.artist.AddEventCommand;

@Getter
@Setter
@ToString
public final class Event {
    private String username;
    private String name;
    private String date;
    private String description;

    public Event(final AddEventCommand command) {
        this.username = command.getUsername();
        this.name = command.getName();
        this.date = command.getDate();
        this.description = command.getDescription();
    }
}
