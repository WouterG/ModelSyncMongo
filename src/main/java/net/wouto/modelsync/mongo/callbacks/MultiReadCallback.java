package net.wouto.modelsync.mongo.callbacks;

import com.mongodb.DBObject;

public interface MultiReadCallback {
    
    public void onQueryDone(DBObject[] result, Exception err);
    
}
