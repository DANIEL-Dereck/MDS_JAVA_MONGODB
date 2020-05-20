package fr.mds.mongodb.util;

import java.util.ArrayList;

public class Menu {
    public static String numberMenuSelector(ArrayList<String> collections, String question)
    {
        String result = "";
        int choise = 0;
        int i = 0;

        do {
            i = 0;
            for (String collectionName : collections) {
                System.out.println(i++ + ")" + collectionName);
            }

            choise = ScannerSingleton.getInstance().getInputNumber(question);
            if (choise < i) {
                result = collections.get(choise);
            }
        } while (result.equals(""));

        return result;

    }
}
