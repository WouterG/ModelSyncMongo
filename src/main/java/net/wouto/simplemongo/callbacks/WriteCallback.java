package net.wouto.simplemongo.callbacks;

import com.mongodb.WriteResult;

public interface WriteCallback {

    public void onQueryDone(WriteResult result, Exception err);
    
}
