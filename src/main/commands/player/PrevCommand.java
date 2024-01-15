package main.commands.player;

import main.Library;
import main.MusicPlayer;
import main.Status;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

public final class PrevCommand extends Command {
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);
        MusicPlayer musicPlayer = user.getMusicPlayer();
        if (user.getMusicPlayer() == null || user.getMusicPlayer().getSrc() == null) {
            output.setMessage("Please load a source before returning to the previous track.");
            return output;
        }
        user.getMusicPlayer().checkStatus(timestamp);
        if (musicPlayer == null || musicPlayer.getLoadedStatus().getName().isEmpty()) {
            output.setMessage("Please load a source before returning to the previous track.");
            return output;
        }
        Status loadedStatus = musicPlayer.getLoadedStatus();
        Status srcStatus = musicPlayer.getSrcStatus();

        if (user.getMusicPlayer().getLoaded().getDuration() > loadedStatus.getRemainedTime()) {
            srcStatus.setRemainedTime((srcStatus.getRemainedTime()
                    - loadedStatus.getRemainedTime()
                    + user.getMusicPlayer().getLoaded().getDuration()));
            loadedStatus.setRemainedTime(user.getMusicPlayer().getLoaded().getDuration());
        } else if (!user.getMusicPlayer().getType().equals("songs")) {
            musicPlayer.setLoaded(musicPlayer.getSrc().prev(srcStatus, loadedStatus, timestamp));
        }

        if (user.getMusicPlayer().getLoadedStatus().getName().isEmpty()) {
            output.setMessage("Please load a source before skipping to the next track.");
        } else {
            String msg = " The current track is "
                    + user.getMusicPlayer().getLoadedStatus().getName()
                    + ".";
            output.setMessage("Returned to previous track successfully." + msg);
        }
        user.getMusicPlayer().getLoadedStatus().setPaused(false);
        user.getMusicPlayer().getSrcStatus().setPaused(false);
        return output;
    }
}
