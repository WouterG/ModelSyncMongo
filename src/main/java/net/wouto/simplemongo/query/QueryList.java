package net.wouto.simplemongo.query;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.Collection;

/**
 * A collection of queries for $or / $and etc.
 * @author Wouter
 */
public class QueryList {
    
    private BasicDBList queryList;
    private String operator;
    private String key;
    
    public QueryList(String operator) {
        this(null, operator);
    }
    
    public QueryList(String key, String operator) {
        this.key = key;
        this.queryList = new BasicDBList();
        this.operator = operator;
    }
    
    public DBObject getQuery() {
        if (key == null) {
            return (DBObject) new BasicDBObject(operator, this.queryList);
        } else {
            return (DBObject) new BasicDBObject(key, new BasicDBObject(operator, queryList));
        }
    }
    
    public QueryList add(Object o) {
        this.queryList.add(o);
        return this;
    }
    
    public QueryList add(BasicDBObject query) {
        this.queryList.add(query);
        return this;
    }
    
    public QueryList add(Query query) {
        add(query.getQuery());
        return this;
    }
    
    public QueryList addAll(Collection<Query> queries) {
        for (Query q : queries) {
            add(q);
        }
        return this;
    }
    
    public QueryList addAll(QueryList queries) {
        this.queryList.addAll(queries.queryList);
        return this;
    }
    
    public Query finish() {
        return new Query(this.getQuery());
    }
    
}
