package net.wouto.modelsync.mongo.query;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import java.util.Collection;
import org.bson.Document;

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
    
    public Document getQuery() {
        if (key == null) {
            return (Document) new Document(operator, this.queryList);
        } else {
            return (Document) new Document(key, new Document(operator, queryList));
        }
    }
    
    public QueryList add(Object o) {
        this.queryList.add(o);
        return this;
    }
    
    public QueryList add(Document query) {
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
