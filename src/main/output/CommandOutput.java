package main.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.commands.Command;

@Getter
@Setter
@ToString
public class CommandOutput {
    protected String command;
    protected String user;
    protected int timestamp;
    protected String message;

    public CommandOutput() {
    }

    public CommandOutput(final Command command) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
    }

    /**
     * for coding style
     */
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", command);
        node.put("user", user);
        node.put("timestamp", timestamp);
        node.put("message", message);
    }
}
