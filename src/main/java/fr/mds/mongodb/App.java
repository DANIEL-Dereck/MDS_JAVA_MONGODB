/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package fr.mds.mongodb;

import com.mongodb.client.*;
import fr.mds.mongodb.manager.MongoSelector;
import fr.mds.mongodb.services.MongoService;
import fr.mds.mongodb.util.ScannerSingleton;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class App {
    private static final String ARG_HOST = "--host";
    private static final String ARG_PORT = "--port";
    private static final String ARG_DBNAME = "--dbName";

    public static void main(String[] args) {
        String host = "localhost";
        String port = "27018";
        String dbName = "";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(ARG_HOST) && args.length >= i + 1 && args[i+1] != null) {
                host = args[i+1];
            } else if (args[i].equals(ARG_PORT) && args.length >= i + 1 && args[i+1] != null) {
                port = args[i+1];
            } else if (args[i].equals(ARG_DBNAME) && args.length >= i + 1 && args[i+1] != null) {
                dbName = args[i+1];
            }
        }

        if (dbName.equals("")) {
            System.out.println("ERROR: no database selected.");
            return;
        }

        new MongoSelector(new MongoService(host, Integer.parseInt(port), dbName)).run();
    }

    public static void debug()
    {
        System.out.println("Debug");
    }
}
