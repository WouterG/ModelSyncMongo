package net.wouto.simplemongo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleScheduler {
    
    private final SimpleConnection connection;
    
    private final ExecutorService writeThreads;
    private final ExecutorService readThreads;
    
    SimpleScheduler(SimpleConnection connection) {
        this.writeThreads = Executors.newCachedThreadPool(new SimpleMongoThreadFactory("SimpleMongo-Write"));
        this.readThreads = Executors.newCachedThreadPool(new SimpleMongoThreadFactory("SimpleMongo-Read"));
        this.connection = connection;
    }
    
    public SimpleConnection getConnection() {
        return this.connection;
    }
    
    public SimpleCollection getCollection(String db, String collection) {
        return new SimpleCollection(this, this.connection.getConnection().getDatabase(db).getCollection(collection));
    }
    
    public void doWrite(Runnable r) {
        this.writeThreads.execute(r);
    }
    
    public void doRead(Runnable r) {
        this.readThreads.execute(r);
    }
    
}
