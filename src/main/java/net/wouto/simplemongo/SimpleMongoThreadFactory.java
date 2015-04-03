package net.wouto.simplemongo;

import java.util.concurrent.ThreadFactory;

public class SimpleMongoThreadFactory implements ThreadFactory {
    
    private final String prefix;
    private int counter;
    
    public SimpleMongoThreadFactory(String prefix) {
        this.prefix = prefix;
        this.counter = 0;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, prefix + "-" + counter++);
    }
    
}
