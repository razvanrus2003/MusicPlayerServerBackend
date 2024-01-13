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
@Setter
@ToString
public final class GetTop5ArtistOutput extends CommandOutput {
    private ArrayList<String> results;

    public GetTop5ArtistOutput(final Command command) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.results = new ArrayList<String>();
    }

    @Override
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", command);
        node.put("timestamp", timestamp);

        ArrayNode row = objectMapper.createArrayNode();
        for (String item : results) {
            row.add(item);
        }
        node.set("result", row);
    }
}
