package fr.mds.mongodb.util;

import java.util.ArrayList;

public class Menu {
    public static String numberMenuSelector(ArrayList<String> collections, String question)
    {
        String result = "";
        int choice;
        int i;

        do {
            i = 0;
            System.out.println("==========================================================================");
            for (String collectionName : collections) {
                System.out.println(i++ + ") " + collectionName);
            }

            choice = ScannerSingleton.getInstance().getInputNumber(question);
            if (choice >= 0 && choice < i) {
                result = collections.get(choice);
            } else {
                System.out.println("Invalid choice.");
            }
        } while (result.equals(""));

        return result;
    }

    public static Integer numberMenuSelectorPosition(ArrayList<String> collections, String question)
    {
        String result = "";
        int choice;
        int i;

        do {
            i = 0;
            System.out.println("==========================================================================");
            for (String collectionName : collections) {
                System.out.println(i++ + ") " + collectionName);
            }

            choice = ScannerSingleton.getInstance().getInputNumber(question);
            if (choice >= 0 && choice < i) {
                result = collections.get(choice);
            } else {
                System.out.println("Invalid choice.");
            }
        } while (result.equals(""));

        return choice;
    }

    public static String menuYesNo(String question)
    {
        ArrayList<String> collections = new ArrayList<String>() {{ add("yes"); add("no");}};
        String result = "";
        int choice;
        int i;

        do {
            i = 0;
            System.out.println("==========================================================================");
            for (String collectionName : collections) {
                System.out.println(i++ + ") " + collectionName);
            }

            choice = ScannerSingleton.getInstance().getInputNumber(question);
            if (choice >= 0 && choice < i) {
                result = collections.get(choice);
            } else {
                System.out.println("Invalid choice.");
            }
        } while (!(result.equals("yes") || result.equals("no")));

        return result;
    }

}
