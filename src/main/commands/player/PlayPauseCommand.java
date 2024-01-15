package main.commands.player;

import main.Library;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

public final class PlayPauseCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);

        if (user.getMusicPlayer() == null || user.getMusicPlayer().getLoaded() == null) {
            String msg = "Please load a source before attempting to pause or resume playback.";
            output.setMessage(msg);
            return output;
        }
        user.getMusicPlayer().checkStatus(timestamp);

        if (user.getMusicPlayer().getLoadedStatus().getName().isEmpty()) {
            String msg = "Please load a source before attempting to pause or resume playback.";
            output.setMessage(msg);
            return output;
        }
        if (user.getMusicPlayer().getSrcStatus().isPaused()) {
            user.getMusicPlayer().updateStatus(false);
            output.setMessage("Playback resumed successfully.");
        } else {
            user.getMusicPlayer().updateStatus(true);
            output.setMessage("Playback paused successfully.");
        }

        return output;
    }
}
