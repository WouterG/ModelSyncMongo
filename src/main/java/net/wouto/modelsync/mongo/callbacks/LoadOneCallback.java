package net.wouto.modelsync.mongo.callbacks;

public interface LoadOneCallback<T> {
    
    public void onQueryDone(T object, Exception err);
    
}
