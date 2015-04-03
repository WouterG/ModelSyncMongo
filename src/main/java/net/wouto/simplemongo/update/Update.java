package net.wouto.simplemongo.update;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.Arrays;
import net.wouto.simplemongo.query.Query;

public class Update {
    
    private final DBObject updateObject;

    public Update() {
        this.updateObject = new BasicDBObject();
    }
    
    public Update(DBObject update) {
        this.updateObject = update;
    }

    public DBObject getUpdateQuery() {
        return this.updateObject;
    }
    
    public Update append(Update other) {
        for (String k : other.updateObject.keySet()) {
            DBObject obj = (DBObject) other.updateObject.get(k);
            if (this.updateObject.containsField(k)) {
                obj.putAll((DBObject) this.updateObject.get(k));
            }
            this.updateObject.put(k, obj);
        }
        return this;
    }
    
    public static Update increment(String field, Number amount) {
        return new Update(
                new BasicDBObject("$inc",
                        new BasicDBObject(field, amount)
                )
        );
    }
    
    public static Update multiply(String field, Number multiplier) {
        return new Update(
                new BasicDBObject("$mul",
                        new BasicDBObject(field, multiplier)
                )
        );
    }
    
    public static Update rename(String field, String newFieldName) {
        return new Update(
                new BasicDBObject("$rename",
                        new BasicDBObject(field, newFieldName)
                )
        );
    }
    
    public static Update setOnInsert(String field, Object value) {
        return new Update(
                new BasicDBObject("$setOnInsert", new BasicDBObject(field, value))
        );
    }
    
    public static Update set(String field, Object value) {
        return new Update(
                new BasicDBObject("$set", new BasicDBObject(field, value))
        );
    }
    
    public static Update unset(String field) {
        return new Update(
                new BasicDBObject(field, "")
        );
    }
    
    public static Update min(String field, Number nr) {
        return new Update(
                new BasicDBObject("$min",
                        new BasicDBObject(field, nr)
                )
        );
    }
    
    public static Update max(String field, Number nr) {
        return new Update(
                new BasicDBObject("$max",
                        new BasicDBObject(field, nr)
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
                new BasicDBObject(
                        "$currentDate",
                        new BasicDBObject(
                                field,
                                new BasicDBObject(
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
                new BasicDBObject(
                        "$currentDate",
                        new BasicDBObject(
                                field,
                                new BasicDBObject(
                                        "$type", 
                                        "timestamp"
                                )
                        )
                )
        );
    }
    
    public static Update addToSet(String field, Object value) {
        return new Update(
                new BasicDBObject(
                        "$addToSet",
                        new BasicDBObject(field, value)
                )
        );
    }
    
    public static Update pop(String field, PopOption pop) {
        return new Update(
                new BasicDBObject(
                        "$pop",
                        new BasicDBObject(field, pop.c)
                )
        );
    }
    
    public static Update pullAll(String field, Object... objs) {
        BasicDBList items = new BasicDBList();
        items.addAll(Arrays.asList(objs));
        return new Update(
                new BasicDBObject(
                        "$pullAll",
                        new BasicDBObject(
                                field, 
                                items
                        )
                )
        );
    }
    
    public static Update pull(String field, Object value) {
        return new Update(
                new BasicDBObject(
                        "$pull",
                        new BasicDBObject(field, value)
                )
        );
    }
    
    public static Update pull(String field, Query query) {
        return new Update(
                new BasicDBObject(
                        "$pull",
                        query.getQuery()
                )
        );
    }
    
    public static Update pushAll(String field, Object... values) {
        BasicDBList list = new BasicDBList();
        list.addAll(Arrays.asList(values));
        return new Update(
                new BasicDBObject(
                        "$pullAll",
                        new BasicDBObject(field, list)
                )
        );
    }
    
    public static Update push(String field, Object value) {
        return new Update(
                new BasicDBObject(
                        "$push", 
                        new BasicDBObject(field, value)
                )
        );
    }
    
}
