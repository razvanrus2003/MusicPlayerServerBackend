package main.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.commands.Command;
import main.output.WrappedFormat.WrappedStat;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.Math.min;

@Getter
@Setter
@ToString
public class WrappedOutput extends CommandOutput {
    private WrappedStat results;

    public WrappedOutput(final Command command) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.results = new WrappedStat();
        this.results.setPairStats(new ArrayList<>());
    }

    @Override
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
        node.put("command", command);
        node.put("user", user);
        node.put("timestamp", timestamp);

        ObjectNode fresults = objectMapper.createObjectNode();

        if (results.getPairStats() == null) {
            node.put("message", "No data to show for " + Library.getUser(user).getType().toLowerCase() + " " + user + ".");
            return;
        }

        for (int j = 1; j < results.getPairStats().size(); j ++) {
            ObjectNode element = objectMapper.createObjectNode();
            for (int i = 0; i < min(results.getPairStats().get(j).size(), 5); i++) {
                element.put(results.getPairStats().get(j).get(i).getKey(),
                        results.getPairStats().get(j).get(i).getValue());
            }
            fresults.set(results.getPairStats().get(0).get(j - 1).getKey(), element);
        }
        if (results.getFans() != null) {
            ArrayNode fans = objectMapper.createArrayNode();
            int i = 0;
            for (String fan : results.getFans()) {
                fans.add(fan);
                i++;
                if (i == 5) {
                    break;
                }
            }
            fresults.set("topFans", fans);
        }
        if (results.getListeners() != null) {
            fresults.put("listeners", results.getListeners());
        }
        node.set("result", fresults);
    }
}
