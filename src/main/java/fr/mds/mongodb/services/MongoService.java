package fr.mds.mongodb.services;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;

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

    public ArrayList<String> getFields(String collection)
    {
        ArrayList<String> fields = new ArrayList<>();

        this.getMongoDatabase().getCollection(collection).find().forEach(x -> {
            x.keySet().forEach(y -> {
                if (!fields.contains(y)) {
                    fields.add(y);
                }
            });
        });

        return fields;
    }

    public Map<String, String> getFieldsType(String collection)
    {
        Map<String, String> fieldsType = new HashMap<>();

        this.getMongoDatabase().getCollection(collection).find().forEach(x -> {
            x.keySet().forEach(y -> {
                if (!fieldsType.containsKey(y)) {
                    fieldsType.put(y, x.get(y) == null ? "null" : x.get(y).getClass().getName());
                }
            });
        });

        return fieldsType;
    }

    public long deleteDocument(String collection, Document document)
    {
        return this.getMongoDatabase().getCollection(collection).deleteMany(document).getDeletedCount();
    }

    public long deleteDocument(String collection, FindIterable<org.bson.Document> documents)
    {
        long result = 0;

        for (org.bson.Document document: documents) {
            result += deleteDocument(collection, document);
        }

        return result;
    }

    public long updateDocument(String collection, Document documentsToUpdate,  Document value)
    {
        return this.getMongoDatabase().getCollection(collection).updateMany(Filters.eq(documentsToUpdate), value).getModifiedCount();
    }

    public long updateDocument(String collection, FindIterable<org.bson.Document> documents, Document value)
    {
        long result = 0;

        for (org.bson.Document document: documents) {
            result += updateDocument(collection, document, value);
        }

        return result;
    }

    public MongoDatabase getMongoDatabase() {
        if (this.database == null) {
            this.database = getMongoClient().getDatabase(dbName);
        }

        return this.database;
    }
}
