package net.wouto.modelsync.mongo.callbacks;

import com.mongodb.client.result.UpdateResult;

public interface UpdateCallback {

    public void onQueryDone(UpdateResult result, Exception err);
    
}
