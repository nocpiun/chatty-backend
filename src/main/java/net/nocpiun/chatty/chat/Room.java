package net.nocpiun.chatty.chat;

import javax.annotation.Nullable;
import java.util.List;

public class Room {
    public final String id;
    public List<String> members;
    @Nullable
    public String name;

    public Room(String id, List<String> members) {
        this.id = id;
        this.members = members;
    }
}
