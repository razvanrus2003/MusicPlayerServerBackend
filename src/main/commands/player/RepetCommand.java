package main.commands.player;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

public final class RepetCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);

        if (user.getMusicPlayer() == null
                || user.getMusicPlayer().getLoaded() == null) {
            output.setMessage("Please load a source before setting the repeat status.");
            return output;
        }

        user.getMusicPlayer().checkStatus(timestamp);
        if (user.getMusicPlayer().getLoadedStatus().getName().isEmpty()) {
            output.setMessage("Please load a source before setting the repeat status.");
            return output;
        }
        boolean playlist = user.getMusicPlayer().getType().equals("playlists")
                || user.getMusicPlayer().getType().equals("albums");
        if (user.getMusicPlayer().getSrcStatus().getRepeat().equals("No Repeat")) {
            if (playlist) {
                output.setMessage("Repeat mode changed to repeat all.");
            } else {
                output.setMessage("Repeat mode changed to repeat once.");
            }
            user.getMusicPlayer().getSrcStatus().setRepeat("Repeat Once");
            user.getMusicPlayer().getLoadedStatus().setRepeat("Repeat Once");
        } else if (user.getMusicPlayer().getSrcStatus().getRepeat().equals("Repeat Once")) {
            if (playlist) {
                output.setMessage("Repeat mode changed to repeat current song.");
            } else {
                output.setMessage("Repeat mode changed to repeat infinite.");
            }
            user.getMusicPlayer().getSrcStatus().setRepeat("Repeat Infinite");
            user.getMusicPlayer().getLoadedStatus().setRepeat("Repeat Infinite");

        } else {
            output.setMessage("Repeat mode changed to no repeat.");

            user.getMusicPlayer().getSrcStatus().setRepeat("No Repeat");
            user.getMusicPlayer().getLoadedStatus().setRepeat("No Repeat");
        }
        return output;
    }
}
