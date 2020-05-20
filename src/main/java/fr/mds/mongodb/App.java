/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package fr.mds.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import fr.mds.mongodb.services.MongoService;

import java.util.Arrays;

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

        MongoService mongo = new MongoService(host, Integer.parseInt(port), dbName);

        MongoIterable<String> coll = mongo.getMongoDatabase().listCollectionNames();
        for (String collectionName: coll) {
            System.out.println(collectionName);
        }

        /*
        String result = ScannerSingleton.getInstance().getInput("Enter something");
        System.out.println(result);
        */
    }

    public static void debug()
    {
        System.out.println("Hola");
    }
}
