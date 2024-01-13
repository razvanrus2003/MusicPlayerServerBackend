package main.commands;

import main.Library;
import main.MusicPlayer;
import main.Status;
import main.User;
import main.items.Item;
import main.output.CommandOutput;

import java.util.ArrayList;


public final class LoadCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        if (user == null)
            return null;
        MusicPlayer musicPlayer = user.getMusicPlayer();
        if (user.getMusicPlayer().getSrc() == null || user.getMusicPlayer().getLoaded() != null) {
            output.setMessage("Please select a source before attempting to load.");
            return output;
        }

        Status loadedStatus = musicPlayer.getLoadedStatus();
        Item src = musicPlayer.getSrc();

        musicPlayer.getSrcStatus().setNewItemStatus(user.getMusicPlayer().getSrc(), timestamp);

        if (!user.getMusicPlayer().getType().equals("songs")) {
            ArrayList<Item> list = user.getMusicPlayer().getSrc().getContent();
            if (user.getMusicPlayer().getType().equals("podcasts")) {
                if (user.getPlayedPodcasts().containsKey(musicPlayer.getSrc().getName())) {

                    int remainedTime = user.getPlayedPodcasts().get(src.getName());
                    int cont = 0;
                    int id = 0;
                    Item curr = list.get(0);
                    while (cont < musicPlayer.getSrc().getDuration() - remainedTime) {
                        cont += curr.getDuration();
                        id++;
                        curr = list.get(id);
                    }

                    musicPlayer.getLoadedStatus().setNewItemStatus(list.get(0), timestamp);
                    loadedStatus.setRemainedTime(cont - src.getDuration() + remainedTime);
                    user.getMusicPlayer().getLoadedStatus().setPosition(id - 1);
                    user.getMusicPlayer().setLoaded(list.get(id - 1));

                    user.getPlayedPodcasts().remove(user.getMusicPlayer().getSrc().getName());
                    output.setMessage("Playback loaded successfully.");
                    user.getMusicPlayer().getLastResults().clear();
                    return output;
                }
            }

            if (list.isEmpty()) {
                output.setMessage("You can't load an empty audio collection!");
                user.getMusicPlayer().getLastResults().clear();
                return output;
            }
            if (user.getMusicPlayer().getType().equals("playlists") ||
                    user.getMusicPlayer().getType().equals("albums")) {
                ArrayList<Integer> order = user.getMusicPlayer().getOrder();
                order.clear();
                for (int i = 0; i < list.size(); i++) {
                    order.add(i, i);
                    user.getMusicPlayer().getSrcStatus().setOrder(order);
                }
                user.updateHistory(list.get(0));
            }
            user.getMusicPlayer().getLoadedStatus().setNewItemStatus(list.get(0), timestamp);
            user.getMusicPlayer().setLoaded(list.get(0));
        } else {
            loadedStatus.setNewItemStatus(user.getMusicPlayer().getSrc(), timestamp);
            user.getMusicPlayer().setLoaded(user.getMusicPlayer().getSrc());
            user.updateHistory(user.getMusicPlayer().getSrc());
        }

        output.setMessage("Playback loaded successfully.");
        user.getMusicPlayer().setLastResults(null);
        return output;
    }
}
