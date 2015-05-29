package net.wouto.simplemongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class SimpleConnection {
    
    private String ip;
    private int port;
    
    private MongoClient client;
    private SimpleScheduler scheduler;
    
    private MongoCredential credential;
    
    public SimpleConnection() {
        this("localhost");
    }
    
    public SimpleConnection(String ip) {
        this(ip, 27017);
    }
    
    public SimpleConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.scheduler = new SimpleScheduler(this);
        this.credential = null;
    }
    
    public void setAuthentication(String username, String password, String database) {
        this.credential = MongoCredential.createCredential(username, database, password.toCharArray());
    }
    
    public void connect() throws UnknownHostException {
        ServerAddress address = new ServerAddress(this.ip, this.port);
        if (this.credential != null) {
            this.client = new MongoClient(address, Arrays.asList(this.credential));
        } else {
            this.client = new MongoClient(address);
        }
    }
    
    public void disconnect() {
        this.client.close();
        this.client = null;
    }
    
    public SimpleScheduler getScheduler() {
        return this.scheduler;
    }
    
    public SimpleCollection getCollection(String database, String collection) {
        if (this.client == null) {
            return null;
        }
        return this.scheduler.getCollection(database, collection);
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
