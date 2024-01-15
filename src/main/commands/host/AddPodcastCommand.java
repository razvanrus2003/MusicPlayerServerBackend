package main.commands.host;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.items.Episode;
import main.items.Item;
import main.items.Podcast;
import main.output.CommandOutput;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class AddPodcastCommand extends Command {
    private String name;
    private ArrayList<Episode> episodes;

    @Override
    public CommandOutput execute() {
        User user = Library.getInstance().getUser(username);
        CommandOutput output = new CommandOutput(this);

        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (!user.isOnline()) {
            output.setMessage(username + " is offline.");
            return output;
        }

        if (!user.getType().equals("host")) {
            output.setMessage(username + " is not a host.");
            return output;
        }
        ArrayList<Item> podcasts = user.getPlaylists();
        for (Item podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                output.setMessage(username + " has another podcast with the same name.");
                return output;
            }
        }

        int i = 0;
        for (Episode episode : episodes) {
            int j = 0;
            for (Episode episode1 : episodes) {
                if (episode1.getName().equals(episode.getName()) && i != j) {
                    output.setMessage(username + " has the same episode in this podcast.");
                    return output;
                }
                j++;
            }
            i++;
        }

        output.setMessage(username + " has added new podcast successfully.");
        Podcast newPodcast = new Podcast(this);
        user.getPlaylists().add(newPodcast);
        Library.getInstance().getPodcasts().add(newPodcast);
        return output;
    }
}
