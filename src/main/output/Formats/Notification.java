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
public class Notification {
    private String name;
    private String description;

    public Notification(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addToArrayNode(final ArrayNode node, final ObjectMapper objectMapper) {
        ObjectNode element = objectMapper.createObjectNode();
        element.put("name", name);
        element.put("description", description);
        node.add(element);
    }
}
