package main.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.PodcastInput;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.MusicPlayer;
import main.Status;
import main.User;
import main.commands.host.AddPodcastCommand;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public final class Podcast extends Collection {
    private final ArrayList<Item> episodes;

    public Podcast(final PodcastInput podcast) {
        this.name = podcast.getName();
        this.owner = podcast.getOwner();
        this.episodes = Episode.getEpisodes(podcast.getEpisodes());
        for (Item episode : episodes) {
            this.duration += episode.getDuration();
        }
    }

    public Podcast(AddPodcastCommand command) {
        this.name = command.getName();
        this.owner = command.getUsername();
        this.episodes = new ArrayList<>();
        for (Episode episode : command.getEpisodes()) {
            this.duration += episode.getDuration();
            this.episodes.add(episode);
        }
    }

    /**
     * Convert the input object to an object that the program can work with.
     *
     * @param input input object
     */
    public static ArrayList<Podcast> getPodcasts(final ArrayList<PodcastInput> input) {
        ArrayList<Podcast> podcasts = new ArrayList<Podcast>();
        for (PodcastInput podcast : input) {
            podcasts.add(new Podcast(podcast));
        }
        return podcasts;
    }

    @Override
    public ArrayList<Item> getContent() {
        return episodes;
    }

    @Override
    public void addToObjectNode(ObjectNode node, ObjectMapper objectMapper) {
        node.put("name", name);
        ArrayNode subnode = objectMapper.createArrayNode();
        for (Item epidode : episodes) {
            subnode.add(epidode.getName());
        }
        node.set("episodes", subnode);

    }

    /**
     * for coding style
     */
    @Override
    public Item next(final MusicPlayer musicPlayer, final int timestamp) {
        Status srcStatus = musicPlayer.getSrcStatus();
        Status loadedStatus = musicPlayer.getLoadedStatus();

        if (srcStatus.getRepeat().equals("No Repeat")) {
            if (srcStatus.checkPlayStatus(timestamp)) {
                loadedStatus.clearStatus();
                return null;
            }
            int pos = loadedStatus.getPosition();
            Item curr = episodes.get(pos);

            while (loadedStatus.getRemainedTime() + loadedStatus.getPlayingSince() <= timestamp) {
                pos++;
                curr = episodes.get(pos);
                loadedStatus.setName(curr.getName());
                loadedStatus.setPlayingSince(loadedStatus.getPlayingSince()
                        + loadedStatus.getRemainedTime());
                loadedStatus.setRemainedTime(curr.getDuration());
            }
            loadedStatus.setRemainedTime(loadedStatus.getPlayingSince()
                    + loadedStatus.getRemainedTime()
                    - timestamp);
            loadedStatus.setPlayingSince(timestamp);
            loadedStatus.setPosition(pos);
            return curr;
        }
        if (srcStatus.getRepeat().equals("Repeat Once")) {
            if (srcStatus.checkPlayStatus(timestamp)) {
                loadedStatus.setRepeat("No repeat");
                srcStatus.setRepeat("No repeat");

                srcStatus.setPlayingSince(srcStatus.getPlayingSince()
                        + srcStatus.getRemainedTime());
                srcStatus.setRemainedTime(duration);
                loadedStatus.setRemainedTime(episodes.get(0).getDuration());
                loadedStatus.setPlayingSince(srcStatus.getPlayingSince());
                return next(musicPlayer, timestamp);
            }
            int pos = loadedStatus.getPosition();
            Item curr = episodes.get(pos);

            while (loadedStatus.getRemainedTime() + loadedStatus.getPlayingSince() <= timestamp) {
                pos++;
                curr = episodes.get(pos);
                loadedStatus.setName(curr.getName());
                loadedStatus.setPlayingSince(loadedStatus.getPlayingSince()
                        + loadedStatus.getRemainedTime());
                loadedStatus.setRemainedTime(curr.getDuration());
            }
            loadedStatus.setRemainedTime(loadedStatus.getPlayingSince()
                    + loadedStatus.getRemainedTime()
                    - timestamp);
            loadedStatus.setPlayingSince(timestamp);
            loadedStatus.setPosition(pos);
            return curr;
        }
        int pos = loadedStatus.getPosition();
        Item curr = episodes.get(pos);

        while (loadedStatus.getRemainedTime() + loadedStatus.getPlayingSince() <= timestamp) {
            pos++;
            if (pos == episodes.size()) {
                pos = 0;
            }
            curr = episodes.get(pos);
            loadedStatus.setName(curr.getName());
            loadedStatus.setPlayingSince(loadedStatus.getPlayingSince()
                    + loadedStatus.getRemainedTime());
            loadedStatus.setRemainedTime(curr.getDuration());
        }
        loadedStatus.setRemainedTime(loadedStatus.getPlayingSince()
                + loadedStatus.getRemainedTime()
                - timestamp);
        loadedStatus.setPlayingSince(timestamp);
        loadedStatus.setPosition(pos);
        return curr;
    }

    @Override
    public Item prev(final Status srcStatus, final Status loadedStatus, final int timestamp) {
        if (loadedStatus.getPosition() == 0) {
            srcStatus.setPaused(false);
            srcStatus.setRemainedTime(duration);
            srcStatus.setPlayingSince(timestamp);

            loadedStatus.setPaused(false);
            loadedStatus.setRemainedTime(episodes.get(0).getDuration());
            loadedStatus.setPlayingSince(timestamp);
            return episodes.get(0);
        }
        Item prev = episodes.get(loadedStatus.getPosition() - 1);

        srcStatus.setRemainedTime(srcStatus.getRemainedTime()
                + loadedStatus.getRemainedTime()
                - episodes.get(loadedStatus.getPosition()).getDuration()
                - prev.getDuration());
        srcStatus.setPlayingSince(timestamp);

        loadedStatus.setName(prev.getName());
        loadedStatus.setRemainedTime(prev.getDuration());
        loadedStatus.setPlayingSince(timestamp);
        srcStatus.setPaused(false);
        loadedStatus.setPaused(false);
        return prev;
    }

    @Override
    public boolean canDelete() {
        Library library = Library.getInstance();

        for (User user : library.getUsers()) {
            if (user.getMusicPlayer().getLastResults() != null
                    && user.getMusicPlayer().getLastResults().contains(this)) {

                return false;
            }

            if (user.getMusicPlayer().getSrc() != null
                    && user.getMusicPlayer().getSrc().getName().equals(name)
                    && user.getMusicPlayer().getLoadedStatus().getRemainedTime() > 0
            ) {

                return false;
            }
        }
        return true;
    }
}
