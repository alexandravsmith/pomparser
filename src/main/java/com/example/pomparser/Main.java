package com.example.pomparser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a URL:");
        String url = scanner.nextLine();

        DatabaseManager dbManager = new DatabaseManager();
        System.out.println("Running Tests...");
        ogData ogdata = new ogData(dbManager);
        ogdata.parseAndStore(url);

        scanner.close();
    }
}
