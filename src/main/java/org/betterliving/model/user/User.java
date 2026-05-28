package org.betterliving.model.user;

public abstract class User {

    protected String name;
    protected int id;

    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
