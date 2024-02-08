package net.nocpiun.chatty.user;

public class User {
    public String name;
    public String token;
    public String password; // md5 + salt

    public User(String name, String token, String password) {
        this.name = name;
        this.token = token;
        this.password = password;
    }
}
