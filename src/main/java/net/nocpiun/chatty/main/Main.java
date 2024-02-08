package net.nocpiun.chatty.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Chatty Backend Server...");

        try {

            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Start the server
            App app = new App();

            // Type `stop` to exit
            BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                String in = sysin.readLine();
                if(in.equals("stop")) {
                    app.stop();
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
