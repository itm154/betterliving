// Save as: User.java

interface UserActions {
    boolean authenticate(String enteredId, String enteredPassword);
    String getRole();
    String getUsername();
    String getId();
    String toFileFormat(); // Formats data to save in users.txt
}

public abstract class User implements UserActions {
    protected String id;
    protected String username;
    protected String password;
    protected String role;

    public User(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public String getId() { return this.id; }

    @Override
    public String getRole() { return this.role; }

    @Override
    public String getUsername() { return this.username; }

    @Override
    public String toFileFormat() {
        // Saves exactly like: 101|JohnDoe|pass123|Student
        return id + "|" + username + "|" + password + "|" + role;
    }
}

class Student extends User {
    public Student(String id, String username, String password) {
        super(id, username, password, "Student");
    }

    @Override
    public boolean authenticate(String enteredId, String enteredPassword) {
        return this.id.equals(enteredId) && this.password.equals(enteredPassword);
    }
}

class Lecturer extends User {
    public Lecturer(String id, String username, String password) {
        super(id, username, password, "Lecturer");
    }

    @Override
    public boolean authenticate(String enteredId, String enteredPassword) {
        return this.id.equals(enteredId) && this.password.equals(enteredPassword);
    }
}