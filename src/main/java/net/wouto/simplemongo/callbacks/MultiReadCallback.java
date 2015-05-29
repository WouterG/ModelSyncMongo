package net.wouto.simplemongo.callbacks;

import com.mongodb.client.MongoCursor;

public interface MultiReadCallback {
    
    public void onQueryDone(MongoCursor result, Exception err);
    
}
