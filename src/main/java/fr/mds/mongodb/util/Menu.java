package fr.mds.mongodb.util;

import java.util.ArrayList;

public class Menu {
    public static String numberMenuSelector(ArrayList<String> collections, String question)
    {
        String result = "";
        int choice = 0;
        int i = 0;

        do {
            i = 0;
            for (String collectionName : collections) {
                System.out.println(i++ + ")" + collectionName);
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
}
