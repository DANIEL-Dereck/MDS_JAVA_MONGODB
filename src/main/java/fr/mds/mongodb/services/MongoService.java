package fr.mds.mongodb.services;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

public class MongoService {
    private String host = "localhost";
    private Integer port = 27018;
    private String dbName = "";
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoService(String host, Integer port, String dbName)
    {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
    }

    public MongoClient getMongoClient() {
        if (this.mongoClient == null) {
            this.mongoClient = createMongoClient(this.host, this.port);
        }

        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        if (this.database == null) {
            this.database = getMongoClient().getDatabase(dbName);
        }

        return this.database;
    }

    private static MongoClient createMongoClient(final String host, final Integer port)
    {
        MongoClient client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress(host, port))))
                        .build());

        return client;
    }
}
