package fr.mds.mongodb.manager;

import com.mongodb.BasicDBObject;
import fr.mds.mongodb.services.MongoService;
import fr.mds.mongodb.util.Menu;
import fr.mds.mongodb.util.ScannerSingleton;

import java.util.ArrayList;
import java.util.Arrays;

public class MongoSelector {
    private MongoService mongos;

    public MongoSelector(MongoService mongos)
    {
        this.mongos = mongos;
    }

    public void run()
    {
        String collection = selectCollection();
        searchOrInsertDocument(collection);
    }

    public String selectCollection()
    {
        ArrayList<String> collections = new ArrayList<>();
        mongos.getMongoDatabase().listCollectionNames().forEach(collections::add);
        return Menu.numberMenuSelector(collections, "Choose collection:");
    }

    public void searchOrInsertDocument(String collection)
    {
        ArrayList<String> menu = new ArrayList<>();
        String search = "Search";
        String insert = "Insert";

        menu.add(search);
        menu.add(insert);

        String result = Menu.numberMenuSelector(menu, "what you want to do ?");

        if (result.equals(search)) {
            search(collection);
        } else if (result.equals(insert)) {
            insert(collection);
        }
    }

    public void search(String collection) {
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> operators = new ArrayList<>();
        operators.add("$eq");
        operators.add("$nq");
        operators.add("$in");
        operators.add("$lt");
        operators.add("$lte");
        operators.add("$gt");
        operators.add("$gte");

        mongos.getMongoDatabase().getCollection(collection).find().forEach(x -> {
            x.keySet().forEach(y -> {
                if (!fields.contains(y)) {
                    fields.add(y);
                }
            });
        });

        String field = Menu.numberMenuSelector(fields, "Which field do you want to use to search ?");
        String operator = Menu.numberMenuSelector(operators, "Which operator do you want to use ?");
        String value = ScannerSingleton.getInstance().getInputString("Insert value");
        value = ScannerSingleton.getInstance().getInput();

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.append(field, new BasicDBObject(operator, value));

        mongos.getMongoDatabase().getCollection(collection).find(whereQuery).forEach(x -> System.out.println(x));

        String choice = Menu.numberMenuSelector(new ArrayList<String>(new ArrayList<String>() {{ add("yes"); add("no");}}) , "Continue to search ?");
        if (choice.equals("yes")) {
            search(collection);
        }
    }

    public void insert(String collection) {
        System.out.println("WIP insert");
    }
}
