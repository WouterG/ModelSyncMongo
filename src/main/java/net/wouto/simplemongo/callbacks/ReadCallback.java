package net.wouto.simplemongo.callbacks;

import com.mongodb.DBObject;

public interface ReadCallback {
 
    public void onQueryDone(DBObject result, Exception err);
    
}
