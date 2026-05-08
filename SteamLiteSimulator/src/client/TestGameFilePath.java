package client;

import database.DatabaseManager;

public class TestGameFilePath {

    public static void main(String[] args) {

        String path = DatabaseManager.getGameFilePath(1);

        System.out.println("Game file path: " + path);
    }
}