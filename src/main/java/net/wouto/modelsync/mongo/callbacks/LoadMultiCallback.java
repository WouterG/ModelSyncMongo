package net.wouto.modelsync.mongo.callbacks;

public interface LoadMultiCallback<T> {
 
    public void onQueryDone(T[] result, Exception err);
    
}
