package net.wouto.simplemongo.sync;

public interface ObjectLoadedCallback<T> {
    
    public void onObjectLoaded(T object);
    
}
