package net.wouto.simplemongo.callbacks;

public interface LoadAllCallback {
 
    public <T> void onQueryDone(T[] result, Exception err);
    
}
