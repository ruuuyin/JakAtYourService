package jays.data.entity;

public class User {
    private String user;
    private int avatar;

    public User(String user, int avatar) {
        this.user = user;
        this.avatar = avatar;
    }

    public String getUser() {
        return user;
    }

    public int getAvatar() {
        return avatar;
    }
}
