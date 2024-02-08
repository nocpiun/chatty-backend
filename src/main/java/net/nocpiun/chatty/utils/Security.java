package net.nocpiun.chatty.utils;

import net.nocpiun.chatty.configuration.Configuration;

import java.security.*;

public class Security {
    public static String md5Encrypt(String text) throws Exception {
        final String salt = Configuration.get().getSalt();
        text = text + salt;
        byte[] bytesOfMessage = text.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] result = md.digest(bytesOfMessage);

        return new String(result, "UTF-8");
    }
}
