package net.nocpiun.chatty.user;

import net.nocpiun.chatty.chat.Room;

import java.util.List;

public class Profile {
    public String userName;
    public List<Room> rooms;

    public Profile(String userName, List<Room> rooms) {
        this.userName = userName;
        this.rooms = rooms;
    }
}
