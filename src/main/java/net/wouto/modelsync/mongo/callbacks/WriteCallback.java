package net.wouto.modelsync.mongo.callbacks;

import org.bson.Document;

public interface WriteCallback {

    public void onQueryDone(Document outputDocument, Exception err);
    
}
