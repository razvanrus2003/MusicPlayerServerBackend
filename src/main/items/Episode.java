package main.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.filters.Filters;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class Episode extends Item {
    private String description;

    public Episode() {
    }

    public Episode(final EpisodeInput episode) {
        this.name = episode.getName();
        this.duration = episode.getDuration();
        this.description = episode.getDescription();
    }

    /**
     * Convert the input object to an object that the program can work with.
     *
     * @param input input object
     */
    public static ArrayList<Item> getEpisodes(final ArrayList<EpisodeInput> input) {
        ArrayList<Item> episodes = new ArrayList<Item>();
        for (EpisodeInput episode : input) {
            episodes.add(new Episode(episode));
        }
        return episodes;
    }

    @Override
    public boolean validate(final Filters filters) {
        return false;
    }

    @Override
    public ArrayList<Item> getContent() {
        return null;
    }

    @Override
    public void addToObjectNode(final ObjectNode node, final ObjectMapper objectMapper) {
    }

    @Override
    public boolean canDelete() {
        return false;
    }
}
