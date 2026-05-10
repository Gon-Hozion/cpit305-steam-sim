package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class ServerLogger {

    public static void log(String message) {

        try (
                FileWriter fw = new FileWriter("server.log", true);
                PrintWriter pw = new PrintWriter(fw)
        ) {

            pw.println("[" + LocalDateTime.now() + "] " + message);

        } catch (IOException e) {

            System.out.println("Logging failed: " + e.getMessage());
        }
    }
}