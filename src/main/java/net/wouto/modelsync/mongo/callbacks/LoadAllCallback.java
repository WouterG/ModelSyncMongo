package net.wouto.modelsync.mongo.callbacks;

public interface LoadAllCallback {
 
    public <T> void onQueryDone(T[] result, Exception err);
    
}
