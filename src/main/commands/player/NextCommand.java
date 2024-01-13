package main.commands.player;

import main.Library;
import main.MusicPlayer;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

public final class NextCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        MusicPlayer musicPlayer = user.getMusicPlayer();
        if (user.getMusicPlayer() == null || user.getMusicPlayer().getSrc() == null) {
            output.setMessage("Please load a source before skipping to the next track.");
            return output;
        }
        user.getMusicPlayer().checkStatus(timestamp);
        if (musicPlayer == null || musicPlayer.getLoadedStatus().getName().isEmpty()) {
            output.setMessage("Please load a source before skipping to the next track.");
            return output;
        }

        user.getMusicPlayer().getLoadedStatus().setRemainedTime(0);
        user.getMusicPlayer().getLoadedStatus().setPaused(false);
        user.getMusicPlayer().getSrcStatus().setPaused(false);
        user.getMusicPlayer().checkStatus(timestamp);

        if (user.getMusicPlayer().getLoadedStatus().getName().isEmpty()) {
            output.setMessage("Please load a source before skipping to the next track.");
        } else {
            String n = user.getMusicPlayer().getLoadedStatus().getName() + ".";
            output.setMessage("Skipped to next track successfully. The current track is " + n);
        }

        return output;
    }
}
