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
public final class ShowPlaylistOutput extends CommandOutput {

    private ArrayList<Item> result;

    public ShowPlaylistOutput(final Command command) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.result = new ArrayList<Item>();
    }

    @Override
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", command);
        node.put("user", user);
        node.put("timestamp", timestamp);

        ArrayNode subnode = objectMapper.createArrayNode();
        if (result != null) {
            for (Item playlist : result) {
                ObjectNode playListSubnode = objectMapper.createObjectNode();
                playlist.addToObjectNode(playListSubnode, objectMapper);
                subnode.add(playListSubnode);
            }
        }

        node.set("result", subnode);
    }

}
