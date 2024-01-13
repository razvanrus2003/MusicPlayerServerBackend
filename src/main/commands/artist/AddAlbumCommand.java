package main.commands.artist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.items.Album;
import main.items.Item;
import main.items.Song;
import main.output.CommandOutput;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class AddAlbumCommand extends Command {
    private String name;
    private int releaseYear;
    private String description;
    private ArrayList<Song> songs;

    @Override
    public CommandOutput execute() {
        User user = Library.getUser(username);
        CommandOutput output = new CommandOutput(this);


        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.getType().equals("artist")) {
            output.setMessage(username + " is not an artist.");
            return output;
        }
        if (!user.isOnline()) {
            output.setMessage(username + " is offline.");
            return output;
        }

        ArrayList<Item> albums = user.getPlaylists();
        for (Item album : albums) {
            if (album.getName().equals(name)) {
                output.setMessage(username + " has another album with the same name.");
                return output;
            }
        }

        int i = 0;
        for (Song song1 : songs) {
            int j = 0;
            for (Song song2 : songs) {
                if (song2.getName().equals(song1.getName()) && i != j) {
                    output.setMessage(username
                            + " has the same song at least twice in this album.");
                    return output;
                }
                j++;
            }
            i++;
        }

        for (Song song : songs) {

            Library.getInstance().getSongs().add(song);
        }
        output.setMessage(username + " has added new album successfully.");
        Album newAlbum = new Album(this);
        user.getPlaylists().add(newAlbum);
        Library.getInstance().getAlbums().add(newAlbum);
        return output;
    }
}
