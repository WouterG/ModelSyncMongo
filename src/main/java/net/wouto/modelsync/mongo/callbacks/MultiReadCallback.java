package net.wouto.modelsync.mongo.callbacks;

import org.bson.Document;

public interface MultiReadCallback {
    
    public void onQueryDone(Document[] result, Exception err);
    
}
