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
        int result;

        if(scanner.hasNextInt()){
            result = scanner.nextInt();
        }else{
            System.out.println("\"" + scanner.nextLine() + "\" => is not a number. Choose a valid number please.");
            result = -1;
        }

        return result;
    }

    public int getInputNumber(String question)
    {
        System.out.println(question);
        return getInputNumber();
    }

    public Double getInputDouble()
    {
        Double result;

        if(scanner.hasNextInt()){
            result = scanner.nextDouble();
        }else{
            System.out.println("\"" + scanner.nextLine() + "\" => is not a number. Choose a valid number please.");
            result = -1d;
        }

        return result;
    }

    public Double getInputDouble(String question)
    {
        System.out.println(question);
        return getInputDouble();
    }
}
