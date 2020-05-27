package fr.mds.mongodb.manager;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import fr.mds.mongodb.services.MongoService;
import fr.mds.mongodb.util.Menu;
import fr.mds.mongodb.util.ScannerSingleton;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class MongoSelector {
    private final MongoService mongos;

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
            searchQuestions(collection);
        } else if (result.equals(insert)) {
            insert(collection);
        }

        if (Menu.menuYesNo("Continue to search or insert ?").equals("yes")) {
            searchOrInsertDocument(collection);
        }
    }

    public void searchQuestions(String collection) {
        ArrayList<String> fields = mongos.getFields(collection);
        String field = Menu.numberMenuSelector(fields, "Which field do you want to use to search ?");
        String fieldsType = mongos.getFieldsType(collection).get(field);

        Class<?> cls = null;
        try {
            cls = Class.forName(fieldsType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = String.class;
        }

        Bson filter = getFilter(field, cls);

        if (filter != null)
        {
            FindIterable<Document> documentsFound =  mongos.getMongoDatabase().getCollection(collection).find(filter);
            documentsFound.forEach(x -> System.out.println(x.toJson()));
            documentOperation(collection, documentsFound);
        } else {
            System.out.println("ERROR: unknow field type.");
        }

        if (Menu.menuYesNo("Continue to search ?").equals("yes")) {
            searchQuestions(collection);
        }
    }

    private Bson getFilter(String field, Class<?> cls) {
        Bson filter;
        if (cls == String.class)
        {
            filter = searchString(field);
        } else if (cls == Integer.class)
        {
            filter = searchInteger(field);
        } else if (cls == Boolean.class)
        {
            filter = searchBoolean(field);
        } else {
            filter = null;
        }
        return filter;
    }

    private void documentOperation(String collection, FindIterable<Document> documentsFound) {
        ArrayList<String> menu = new ArrayList<String>(){{
            add("Nothing");
            add("Delete All selected documents");
            add("Delete One document selected");
            add("Update all documents selected");
            add("Update all document selected");
        }};

        Integer result = Menu.numberMenuSelectorPosition(menu, "what you want to do ?");

        long numberItemsChange = 0;

        switch (result) {
            case 1:
                numberItemsChange = mongos.deleteDocument(collection, documentsFound);
                System.out.println( numberItemsChange + " document deleted.");
                break;
            case 2:
                ArrayList<String> listStringItem = new ArrayList<>();
                documentsFound.forEach(x -> {
                    listStringItem.add(x.toJson());
                });

                String itemToDelete = Menu.numberMenuSelector(listStringItem, "Which document do you want to delete ?");
                numberItemsChange = mongos.deleteDocument(collection, Document.parse(itemToDelete));
                System.out.println( numberItemsChange + " document deleted.");
                break;
            case 3:
                // TODO: Update all;
                //mongos.getMongoDatabase().getCollection(collection).find(filter).forEach(x -> System.out.println(x.toJson()));
                break;
            case 4:
                // TODO: update one;
                //mongos.getMongoDatabase().getCollection(collection).find(filter).forEach(x -> System.out.println(x.toJson()));
                break;
            case 0:
            default:
                break;
        }
    }

    private Bson searchString(String field)
    {
        ArrayList<String> operators = new ArrayList<String>() {{
            add("$eq"); add("$ne");
        }};

        String operator = Menu.numberMenuSelector(operators, "Which operator do you want to use ?");
        String value = ScannerSingleton.getInstance().getInputString("Insert value");
        value = ScannerSingleton.getInstance().getInput();

        Bson filter;
        switch (operator) {
            case "$eq":
                filter = Filters.eq(field, value);
                break;
            case "$ne":
                filter = Filters.ne(field, value);
                break;
            default:
                filter = null;
                break;
        }

        return filter;
    }

    private Bson searchInteger(String field)
    {
        ArrayList<String> operators = new ArrayList<String>() {{
            add("$eq"); add("$ne"); add("$in"); add("$lt"); add("$lte"); add("$gt"); add("$gte");
        }};

        String operator = Menu.numberMenuSelector(operators, "Which operator do you want to use ?");
        Integer value = ScannerSingleton.getInstance().getInputNumber("Insert value");

        Bson filter = null;

        switch (operator) {
            case "$eq":
                filter = Filters.eq(field, value);
                break;
            case "$ne":
                filter = Filters.ne(field, value);
                break;
            case "$in":
                filter = Filters.in(field, value);
                break;
            case "$lt":
                filter = Filters.lt(field, value);
                break;
            case "$lte":
                filter = Filters.lte(field, value);
                break;
            case "$gt":
                filter = Filters.gt(field, value);
                break;
            case "$gte":
                filter = Filters.gte(field, value);
                break;
        }

        return filter;
    }

    private Bson searchBoolean(String field)
    {
        ArrayList<String> operators = new ArrayList<String>() {{
            add("$eq"); add("$ne");
        }};

        String operator = Menu.numberMenuSelector(operators, "Which operator do you want to use ?");
        Boolean value = Boolean.valueOf(ScannerSingleton.getInstance().getInputString("Insert value"));
        value = Boolean.valueOf(ScannerSingleton.getInstance().getInput());

        Bson filter = null;

        switch (operator) {
            case "$eq":
                filter = Filters.eq(field, value);
                break;
            case "$ne":
                filter = Filters.ne(field, value);
                break;
        }

        return filter;
    }

    public void insert(String collection) {
        System.out.println("WIP insert");

        if (Menu.menuYesNo("Continue to insert ?").equals("yes")) {
            insert(collection);
        }
    }
}
