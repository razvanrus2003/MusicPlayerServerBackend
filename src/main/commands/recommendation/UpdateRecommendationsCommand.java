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
import org.checkerframework.checker.index.qual.LTEqLengthOf;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class UpdateRecommendationsCommand extends Command {
    private String recommendationType;
    @Override
    public CommandOutput execute() {
        CommandOutput output = new CommandOutput(this);

        User user = Library.getUser(username);
        if (user == null) {
            output.setMessage("The username " + username + " doesn't exist.");
            return output;
        }

        if (user.getMusicPlayer() != null && user.getMusicPlayer().getLoaded() != null && user.isOnline()) {
            user.getMusicPlayer().checkStatus(timestamp);
        }

        if (Library.getInstance().getArtists().contains(user) || Library.getInstance().getHosts().contains(user)) {
            output.setMessage(username + " is not a normal user.");
            return output;
        }
//        if  (user.getMusicPlayer().getLoaded() != null)
//            System.out.println(user.getMusicPlayer().getLoaded().getName());

        if (recommendationType.equals("random_song")) {
            if  (user.getMusicPlayer().getLoaded() == null
//                    || user.getMusicPlayer().getLoadedStatus().getRemainedTime() + 30
//                    <= user.getMusicPlayer().getLoaded().getDuration()
            ) {
                output.setMessage("No new recommendations were found");
                return output;
            }

            String genre = ((Song) user.getMusicPlayer().getLoaded()).getGenre();
            int seed =  user.getMusicPlayer().getLoaded().getDuration()
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
                    .map(song -> ((Song)song).getGenre())
                    .collect(Collectors.groupingBy(
                            s -> s,
                            Collectors.mapping(s-> 1, Collectors.toList())))
                    .entrySet()
                    .stream().map(entry -> new AbstractMap.SimpleEntry<String, Integer>(
                            entry.getKey(),
                            entry.getValue().stream().reduce(0, Integer::sum)))
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .toList());
            int i = 3;
            Playlist playlist = new Playlist();
            for (String genre : topGenres) {
                ArrayList<Item> songs = new ArrayList<>(Library.getInstance().getSongs().stream()
                        .filter(song -> song.getGenre().equals(genre))
                        .sorted(Comparator.comparing(Song::getLikes).reversed())
                        .toList());
                if (i == 3)
                    for (int j = 0; j < 5 && j < songs.size(); j++) {
                        playlist.getSongs().add(songs.get(j));
                    }
                if (i == 2)
                    for (int j = 0; j < 3 && j < songs.size(); j++) {
                        playlist.getSongs().add(songs.get(j));
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
                if (user1.getMusicPlayer() != null && user1.getMusicPlayer().getLoaded() != null && user1.isOnline() && user1 != user) {
                    user1.getMusicPlayer().checkStatus(timestamp);
                }
            }
            Comparator<Map.Entry<String, Integer>> comparator = Comparator.comparing(Map.Entry::getValue);
            comparator = comparator.reversed().thenComparing(Map.Entry.comparingByKey());

            User artist = Library.getUser(((Song)user.getMusicPlayer().getLoaded()).getArtist());
            ArrayList<String> topFans = new ArrayList<>(
                    Library.getInstance().getUsers().stream()
                            .map(user1 -> user1.getArtistHistory().entrySet()
                                    .stream()
                                    .filter(entry -> entry.getKey().equals(artist.getUsername()))
                                    .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(
                                            user.getUsername(),
                                            entry.getValue()) {})
                                    .findFirst()
                                    .orElse(null))
                            .filter(Objects::nonNull)
                            .sorted(comparator)
                            .map(Map.Entry::getKey)
                            .toList()
            );

            int i = 5;
            Playlist playlist = new Playlist();
            for (String fan : topFans) {
                ArrayList<Item> songs = new ArrayList<>(Library.getUser(fan).getLikedSongs()
                        .stream()
                        .sorted(Comparator.comparing(song -> ((Song)song).getLikes()).reversed())
                        .toList());

                for (int j = 0; j < 5 && j < songs.size(); j++) {
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
            playlist.setName(artist.getUsername() +" Fan Club recommendations");
            user.getRecommendation().add(playlist);
            user.getRecommendationType().add("playlist");
        }

        output.setMessage("The recommendations for user " + username + " have been updated successfully.");
        return output;
    }
}
