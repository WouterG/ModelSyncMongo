package net.wouto.modelsync.mongo.callbacks;

import com.mongodb.DBObject;

public interface ReadCallback {
 
    public void onQueryDone(DBObject result, Exception err);
    
}
