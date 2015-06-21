package net.wouto.modelsync.mongo.sync;

public interface ObjectLoadedCallback<T> {
    
    public void onObjectLoaded(T object);
    
}
