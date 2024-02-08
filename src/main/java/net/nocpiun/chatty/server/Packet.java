package net.nocpiun.chatty.server;

import com.google.gson.Gson;

public class Packet<D> {
    public final PacketType type;
    public D data;

    public Packet(PacketType type, D data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    
    public <T> T getData(Class<T> classOfT) {
        Gson gson = new Gson();
        String stringified = gson.toJson(data);
        return gson.fromJson(stringified, classOfT);
    }

    public static <D> String create(PacketType type, D data) {
        return new Packet<D>(type, data).toString();
    }

    public static <D> Packet<D> from(String message) {
        return new Gson().fromJson(message, Packet.class);
    }
}
