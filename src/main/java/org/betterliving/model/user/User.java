package org.betterliving.model.user;

public abstract class User {

    protected String name;
    protected int id;

    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Name : " + name + ", ID : " + id;
    }
    
}
