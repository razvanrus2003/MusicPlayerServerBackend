package main.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.Status;
import main.User;
import main.commands.Command;

@Getter
@Setter
@ToString
public final class StatusOutput extends CommandOutput {

    private Status status;

    public StatusOutput(final Command command) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
    }

    @Override
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", command);
        node.put("user", user);
        node.put("timestamp", timestamp);

        User user1 = Library.getInstance().getUser(user);
        if (user1.getMusicPlayer() != null
                && user1.getMusicPlayer().getType().equals("playlists")) {
            if (status.getRepeat().equals("Repeat Once")) {
                status.setRepeat("Repeat All");
            }
            if (status.getRepeat().equals("Repeat Infinite")) {
                status.setRepeat("Repeat Current Song");
            }
        }
        ObjectNode subnode = objectMapper.createObjectNode();
        if (status != null) {
            status.addToObjectNode(subnode, objectMapper);
        }

        node.set("stats", subnode);
        if (user1.getMusicPlayer() != null
                && user1.getMusicPlayer().getType().equals("playlists")) {
            if (status.getRepeat().equals("Repeat All")) {
                status.setRepeat("Repeat Once");
            }
            if (status.getRepeat().equals("Repeat Current Song")) {
                status.setRepeat("Repeat Infinite");
            }
        }
    }

}
