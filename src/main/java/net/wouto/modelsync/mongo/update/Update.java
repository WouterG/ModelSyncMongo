package net.wouto.modelsync.mongo.update;

import com.mongodb.BasicDBList;
import java.util.Arrays;
import net.wouto.modelsync.mongo.query.Query;
import org.bson.Document;

public class Update {
    
    private final Document updateObject;

    public Update() {
        this.updateObject = new Document();
    }
    
    public Update(Document update) {
        this.updateObject = update;
    }

    public Document getUpdateQuery() {
        return this.updateObject;
    }
    
    public Update append(Update other) {
        for (String k : other.updateObject.keySet()) {
            Document obj = (Document) other.updateObject.get(k);
            if (this.updateObject.containsKey(k)) {
                obj.putAll((Document) this.updateObject.get(k));
            }
            this.updateObject.put(k, obj);
        }
        return this;
    }
    
    public static Update increment(String field, Number amount) {
        return new Update(
                new Document("$inc",
                        new Document(field, amount)
                )
        );
    }
    
    public static Update multiply(String field, Number multiplier) {
        return new Update(
                new Document("$mul",
                        new Document(field, multiplier)
                )
        );
    }
    
    public static Update rename(String field, String newFieldName) {
        return new Update(
                new Document("$rename",
                        new Document(field, newFieldName)
                )
        );
    }
    
    public static Update setOnInsert(String field, Object value) {
        return new Update(
                new Document("$setOnInsert", new Document(field, value))
        );
    }
    
    public static Update set(String field, Object value) {
        return new Update(
                new Document("$set", new Document(field, value))
        );
    }
    
    public static Update unset(String field) {
        return new Update(
                new Document(field, "")
        );
    }
    
    public static Update min(String field, Number nr) {
        return new Update(
                new Document("$min",
                        new Document(field, nr)
                )
        );
    }
    
    public static Update max(String field, Number nr) {
        return new Update(
                new Document("$max",
                        new Document(field, nr)
                )
        );
    }
    
    /**
     * would translate to:<br />
     * <i>{ $currentDate: { field: { $type: "date" } } }</i>
     * @param field
     * @return 
     */
    public static Update currentDate(String field) {
        return new Update(
                new Document(
                        "$currentDate",
                        new Document(
                                field,
                                new Document(
                                        "$type", 
                                        "date"
                                )
                        )
                )
        );
    }
    
    /**
     * would translate to:<br />
     * <i>{ $currentDate: { field: { $type: "timestamp" } } }</i>
     * @param field
     * @return 
     */
    public static Update currentTimestamp(String field) {
        return new Update(
                new Document(
                        "$currentDate",
                        new Document(
                                field,
                                new Document(
                                        "$type", 
                                        "timestamp"
                                )
                        )
                )
        );
    }
    
    public static Update addToSet(String field, Object value) {
        return new Update(
                new Document(
                        "$addToSet",
                        new Document(field, value)
                )
        );
    }
    
    public static Update pop(String field, PopOption pop) {
        return new Update(
                new Document(
                        "$pop",
                        new Document(field, pop.c)
                )
        );
    }
    
    public static Update pullAll(String field, Object... objs) {
        BasicDBList items = new BasicDBList();
        items.addAll(Arrays.asList(objs));
        return new Update(
                new Document(
                        "$pullAll",
                        new Document(
                                field, 
                                items
                        )
                )
        );
    }
    
    public static Update pull(String field, Object value) {
        return new Update(
                new Document(
                        "$pull",
                        new Document(field, value)
                )
        );
    }
    
    public static Update pull(String field, Query query) {
        return new Update(
                new Document(
                        "$pull",
                        query.getQuery()
                )
        );
    }
    
    public static Update pushAll(String field, Object... values) {
        BasicDBList list = new BasicDBList();
        list.addAll(Arrays.asList(values));
        return new Update(
                new Document(
                        "$pullAll",
                        new Document(field, list)
                )
        );
    }
    
    public static Update push(String field, Object value) {
        return new Update(
                new Document(
                        "$push", 
                        new Document(field, value)
                )
        );
    }
    
}
