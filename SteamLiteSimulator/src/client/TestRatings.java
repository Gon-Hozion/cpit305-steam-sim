package client;

import database.DatabaseManager;

public class TestRatings {

    public static void main(String[] args) {

        boolean rated = DatabaseManager.rateGame(1, 1, 5);

        if (rated) {
            System.out.println("Rating saved!");
        }

        System.out.println(DatabaseManager.getGamesWithRatingsText());
    }
}