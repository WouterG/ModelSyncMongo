package net.wouto.modelsync.mongo.callbacks;

public interface LoadMultiCallback<T> {
 
    public <T> void onQueryDone(T[] result, Exception err);
    
}
