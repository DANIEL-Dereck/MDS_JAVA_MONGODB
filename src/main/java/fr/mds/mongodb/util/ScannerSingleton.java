package fr.mds.mongodb.util;

import java.util.Scanner;

public class ScannerSingleton {
    private static ScannerSingleton instance;
    private final Scanner scanner;

    private ScannerSingleton()
    {
        scanner = new Scanner(System.in);
    }

    public static ScannerSingleton getInstance()
    {
        if (instance == null) {
            instance = new ScannerSingleton();
        }

        return instance;
    }

    public String getInput()
    {
        return scanner.next();
    }

    public String getInput(String question)
    {
        System.out.println(question);
        return scanner.next();
    }
}
