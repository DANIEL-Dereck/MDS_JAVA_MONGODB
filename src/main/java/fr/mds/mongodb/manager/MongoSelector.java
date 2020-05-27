package fr.mds.mongodb.manager;

import com.mongodb.client.model.Filters;
import fr.mds.mongodb.services.MongoService;
import fr.mds.mongodb.util.Menu;
import fr.mds.mongodb.util.ScannerSingleton;
import org.bson.conversions.Bson;

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

        Bson filter = null;

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

        if (filter != null)
        {
            mongos.getMongoDatabase().getCollection(collection).find(filter).forEach(x -> System.out.println(x.toJson()));
        } else {
            System.out.println("ERROR: unknow field type.");
        }

        if (Menu.menuYesNo("Continue to search ?").equals("yes")) {
            searchQuestions(collection);
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
