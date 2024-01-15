package main.commands.artist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.output.CommandOutput;

@Getter
@Setter
@ToString
public final class RemoveAlbumCommand extends Command {
    private String name;

    @Override
    public CommandOutput execute() {
        User user = Library.getInstance().getUser(username);
        CommandOutput output = new CommandOutput(this);

        for (User user1 : Library.getInstance().getUsers()) {
            if (user1.getMusicPlayer().getLoaded() != null) {
                user1.getMusicPlayer().checkStatus(timestamp);
            }
        }

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

        Item toDelete = null;
        for (Item album : user.getPlaylists()) {
            if (album.getName().equals(name)) {
                toDelete = album;
            }
        }

        if (toDelete == null) {
            output.setMessage(username + " doesn't have an album with the given name.");
            return output;
        }

        if (!toDelete.canDelete()) {
            output.setMessage(username + " can't delete this album.");
            return output;
        }

        for (Item song : toDelete.getContent()) {
            Library.getInstance().getSongs().remove(song);
        }
        Library.getInstance().getAlbums().remove(toDelete);
        user.getPlaylists().remove(toDelete);
        output.setMessage(username + " deleted the album successfully.");

        return output;
    }
}
