package main.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.commands.Command;

public final class PrintCurrentPageOutput extends CommandOutput {
    public PrintCurrentPageOutput(final Command command) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
    }

    /**
     * for coding style
     */
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("user", user);
        node.put("command", command);
        node.put("timestamp", timestamp);
        node.put("message", message);
    }
}
