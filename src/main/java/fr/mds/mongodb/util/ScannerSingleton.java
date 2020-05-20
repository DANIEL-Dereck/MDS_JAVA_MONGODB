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
        return scanner.nextLine();
    }

    public String getInputString(String question)
    {
        System.out.println(question);
        return getInput();
    }

    public int getInputNumber()
    {
        return scanner.nextInt();
    }

    public int getInputNumber(String question)
    {
        System.out.println(question);
        return getInputNumber();
    }

}
