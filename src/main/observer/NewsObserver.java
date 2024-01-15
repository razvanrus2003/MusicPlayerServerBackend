package main.observer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.User;
import main.output.Formats.Notification;

@Getter
@Setter
@ToString
/**
 * Observer class
 * Contains the user that is subscribed to the artist
 */
public class NewsObserver implements Observer {
    private User user;

    public NewsObserver(final User user) {
        this.user = user;
    }

    /** fuction that adds a notification to the user when a artist notifies the observer
     * @param news  the news to be added
     * @param artist the artist that added the news
     */
    @Override
    public void update(final String news, final User artist) {
        user.getNotifications().add(new Notification(
                news,
                news
                        + " from "
                        +
                        artist.getUsername()
                        + "."
        ));
    }
}
