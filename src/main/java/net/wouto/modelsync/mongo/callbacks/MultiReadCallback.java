package net.wouto.modelsync.mongo.callbacks;

import com.mongodb.client.MongoCursor;

public interface MultiReadCallback {
    
    public void onQueryDone(MongoCursor result, Exception err);
    
}
