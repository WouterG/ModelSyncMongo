package net.wouto.simplemongo.callbacks;

import net.wouto.simplemongo.sync.SyncedClass;

public interface LoadAllCallback {
 
    public <T extends SyncedClass> void onQueryDone(T[] result, Exception err);
    
}
