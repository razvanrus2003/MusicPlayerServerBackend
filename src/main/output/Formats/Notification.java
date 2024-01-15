package main.output.Formats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * Notification class
 * Contains the name and description of a notification
 */
public class Notification {
    private String name;
    private String description;

    /**
     * Constructor for the notification class
     * @param name the name of the notification
     * @param description the description of the notification
     */
    public Notification(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
    /**
     * function that adds a notification to the array node
     * @param node the array node to be added to
     * @param objectMapper the object mapper used to create the object node
     */
    public void addToArrayNode(final ArrayNode node, final ObjectMapper objectMapper) {
        ObjectNode element = objectMapper.createObjectNode();
        element.put("name", name);
        element.put("description", description);
        node.add(element);
    }
}
