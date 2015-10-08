package net.wouto.modelsync.mongo.callbacks;

import com.mongodb.DBObject;

public interface ReadCallback<T> {
 
    public void onQueryDone(DBObject result, Exception err);
    
}
