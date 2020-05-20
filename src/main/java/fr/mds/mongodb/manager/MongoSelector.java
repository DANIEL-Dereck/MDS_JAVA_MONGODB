package fr.mds.mongodb.manager;

import fr.mds.mongodb.services.MongoService;
import fr.mds.mongodb.util.Menu;

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
            search();
        } else if (result.equals(insert)) {
            insert();
        }
    }

    public void search() {
        System.out.println("WIP search");
    }

    public void insert() {
        System.out.println("WIP insert");
    }
}
