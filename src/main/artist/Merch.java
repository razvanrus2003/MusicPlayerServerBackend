package main.artist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.commands.artist.AddMerchCommand;

@Getter
@Setter
@ToString
public final class Merch {
    private String username;
    private String name;
    private int price;
    private String description;

    public Merch(final AddMerchCommand command) {
        this.username = command.getUsername();
        this.name = command.getName();
        this.price = command.getPrice();
        this.description = command.getDescription();
    }
}
