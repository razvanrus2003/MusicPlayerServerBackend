package main.commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.commands.admin.AddUserCommand;
import main.commands.admin.DeleteUserCommand;
import main.commands.admin.ShowAlbumsCommand;
import main.commands.admin.ShowPodcastsCommand;
import main.commands.artist.AddAlbumCommand;
import main.commands.artist.AddMerchCommand;
import main.commands.artist.AddEventCommand;
import main.commands.artist.RemoveAlbumCommand;
import main.commands.artist.RemoveEventCommand;
import main.commands.host.AddAnnouncementCommand;
import main.commands.host.AddPodcastCommand;
import main.commands.host.RemoveAnnouncementCommand;
import main.commands.host.RemovePodcastCommand;
import main.commands.page.ChangePageCommand;
import main.commands.page.PrintCurrentPageCommand;
import main.commands.player.NextCommand;
import main.commands.player.PrevCommand;
import main.commands.player.PlayPauseCommand;
import main.commands.player.RepetCommand;
import main.commands.player.ShuffleCommand;
import main.commands.player.ForwardCommand;
import main.commands.player.BackwardCommand;
import main.commands.playlist.CreatePlaylistCommand;
import main.commands.playlist.FollowPlaylistCommand;
import main.commands.playlist.ShowPlaylistsCommand;
import main.commands.playlist.SwitchVisibilityCommand;
import main.commands.stats.GetAllUsersCommand;
import main.commands.stats.GetOnlineUsersCommand;
import main.commands.stats.GetTop5ArtistsCommand;
import main.commands.stats.GetTop5AlbumsCommand;
import main.commands.stats.GetTop5PlaylistsCommand;
import main.commands.stats.GetTop5SongsCommand;
import main.commands.stats.ShowPreferredSongsCommand;
import main.commands.user.*;
import main.output.CommandOutput;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        visible = true,
        include = JsonTypeInfo.As.PROPERTY,
        property = "command")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SelectCommand.class, name = "select"),
        @JsonSubTypes.Type(value = SearchCommand.class, name = "search"),
        @JsonSubTypes.Type(value = StatusCommand.class, name = "status"),
        @JsonSubTypes.Type(value = PlayPauseCommand.class, name = "playPause"),
        @JsonSubTypes.Type(value = LoadCommand.class, name = "load"),
        @JsonSubTypes.Type(value = RepetCommand.class, name = "repeat"),
        @JsonSubTypes.Type(value = ShuffleCommand.class, name = "shuffle"),
        @JsonSubTypes.Type(value = ForwardCommand.class, name = "forward"),
        @JsonSubTypes.Type(value = BackwardCommand.class, name = "backward"),
        @JsonSubTypes.Type(value = LikeCommand.class, name = "like"),
        @JsonSubTypes.Type(value = NextCommand.class, name = "next"),
        @JsonSubTypes.Type(value = PrevCommand.class, name = "prev"),
        @JsonSubTypes.Type(value = AddRemoveInPlaylistCommand.class, name = "addRemoveInPlaylist"),
        @JsonSubTypes.Type(value = CreatePlaylistCommand.class, name = "createPlaylist"),
        @JsonSubTypes.Type(value = SwitchVisibilityCommand.class, name = "switchVisibility"),
        @JsonSubTypes.Type(value = FollowPlaylistCommand.class, name = "follow"),
        @JsonSubTypes.Type(value = ShowPlaylistsCommand.class, name = "showPlaylists"),
        @JsonSubTypes.Type(value = ShowPreferredSongsCommand.class, name = "showPreferredSongs"),
        @JsonSubTypes.Type(value = GetTop5SongsCommand.class, name = "getTop5Songs"),
        @JsonSubTypes.Type(value = GetTop5PlaylistsCommand.class, name = "getTop5Playlists"),
        @JsonSubTypes.Type(value = AddUserCommand.class, name = "addUser"),
        @JsonSubTypes.Type(value = DeleteUserCommand.class, name = "deleteUser"),
        @JsonSubTypes.Type(value = ShowAlbumsCommand.class, name = "showAlbums"),
        @JsonSubTypes.Type(value = AddAlbumCommand.class, name = "addAlbum"),
        @JsonSubTypes.Type(value = ShowPodcastsCommand.class, name = "showPodcasts"),
        @JsonSubTypes.Type(value = RemoveAlbumCommand.class, name = "removeAlbum"),
        @JsonSubTypes.Type(value = AddEventCommand.class, name = "addEvent"),
        @JsonSubTypes.Type(value = RemoveEventCommand.class, name = "removeEvent"),
        @JsonSubTypes.Type(value = AddMerchCommand.class, name = "addMerch"),
        @JsonSubTypes.Type(value = AddPodcastCommand.class, name = "addPodcast"),
        @JsonSubTypes.Type(value = RemovePodcastCommand.class, name = "removePodcast"),
        @JsonSubTypes.Type(value = AddAnnouncementCommand.class, name = "addAnnouncement"),
        @JsonSubTypes.Type(value = RemoveAnnouncementCommand.class, name = "removeAnnouncement"),
        @JsonSubTypes.Type(value = SwitchConnectionStatusCommand.class, name = "switchConnectionStatus"),
        @JsonSubTypes.Type(value = GetTop5AlbumsCommand.class, name = "getTop5Albums"),
        @JsonSubTypes.Type(value = GetTop5ArtistsCommand.class, name = "getTop5Artists"),
        @JsonSubTypes.Type(value = GetAllUsersCommand.class, name = "getAllUsers"),
        @JsonSubTypes.Type(value = GetOnlineUsersCommand.class, name = "getOnlineUsers"),
        @JsonSubTypes.Type(value = PrintCurrentPageCommand.class, name = "printCurrentPage"),
        @JsonSubTypes.Type(value = ChangePageCommand.class, name = "changePage"),
        @JsonSubTypes.Type(value = WrappedCommand.class, name = "wrapped"),
        @JsonSubTypes.Type(value = BuyPremium.class, name = "buyPremium"),
        @JsonSubTypes.Type(value = CancelPremium.class, name = "cancelPremium"),
        @JsonSubTypes.Type(value = AdBreakCommand.class, name = "adBreak"),
        @JsonSubTypes.Type(value = NullCommand.class, name = "subscribe"),
        @JsonSubTypes.Type(value = NullCommand.class, name = "getNotifications"),
        @JsonSubTypes.Type(value = BuyMerchCommand.class, name = "buyMerch"),
        @JsonSubTypes.Type(value = SeeMerchCommand.class, name = "seeMerch"),
        @JsonSubTypes.Type(value = NullCommand.class, name = "updateRecommendations"),
        @JsonSubTypes.Type(value = NullCommand.class, name = "previousPage"),
        @JsonSubTypes.Type(value = NullCommand.class, name = "loadRecommendations"),
        @JsonSubTypes.Type(value = NullCommand.class, name = "nextPage"),
})
@Getter
@Setter
@ToString
public abstract class Command {
    protected String command;
    protected String username;
    protected int timestamp;

    /**
     * @return
     */
    public abstract CommandOutput execute();
}
