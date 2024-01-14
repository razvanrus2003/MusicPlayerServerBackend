package main.observer;

import main.User;

import java.util.ArrayList;

public interface Observer {
    public void update(String name, User artist);
}
