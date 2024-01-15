package main.commands.recommendation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.Library;
import main.User;
import main.commands.Command;
import main.items.Item;
import main.items.Playlist;
import main.items.Song;
import main.output.CommandOutput;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public final class UpdateRecommendationsCommand extends Command {
    private String recommendationType;
    private static final int FIVE = 5;
    private static final int THREE = 3;

    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getInstance().getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }
        if (user.getMusicPlayer() != null
                && user.getMusicPlayer().getLoaded() != null && user.isOnline()) {
            user.getMusicPlayer().checkStatus(timestamp);
        }
        if (Library.getInstance().getArtists().contains(user)
                || Library.getInstance().getHosts().contains(user)) {
            output.setMessage(username + " is not a normal user.");
            return output;
        }
        if (recommendationType.equals("random_song")) {
            if (user.getMusicPlayer().getLoaded() == null) {
                output.setMessage("No new recommendations were found");
                return output;
            }
            String genre = ((Song) user.getMusicPlayer().getLoaded()).getGenre();
            int seed = user.getMusicPlayer().getLoaded().getDuration()
                    - user.getMusicPlayer().getLoadedStatus().getRemainedTime();
            ArrayList<Item> songs = new ArrayList<>(Library.getInstance().getSongs().stream()
                    .filter(song -> song.getGenre().equals(genre))
                    .toList());
            Random random = new Random(seed);

            user.getRecommendation().add(songs.get(random.nextInt(songs.size())));
            user.getRecommendationType().add("song");
        } else if (recommendationType.equals("random_playlist")) {
            ArrayList<Item> likedSongs = new ArrayList<>(user.getLikedSongs().stream().toList());
            likedSongs.addAll(new ArrayList<>(user.getPlaylists().stream()
                    .map(Item::getContent)
                    .flatMap(List::stream)
                    .toList()));
            likedSongs.addAll(new ArrayList<>(user.getFollowedPlayLists().stream()
                    .map(Item::getContent)
                    .flatMap(List::stream)
                    .toList()));
            ArrayList<String> topGenres = new ArrayList<>(likedSongs.stream()
                    .map(song -> ((Song) song).getGenre())
                    .collect(Collectors.groupingBy(
                            s -> s,
                            Collectors.mapping(s -> 1, Collectors.toList())))
                    .entrySet()
                    .stream().map(entry -> new AbstractMap.SimpleEntry<String, Integer>(
                            entry.getKey(),
                            entry.getValue().stream().reduce(0, Integer::sum)))
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .toList());
            int i = THREE;
            Playlist playlist = new Playlist();
            for (String genre : topGenres) {
                ArrayList<Item> songs = new ArrayList<>(Library.getInstance().getSongs().stream()
                        .filter(song -> song.getGenre().equals(genre))
                        .sorted(Comparator.comparing(Song::getLikes).reversed())
                        .toList());
                if (i == THREE) {
                    for (int j = 0; j < FIVE && j < songs.size(); j++) {
                        playlist.getSongs().add(songs.get(j));
                    }
                }
                if (i == 2) {
                    for (int j = 0; j < THREE && j < songs.size(); j++) {
                        playlist.getSongs().add(songs.get(j));
                    }
                }
                if (i == 1) {
                    for (int j = 0; j < 2 && j < songs.size(); j++) {
                        playlist.getSongs().add(songs.get(j));
                    }
                    break;
                }
                i--;
            }

            if (playlist.getSongs().isEmpty()) {
                output.setMessage("No new recommendations were found");
                return output;
            }
            playlist.setName(username + "'s recommendations");
            user.getRecommendation().add(playlist);
            user.getRecommendationType().add("playlist");
        } else if (recommendationType.equals("fans_playlist")) {
            for (User user1 : Library.getInstance().getUsers()) {
                if (user1.getMusicPlayer() != null && user1.getMusicPlayer().getLoaded() != null
                        && user1.isOnline() && user1 != user) {
                    user1.getMusicPlayer().checkStatus(timestamp);
                }
            }
            Comparator<Map.Entry<String, Integer>> comparator =
                    Comparator.comparing(Map.Entry::getValue);
            comparator = comparator.reversed().thenComparing(Map.Entry.comparingByKey());

            User artist = Library.getInstance().getUser(
                    ((Song) user.getMusicPlayer().getLoaded()).getArtist());
            ArrayList<String> topFans = new ArrayList<>(
                    Library.getInstance().getUsers().stream()
                            .map(user1 -> user1.getArtistHistory().entrySet()
                                    .stream()
                                    .filter(entry -> entry.getKey().equals(artist.getUsername()))
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
            int i = FIVE;
            Playlist playlist = new Playlist();
            for (String fan : topFans) {
                ArrayList<Item> songs = new ArrayList<>(Library.getInstance().getUser(
                        fan).getLikedSongs()
                        .stream()
                        .sorted(Comparator.comparing(song -> ((Song) song).getLikes()).reversed())
                        .toList());

                for (int j = 0; j < FIVE && j < songs.size(); j++) {
                    playlist.getSongs().add(songs.get(j));
                }

                if (i == 1) {
                    break;
                }
                i--;
            }
            if (playlist.getSongs().isEmpty()) {
                output.setMessage("No new recommendations were found");
                return output;
            }
            playlist.setName(artist.getUsername() + " Fan Club recommendations");
            user.getRecommendation().add(playlist);
            user.getRecommendationType().add("playlist");
        }
        output.setMessage("The recommendations for user "
                + username
                + " have been updated successfully.");
        return output;
    }
}
