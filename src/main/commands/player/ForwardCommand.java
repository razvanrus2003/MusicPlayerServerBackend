package main.commands.player;

import main.Library;
import main.MusicPlayer;
import main.Status;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

public final class ForwardCommand extends Command {

    public static final int OFFSET = 90;

    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        MusicPlayer musicPlayer = user.getMusicPlayer();
        if (user.getMusicPlayer().getSrc() == null) {
            output.setMessage("Please load a source before attempting to forward.");
            return output;
        }
        user.getMusicPlayer().checkStatus(timestamp);
        if (musicPlayer.getLoadedStatus().getName().isEmpty()) {
            output.setMessage("Please load a source before attempting to forward.");
            return output;
        }
        if (!user.getMusicPlayer().getType().equals("podcasts")) {
            output.setMessage("The loaded source is not a podcast.");
            return output;
        }

        Status loadedStatus = musicPlayer.getLoadedStatus();
        Status srcStatus = musicPlayer.getSrcStatus();
        output.setMessage("Rewound successfully.");
        output.setMessage("Skipped forward successfully.");
        if (user.getMusicPlayer().getLoadedStatus().getRemainedTime() > OFFSET) {
            loadedStatus.setRemainedTime(loadedStatus.getRemainedTime() - OFFSET);
            srcStatus.setRemainedTime(srcStatus.getRemainedTime() - OFFSET);
        } else {
            int remainedTime = srcStatus.getRemainedTime() - loadedStatus.getRemainedTime();
            srcStatus.setRemainedTime(remainedTime);
            loadedStatus.setRemainedTime(0);
        }
        user.getMusicPlayer().checkStatus(timestamp);
        return output;
    }
}
