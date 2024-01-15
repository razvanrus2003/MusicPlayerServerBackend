package main.observer;

import main.User;

/**
 * Observer interface
 * Contains the user that is subscribed to the artist
 */
public interface Observer {
    /**
     * Update the user with the artist's news
     * @param name The name of the artist
     * @param artist The artist
     */
    void update(String name, User artist);
}
