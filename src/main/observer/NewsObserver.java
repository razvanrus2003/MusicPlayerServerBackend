package main.observer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.User;
import main.output.Formats.Notification;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class NewsObserver implements Observer{
    private User user;

    public NewsObserver(User user) {
        this.user = user;
    }
    @Override
    public void update(String news, User artist) {
        user.getNotifications().add(new Notification(news, news + " from " + artist.getUsername() + "."));
    }
}
