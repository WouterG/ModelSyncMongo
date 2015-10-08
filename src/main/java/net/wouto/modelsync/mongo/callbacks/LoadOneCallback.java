package net.wouto.modelsync.mongo.callbacks;

public interface LoadOneCallback<T> {
    
    public <T> void onQueryDone(T object, Exception err);
    
}
