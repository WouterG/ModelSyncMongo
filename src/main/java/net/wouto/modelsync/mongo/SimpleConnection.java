package net.wouto.modelsync.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import java.net.UnknownHostException;

public class SimpleConnection {
    
    private String ip;
    private int port;
    
    private MongoClient client;
    private SimpleScheduler scheduler;
    
    private String username;
    private String password;
    private String authDB;
    
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
        this.username = null;
        this.password = null;
        this.authDB = null;
    }
    
    public void setAuthentication(String username, String password, String database) {
        this.username = username;
        this.password = password;
        this.authDB = database;
    }
    
    public void connect() throws UnknownHostException {
        if (this.ip.endsWith("/")) {
            this.ip = this.ip.substring(0, this.ip.length() - 1);
        }
        MongoClientURI uri;
        if (this.username != null && this.password != null && authDB != null) {
            uri = new MongoClientURI("mongodb://" + this.username + ":" + this.password + "@" + this.ip + ":" + this.port + "/?authSource=" + this.authDB);
        } else {
            uri = new MongoClientURI("mongodb://" + this.ip + ":" + this.port + "/");
        }
        this.client = new MongoClient(uri);
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
