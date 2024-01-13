package main.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.commands.Command;
import main.items.Item;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class ShowAlbumsOutput extends CommandOutput {
    private ArrayList<Item> results;

    public ShowAlbumsOutput(final Command command) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.results = new ArrayList<Item>();
    }

    @Override
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", command);
        node.put("user", user);
        node.put("timestamp", timestamp);

        ArrayNode subnode = objectMapper.createArrayNode();
        if (results != null) {
            for (Item album : results) {
                ObjectNode albumSubnode = objectMapper.createObjectNode();
                album.addToObjectNode(albumSubnode, objectMapper);
                subnode.add(albumSubnode);
            }
        }
        node.set("result", subnode);
    }
}
