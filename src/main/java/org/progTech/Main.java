package org.progTech;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connect4Manager manager = new Connect4Manager(dbConnection);
        manager.fomenu();
    }
}
