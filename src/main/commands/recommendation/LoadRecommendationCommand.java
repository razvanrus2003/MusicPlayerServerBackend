package main.commands.recommendation;

import main.Library;
import main.MusicPlayer;
import main.Status;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.items.Podcast;
import main.output.CommandOutput;

import java.util.ArrayList;

public class LoadRecommendationCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        if (user == null)
            return null;

        if (!user.isOnline()) {
            output.setMessage(username + "is offline.");
            return output;
        }
        user.getMusicPlayer().setLastResults(null);
        MusicPlayer musicPlayer = user.getMusicPlayer();
        if (user.getRecommendation().isEmpty()) {
            output.setMessage("No recommendations available.");
            return output;
        }

//        if (user.getMusicPlayer().getSrc() != null) {
//            if (user.getMusicPlayer().getLoadedStatus().getPlayingSince()
//                    + user.getMusicPlayer().getLoadedStatus().getRemainedTime() - timestamp != 0)
//                user.getMusicPlayer().checkStatus(timestamp);
//        }

        Status loadedStatus = musicPlayer.getLoadedStatus();
        Item src = musicPlayer.getSrc();

        musicPlayer.getSrcStatus().setNewItemStatus(
                user.getRecommendation().get(user.getRecommendation().size() - 1)
                , timestamp);

        Item newSrc = user.getRecommendation().get(user.getRecommendation().size() - 1);;
        String type = user.getRecommendationType().get(user.getRecommendationType().size() - 1);

        user.getMusicPlayer().setSrc(newSrc);
        if (type.equals("playlist")) {
            ArrayList<Item> list = user.getMusicPlayer().getSrc().getContent();
            if (list.isEmpty()) {
                output.setMessage("You can't load an empty audio collection!");
                user.getMusicPlayer().getLastResults().clear();
                return output;
            }
            if (!user.getFreeSong().isEmpty() && user.getFreeSong().get(user.getFreeSong().size() - 1).getDuration() == 0) {
                user.getFreeSong().remove(user.getFreeSong().size() - 1);
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

            if (!user.getFreeSong().isEmpty() && user.getFreeSong().get(user.getFreeSong().size() - 1).getDuration() == 0) {
                user.getFreeSong().remove(user.getFreeSong().size() - 1);
            }

            user.updateHistory(user.getMusicPlayer().getSrc());
        }

        output.setMessage("Playback loaded successfully.");
        user.getMusicPlayer().setLastResults(null);
        return output;
    }
}
