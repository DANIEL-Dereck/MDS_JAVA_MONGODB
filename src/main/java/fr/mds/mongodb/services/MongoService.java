package fr.mds.mongodb.services;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

public class MongoService {
    private final String host;
    private final Integer port;
    private final String dbName;
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoService(final String host, final Integer port, final String dbName)
    {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
    }

    public MongoClient getMongoClient() {
        if (this.mongoClient == null) {
            this.mongoClient = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyToClusterSettings(builder ->
                                    builder.hosts(Arrays.asList(new ServerAddress(host, port))))
                            .build());
        }

        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        if (this.database == null) {
            this.database = getMongoClient().getDatabase(dbName);
        }

        return this.database;
    }
}
