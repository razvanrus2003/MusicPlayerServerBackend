package main.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.commands.Command;

import java.util.ArrayList;

@Getter
@ToString
@Setter
public final class SearchOutput extends CommandOutput {
    private ArrayList<String> results;

    public SearchOutput(final Command command) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.results = new ArrayList<String>();
    }

    @Override
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", command);
        node.put("user", user);
        node.put("timestamp", timestamp);
        node.put("message", message);

        ArrayNode row = objectMapper.createArrayNode();
        for (String item : results) {
            row.add(item);
        }
        node.set("results", row);
    }

}
