package net.wouto.modelsync.mongo.callbacks;

import org.bson.Document;

public interface DocumentWriteCallback {
    
    public void onQueryDone(Document written, Exception ex);
    
}
