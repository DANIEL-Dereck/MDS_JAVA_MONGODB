package fr.mds.mongodb.manager;

import com.mongodb.BasicDBObject;
import fr.mds.mongodb.services.MongoService;
import fr.mds.mongodb.util.Menu;
import fr.mds.mongodb.util.ScannerSingleton;

import java.util.ArrayList;

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

        if (Menu.menuYesNo("Continue to select collection ?").equals("yes")) {
            run();
        }
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

        if (Menu.menuYesNo("Continue to search or insert ?").equals("yes")) {
            searchOrInsertDocument(collection);
        }
    }

    public void search(String collection) {
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> operators = new ArrayList<String>() {{
            add("$eq"); add("$ne"); add("$in"); add("$lt"); add("$lte"); add("$gt"); add("$gte");
            }};

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

        if (Menu.menuYesNo("Continue to search ?").equals("yes")) {
            search(collection);
        }
    }

    public void insert(String collection) {
        System.out.println("WIP insert");

        if (Menu.menuYesNo("Continue to insert ?").equals("yes")) {
            insert(collection);
        }
    }
}
