package net.wouto.modelsync.mongo.callbacks;

import com.mongodb.BasicDBObject;

public interface WriteCallback {

    public void onQueryDone(BasicDBObject outputDocument, Exception err);
    
}
