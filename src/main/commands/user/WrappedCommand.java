package main.commands.user;

import main.Library;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.output.CommandOutput;
import main.output.Formats.WrappedStat;
import main.output.WrappedOutput;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WrappedCommand extends Command {
    private static final int FIVE = 5;
    /**
     * Return the top artists, genres, songs, albums and episodes of a user
     *
     * @return wrapped results for a user
     */
    private ArrayList<ArrayList<Map.Entry<String, Integer>>> userResult(final User user) {
        ArrayList<ArrayList<Map.Entry<String, Integer>>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.get(0).add(new AbstractMap.SimpleEntry<>("topArtists", 1));
        result.get(0).add(new AbstractMap.SimpleEntry<>("topGenres", 1));
        result.get(0).add(new AbstractMap.SimpleEntry<>("topSongs", 1));
        result.get(0).add(new AbstractMap.SimpleEntry<>("topAlbums", 1));
        result.get(0).add(new AbstractMap.SimpleEntry<>("topEpisodes", 1));


        Comparator<Map.Entry<String, Integer>> comparator =
                Comparator.comparing(Map.Entry::getValue);
        comparator = comparator.reversed().thenComparing(Map.Entry.comparingByKey());

        ArrayList<Map.Entry<String, Integer>> topArtists = new ArrayList<>(
                user.getSongHistory().entrySet()
                        .stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getKey().getArtist(),
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().stream().reduce(0, Integer::sum)))
                        .entrySet()
                        .stream()
                        .sorted(comparator)
                        .toList());

        ArrayList<Map.Entry<String, Integer>> topGenres = new ArrayList<>(
                user.getSongHistory().entrySet()
                        .stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getKey().getGenre(),
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().stream().reduce(0, Integer::sum)))
                        .entrySet()
                        .stream()
                        .sorted(comparator)
                        .toList());


        ArrayList<Map.Entry<String, Integer>> topSongs = new ArrayList<>(
                user.getSongHistory().entrySet()
                        .stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getKey().getName(),
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().stream().reduce(0, Integer::sum)))
                        .entrySet()
                        .stream()
                        .sorted(comparator)
                        .toList());

        ArrayList<Map.Entry<String, Integer>> topAlbums = new ArrayList<>(
                user.getSongHistory().entrySet()
                        .stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getKey().getAlbum(),
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().stream().reduce(0, Integer::sum)))
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue() != 0)
                        .sorted(comparator)
                        .toList());

        ArrayList<Map.Entry<String, Integer>> topEpisodes = new ArrayList<>(
                user.getEpisodeHistory().entrySet()
                        .stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getKey().getName(),
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().stream().reduce(0, Integer::sum)))
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue() != 0)
                        .sorted(comparator)
                        .toList()
        );

        result.add(topArtists);
        result.add(topGenres);
        result.add(topSongs);
        result.add(topAlbums);
        result.add(topEpisodes);
        if (result.get(1).isEmpty() && result.get(FIVE).isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     * Returns the top albums and songs of an artist
     *
     * @return top albums and songs
     */
    private ArrayList<ArrayList<Map.Entry<String, Integer>>> artistResult(final User artist) {
        ArrayList<ArrayList<Map.Entry<String, Integer>>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.get(0).add(new AbstractMap.SimpleEntry<>("topAlbums", 1));
        result.get(0).add(new AbstractMap.SimpleEntry<>("topSongs", 1));

        Comparator<Map.Entry<String, Integer>> comparator =
                Comparator.comparing(Map.Entry::getValue);
        comparator = comparator.reversed().thenComparing(Map.Entry.comparingByKey());

        ArrayList<Map.Entry<String, Integer>> topAlbums = new ArrayList<>(
                Library.getInstance().getUsers().stream()
                        .map(user -> user.getSongHistory().entrySet())
                        .flatMap(Set::stream)
                        .filter(entry -> entry.getKey().getArtist().equals(artist.getUsername()))
                        .collect(Collectors.groupingBy(
                                entry -> entry.getKey().getAlbum(),
                                Collectors.mapping(
                                        entry -> entry.getValue(),
                                        Collectors.toList()
                                )
                        ))
                        .entrySet()
                        .stream()
                        .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(
                                entry.getKey(),
                                entry.getValue().stream().reduce(0, Integer::sum)))
                        .sorted(comparator)
                        .toList()
        );

        ArrayList<Map.Entry<String, Integer>> topSongs = new ArrayList<>(
                Library.getInstance().getUsers().stream()
                        .map(user -> user.getSongHistory().entrySet())
                        .flatMap(Set::stream)
                        .filter(entry -> entry.getKey().getArtist().equals(artist.getUsername()))
                        .collect(Collectors.groupingBy(
                                entry -> entry.getKey().getName(),
                                Collectors.mapping(
                                        entry -> entry.getValue(),
                                        Collectors.toList()
                                )
                        ))
                        .entrySet()
                        .stream()
                        .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(
                                entry.getKey(),
                                entry.getValue().stream().reduce(0, Integer::sum)))
                        .sorted(comparator)
                        .toList()
        );

        result.add(topAlbums);
        result.add(topSongs);
        return result;
    }

    /**
     * Returns the top listeners of an artist
     *
     * @return top listeners
     */
    private ArrayList<String> getListenres(final User artists) {
        Comparator<Map.Entry<String, Integer>> comparator =
                Comparator.comparing(Map.Entry::getValue);
        comparator = comparator.reversed().thenComparing(Map.Entry.comparingByKey());

        return new ArrayList<>(
                Library.getInstance().getUsers().stream()
                        .map(user -> user.getArtistHistory().entrySet()
                                .stream()
                                .filter(entry -> entry.getKey().equals(artists.getUsername()))
                                .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(
                                        user.getUsername(),
                                        entry.getValue()) {
                                })
                                .findFirst()
                                .orElse(null))
                        .filter(Objects::nonNull)
                        .sorted(comparator)
                        .map(Map.Entry::getKey)
                        .toList()
        );
    }

    /**
     * Returns the top episodes of a host
     *
     * @return top episodes
     */
    private ArrayList<ArrayList<Map.Entry<String, Integer>>> getTopEpisodes(final User host) {
        ArrayList<ArrayList<Map.Entry<String, Integer>>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.get(0).add(new AbstractMap.SimpleEntry<>("topEpisodes", 1));

        Comparator<Map.Entry<String, Integer>> comparator =
                Comparator.comparing(Map.Entry::getValue);
        comparator = comparator.reversed().thenComparing(Map.Entry.comparingByKey());

        ArrayList<Map.Entry<String, Integer>> topEpisodes = new ArrayList<>(
                host.getPlaylists()
                        .stream()
                        .map(Item::getContent)
                        .flatMap(List::stream)
                        .filter(item -> item.getListens() > 0)
                        .collect(Collectors.toMap(Item::getName, Item::getListens))
                        .entrySet()
                        .stream()
                        .sorted(comparator)
                        .toList()
        );

        result.add(topEpisodes);
        return result;
    }

    /**
     * @return the output of the command
     */
    @Override
    public CommandOutput execute() {
        WrappedOutput output = new WrappedOutput(this);

        User user = Library.getInstance().getUser(username);
        if (user.getMusicPlayer() != null
                && user.getMusicPlayer().getLoaded() != null
                && user.isOnline()) {
            user.getMusicPlayer().checkStatus(timestamp);
        }
        WrappedStat stats = new WrappedStat();

        if (user.getType().equals("user")) {
            stats.setPairStats(userResult(user));
        } else if (user.getType().equals("artist")) {
            for (User user1 : Library.getInstance().getUsers()) {
                if (user1.getMusicPlayer() != null
                        && user1.getMusicPlayer().getLoaded() != null && user1.isOnline()
                        && user1 != user) {
                    user1.getMusicPlayer().checkStatus(timestamp);
                }
            }
            stats.setPairStats(artistResult(user));
            stats.setFans(getListenres(user));
            stats.setListeners(stats.getFans().size());
            if (stats.getPairStats().get(1).isEmpty() && stats.getListeners() == 0) {
                stats.setPairStats(null);
            }
        } else if (user.getType().equals("host")) {
            for (User user1 : Library.getInstance().getUsers()) {
                if (user1.getMusicPlayer() != null
                        && user1.getMusicPlayer().getLoaded() != null
                        && user1.isOnline() && user1 != user) {
                    user1.getMusicPlayer().checkStatus(timestamp);
                }
            }
            stats.setPairStats(getTopEpisodes(user));
            stats.setListeners(getListenres(user).size());
        }

        output.setResults(stats);
        return output;
    }
}
