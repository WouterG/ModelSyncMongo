package net.wouto.modelsync.mongo.callbacks;

import com.mongodb.client.result.DeleteResult;

public interface DeleteCallback {

    public void onQueryDone(DeleteResult result, Exception err);
    
}
