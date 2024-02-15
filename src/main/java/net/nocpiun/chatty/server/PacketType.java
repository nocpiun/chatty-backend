package net.nocpiun.chatty.server;

public enum PacketType {
    HANDSHAKE,
    CLOSE,
    ERROR,
    REGISTER,
    UNREGISTER,
    LOGIN,
    LOGOUT,
    PROFILE,
    ROOM,
    ROOM_DETAIL,
    ADD_FRIEND,
    DELETE_FRIEND,
    MESSAGE
}
