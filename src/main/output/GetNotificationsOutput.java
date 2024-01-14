package main.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.commands.Command;
import main.output.Formats.Notification;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class GetNotificationsOutput extends CommandOutput {
    ArrayList<Notification> notifications = new ArrayList<>();

    public GetNotificationsOutput(final Command command) {
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
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (Notification notification : notifications) {
            notification.addToArrayNode(arrayNode, objectMapper);
        }
        node.put("notifications", arrayNode);
    }
}
