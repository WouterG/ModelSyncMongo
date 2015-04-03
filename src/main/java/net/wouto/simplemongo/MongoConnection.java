package net.wouto.simplemongo;

import com.mongodb.MongoClient;
import java.net.UnknownHostException;

public class MongoConnection {
    
    private String ip;
    private int port;
    
    private MongoClient client;
    
    public MongoConnection() {
        this("localhost");
    }
    
    public MongoConnection(String ip) {
        this(ip, 27017);
    }
    
    public MongoConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public void connect() throws UnknownHostException {
        this.client = new MongoClient(this.ip, this.port);
    }
    
    public void disconnect() {
        this.client.close();
        this.client = null;
    }
    
    public String getIp() {
        return this.ip;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public MongoClient getConnection() {
        return this.client;
    }
    
    public boolean isConnected() {
        return (this.client != null);
    }
    
    
}
