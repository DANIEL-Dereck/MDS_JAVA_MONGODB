package fr.mds.mongodb.manager;

import com.github.javafaker.Faker;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import fr.mds.mongodb.services.MongoService;
import fr.mds.mongodb.util.Menu;
import fr.mds.mongodb.util.ScannerSingleton;
import org.bson.*;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private String selectCollection()
    {
        ArrayList<String> collections = new ArrayList<>();
        mongos.getMongoDatabase().listCollectionNames().forEach(collections::add);
        return Menu.numberMenuSelector(collections, "Choose collection:");
    }

    private void searchOrInsertDocument(String collection)
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

    private void searchQuestions(String collection) {
        String field = Menu.numberMenuSelector(mongos.getFields(collection), "Which field do you want to use to search ?");
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
        } else if (cls == Double.class)
        {
            filter = searchDouble(field);
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

        Document documentToUpdate = null;
        long numberItemsChange = 0;

        ArrayList<String> listStringItem = new ArrayList<>();
        documentsFound.forEach(x -> {
            listStringItem.add(x.toJson());
        });

        switch (result) {
            case 1:
                numberItemsChange = mongos.deleteDocument(collection, documentsFound);
                System.out.println( numberItemsChange + " documents deleted.");
                break;
            case 2:
                String itemToDelete = Menu.numberMenuSelector(listStringItem, "Which document do you want to delete ?");
                numberItemsChange = mongos.deleteDocument(collection, Document.parse(itemToDelete));
                System.out.println( numberItemsChange + " documents deleted.");
                break;
            case 3:
                String itemToUpdate = Menu.numberMenuSelector(listStringItem, "Which document do you want to update ?");
                documentToUpdate = Document.parse(itemToUpdate);
            case 4:
                String fieldToUpdate = Menu.numberMenuSelector(mongos.getFields(collection), "Which field do you want to update ?");
                String value = Menu.numberMenuSelector(mongos.getFields(collection), "Which value ?");

                Document filter = new Document();
                filter.append("$set", 33);
                // TODO: create BSON filter.

                if (documentToUpdate != null)
                {
                    numberItemsChange = mongos.updateDocument(collection, documentToUpdate, null);
                } else {
                    numberItemsChange = mongos.updateDocument(collection, documentsFound,null);
                }

                System.out.println( numberItemsChange + " documents updated.");
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

    private Bson searchDouble(String field)
    {
        ArrayList<String> operators = new ArrayList<String>() {{
            add("$eq"); add("$ne"); add("$in"); add("$lt"); add("$lte"); add("$gt"); add("$gte");
        }};

        String operator = Menu.numberMenuSelector(operators, "Which operator do you want to use ?");
        Double value = ScannerSingleton.getInstance().getInputDouble("Insert value");

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

    private void insert(String collection) {
        System.out.println("WIP insert");

        if (Menu.menuYesNo("Generate random insert ?").equals("yes")) {
            fakerDocumentGenerator(collection);
        } else {
            documentGenerator(collection);
        }

        if (Menu.menuYesNo("Continue to insert ?").equals("yes")) {
            insert(collection);
        }
    }

    private void documentGenerator(String collection)
    {
        Document newDocument = new Document();

        for (String key : mongos.getFields(collection)) {
            Class<?> cls = null;
            try {
                cls = Class.forName(mongos.getFieldsType(collection).get(key));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                cls = String.class;
            }

            if (cls == String.class) {
                if (key.equals("_id")) {
                    newDocument.append(key, new BsonString(UUID.randomUUID().toString()));
                } else {
                    newDocument.append(key, new BsonString(ScannerSingleton.getInstance().getInputString(key + ": Insert value ")));
                }
            } else if (cls == Integer.class) {
                newDocument.append(key, new BsonInt32(ScannerSingleton.getInstance().getInputNumber(key + ": Insert value ")));
            } else if (cls == Boolean.class) {
                newDocument.append(key, new BsonBoolean(Menu.menuTrueFalse(key + ": insert value ")));
            } else if (cls == Double.class) {
                newDocument.append(key, new BsonDouble(ScannerSingleton.getInstance().getInputDouble(key + ": Insert value ")));
            }
        }

        System.out.println(String.format("Insert %s", newDocument.toJson()));
        mongos.insert(collection, newDocument);
    }

    private void fakerDocumentGenerator(String collection) {
        Document newDocument = new Document();

        for (String key : mongos.getFields(collection)) {
            Class<?> cls = null;
            try {
                cls = Class.forName(mongos.getFieldsType(collection).get(key));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                cls = String.class;
            }

            if (cls == String.class) {
                if (key.equals("_id")) {
                    newDocument.append(key, new BsonString(UUID.randomUUID().toString()));
                } else {
                    StringBuilder builder = new StringBuilder();
                    List<String> strings = Faker.instance().lorem().words(Faker.instance().number().numberBetween(1, 5));
                    for (String string : strings) {
                        builder.append(string);
                    }
                    newDocument.append(key, new BsonString(builder.toString()));
                }
            } else if (cls == Integer.class) {
                newDocument.append(key, new BsonInt32(Faker.instance().number().numberBetween(1, Integer.MAX_VALUE)));
            } else if (cls == Boolean.class) {
                newDocument.append(key, new BsonBoolean(Faker.instance().bool().bool()));
            } else if (cls == Double.class) {
                newDocument.append(key, new BsonDouble(Faker.instance().number().randomDouble(20, -Integer.MAX_VALUE, Integer.MAX_VALUE)));
            }
        }

        System.out.println(String.format("Insert %s", newDocument.toJson()));
        mongos.insert(collection, newDocument);
    }
}
