package main.commands.player;

import main.Library;
import main.MusicPlayer;
import main.Status;
import main.User;
import main.commands.Command;
import main.output.CommandOutput;

public final class BackwardCommand extends Command {
    public static final int OFFSET = 90;

    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        MusicPlayer musicPlayer = user.getMusicPlayer();
        if (user.getMusicPlayer().getSrc() == null) {
            output.setMessage("Please select a source before rewinding.");
            return output;
        }
        user.getMusicPlayer().checkStatus(timestamp);
        if (user.getMusicPlayer().getLoadedStatus().getName().isEmpty()) {
            output.setMessage("Please select a source before rewinding.");
            return output;
        }
        if (!user.getMusicPlayer().getType().equals("podcasts")) {
            output.setMessage("The loaded source is not a podcast.");
            return output;
        }

        Status loadedStatus = musicPlayer.getLoadedStatus();
        Status srcStatus = musicPlayer.getSrcStatus();
        output.setMessage("Rewound successfully.");
        if (musicPlayer.getLoaded().getDuration() - loadedStatus.getRemainedTime() >= OFFSET) {
            loadedStatus.setRemainedTime(loadedStatus.getRemainedTime() + OFFSET);
            srcStatus.setRemainedTime(musicPlayer.getSrcStatus().getRemainedTime() + OFFSET);
        } else {
            int remainedTime = user.getMusicPlayer().getSrcStatus().getRemainedTime()
                    + user.getMusicPlayer().getLoaded().getDuration()
                    - user.getMusicPlayer().getLoadedStatus().getRemainedTime();
            srcStatus.setRemainedTime(remainedTime);
            loadedStatus.setRemainedTime(user.getMusicPlayer().getLoaded().getDuration());
        }
        user.getMusicPlayer().checkStatus(timestamp);

        return output;
    }
}
