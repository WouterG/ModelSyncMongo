package net.wouto.modelsync.mongo.query;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.regex.Pattern;

public class Query {

    private final DBObject queryObject;

    public Query() {
        this.queryObject = new BasicDBObject();
    }
    
    public Query(DBObject q) {
        this.queryObject = q;
    }

    public DBObject getQuery() {
        return this.queryObject;
    }

    public static Query greaterThan(String key, Number nr) {
        return new Query(
                new BasicDBObject(
                        key,
                        new BasicDBObject("$gt", nr)
                )
        );
    }

    public static Query greaterThanOrEqual(String key, Number nr) {
        return new Query(
                new BasicDBObject(
                        key,
                        new BasicDBObject("$gte", nr)
                )
        );
    }

    public static Query lessThan(String key, Number nr) {
        return new Query(
                new BasicDBObject(
                        key,
                        new BasicDBObject("$lt", nr)
                )
        );
    }

    public static Query lessThanOrEqual(String key, Number nr) {
        return new Query(
                new BasicDBObject(
                        key,
                        new BasicDBObject("$lte", nr)
                )
        );
    }

    public static Query notEqual(String key, Object obj) {
        return new Query(
                new BasicDBObject(
                        key,
                        new BasicDBObject("$ne", obj)
                )
        );
    }
    
    public static Query equals(String key, Object obj) {
        return new Query(
                new BasicDBObject(
                        key,
                        obj
                )
        );
    }
    
    public static Query exists(String key) {
        return exists(key, true);
    }
    
    public static Query exists(String key, boolean exists) {
        return new Query(
                new BasicDBObject(
                        key,
                        new BasicDBObject("$exists", exists)
                )
        );
    }
    
    public static Query type(String key, Number nr) {
        return new Query(
                new BasicDBObject(
                        key,
                        new BasicDBObject("$type", nr)
                )
        );
    }
    
    public static Query mod(String key, final Number devisor, final Number remainder) {
        return new Query(
                new BasicDBObject(
                        key,
                        new BasicDBObject(
                                "$mod",
                                new BasicDBList() {
                                    {
                                        add(devisor);
                                        add(remainder);
                                    }
                                }
                        )
                )
        );
    }
    
    public static Query regex(String key, String pattern) {
        return regex(key, pattern, 0);
    }
    
    public static Query regex(String key, String pattern, RegexOption... options) {
        int f = 0;
        for (RegexOption o : options) {
            f += o.getFlag();
        }
        return regex(key, pattern, f);
    }
    
    private static Query regex(String key, String pattern, int options) {
        return new Query(
                new BasicDBObject(
                        key,
                        Pattern.compile(pattern, options)
                )
        );
    }
    
    public static Query text(String search) {
        return text(search, "none");
    }
    
    public static Query text(String search, String language) {
        return new Query(
                new BasicDBObject(
                        "$text",
                        new BasicDBObject()
                            .append("$search", search)
                            .append("$language", language)
                )
        );
    }
    
    public static Query size(String key, Number size) {
        return new Query(
                new BasicDBObject(
                        key,
                        new BasicDBObject("$size", size)
                )
        );
    }
    
    public static final Query empty = new Query();
    
    public static QueryList all(String key) {
        return new QueryList(key, "$all");
    }
    
    public static QueryList elemMatch(String key) {
        return new QueryList(key, "$elemMatch");
    }
    
    public static QueryList in(String key) {
        return new QueryList(key, "$in");
    }
    
    public static QueryList notIn(String key) {
        return new QueryList(key, "$nin");
    }
    
    public static QueryList and() {
        return new QueryList("$and");
    }
    
    public static QueryList or() {
        return new QueryList("$or");
    }
    
    public static QueryList not() {
        return new QueryList("$not");
    }
    
    public static QueryList nor() {
        return new QueryList("$nor");
    }
    
    public Query append(Query other) {
        for (String k : other.queryObject.keySet()) {
            DBObject obj = (DBObject) other.queryObject.get(k);
            if (this.queryObject.containsField(k)) {
                obj.putAll((DBObject) this.queryObject.get(k));
            }
            this.queryObject.put(k, obj);
        }
        return this;
    }

}
