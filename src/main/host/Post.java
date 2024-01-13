package main.host;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.commands.host.AddAnnouncementCommand;

@Setter
@Getter
@ToString
public final class Post {
    private String username;
    private String name;
    private String description;

    public Post(AddAnnouncementCommand command) {
        this.username = command.getUsername();
        this.name = command.getName();
        this.description = command.getDescription();
    }
}
