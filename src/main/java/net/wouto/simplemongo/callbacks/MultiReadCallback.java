package net.wouto.simplemongo.callbacks;

import com.mongodb.DBCursor;

public interface MultiReadCallback {
    
    public void onQueryDone(DBCursor result, Exception err);
    
}
