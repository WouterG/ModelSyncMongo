package net.wouto.simplemongo.callbacks;

import com.mongodb.client.result.DeleteResult;

public interface DeleteCallback {

    public void onQueryDone(DeleteResult result, Exception err);
    
}
