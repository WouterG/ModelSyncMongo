package net.wouto.simplemongo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MongoScheduler {
    
    private final MongoConnection connection;
    
    private final ExecutorService writeThreads;
    private final ExecutorService readThreads;
    
    public MongoScheduler(MongoConnection connection) {
        this.writeThreads = Executors.newCachedThreadPool(new SimpleMongoThreadFactory("SimpleMongo-Write"));
        this.readThreads = Executors.newCachedThreadPool(new SimpleMongoThreadFactory("SimpleMongo-Read"));
        this.connection = connection;
    }
    
    public MongoConnection getConnection() {
        return this.connection;
    }
    
    public MongoCollection getCollection(String db, String collection) {
        return new MongoCollection(this, this.connection.getConnection().getDB(db).getCollection(collection));
    }
    
    public void doWrite(Runnable r) {
        this.writeThreads.execute(r);
    }
    
    public void doRead(Runnable r) {
        this.readThreads.execute(r);
    }
    
}
