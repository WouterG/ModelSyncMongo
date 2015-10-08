package net.wouto.modelsync.mongo.callbacks;

import org.bson.Document;

public abstract class FindAndUpdateCallback {
    
    public abstract void onQueryDone(Document result, Exception ex);
    
}
