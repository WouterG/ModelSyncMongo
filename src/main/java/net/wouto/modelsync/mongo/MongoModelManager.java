package net.wouto.modelsync.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.lang.reflect.ParameterizedType;
import net.wouto.modelsync.mongo.callbacks.DeleteCallback;
import net.wouto.modelsync.mongo.callbacks.LoadAllCallback;
import net.wouto.modelsync.mongo.callbacks.MultiReadCallback;
import net.wouto.modelsync.mongo.callbacks.ReadCallback;
import net.wouto.modelsync.mongo.callbacks.UpdateCallback;
import net.wouto.modelsync.mongo.callbacks.WriteCallback;
import net.wouto.modelsync.mongo.query.Query;
import net.wouto.modelsync.mongo.update.Update;

public class MongoModelManager<T> {

    private Class<T> type;
    private SimpleCollection collection;

    public MongoModelManager(SimpleCollection collection) {
        this.collection = collection;
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public MongoCollection getHandle() {
        return this.collection.getHandle();
    }

    public void loadAll(LoadAllCallback callback) {
        this.collection.loadAll(type, callback);
    }

    public T[] loadAllSync() throws Exception {
        return (T[]) this.collection.loadAllSync(type);
    }

    public void remove(Query q, DeleteCallback callback) {
        this.collection.remove(q, callback);
    }

    public void remove(Query q) {
        this.collection.remove(q);
    }

    public DeleteResult removeSync(Query q) throws Exception {
        return this.collection.removeSync(q);
    }

    public void findAndRemove(Query q, ReadCallback callback) {
        this.collection.findAndRemove(q, callback);
    }

    public DBObject findOneAndRemoveSync(Query q) throws Exception {
        return this.collection.findOneAndRemoveSync(q);
    }

    public void find(Query q, MultiReadCallback callback) {
        this.collection.find(q, callback);
    }

    public MongoCursor findSync(Query q) throws Exception {
        return this.collection.findSync(q);
    }

    public void findOne(Query q, ReadCallback callback) {
        this.collection.findOne(q, callback);
    }

    public DBObject findOneSync(Query q) throws Exception {
        return this.collection.findOneSync(q);
    }

    public void insert(BasicDBObject obj, WriteCallback callback) {
        this.collection.insert(obj, callback);
    }

    public void insert(BasicDBObject obj) {
        this.collection.insert(obj);
    }

    public void insertSync(BasicDBObject obj) throws Exception {
        this.collection.insertSync(obj);
    }

    public void updateOrInsert(String key, Object value, Update set, UpdateCallback callback) {
        this.collection.updateOrInsert(key, value, set, callback);
    }

    public void updateOrInsert(String key, Object value, Update set) {
        this.collection.updateOrInsert(key, value, set);
    }

    public UpdateResult updateOrInsertSync(String key, Object value, Update set) throws Exception {
        return this.collection.updateOrInsertSync(key, value, set);
    }

    public void update(Query q, Update u, boolean upsert, boolean multi, UpdateCallback callback) {
        this.collection.update(q, u, upsert, multi, callback);
    }

    public void update(Query q, Update u, boolean upsert, boolean multi) {
        this.collection.update(q, u, upsert, multi);
    }

    public UpdateResult updateSync(Query q, Update u, boolean upsert, boolean multi) throws Exception {
        return this.collection.updateSync(q, u, upsert, multi);
    }

    public void update(Query q, Update u, UpdateCallback callback) {
        this.collection.update(q, u, callback);
    }

    public void update(Query q, Update u) {
        this.collection.update(q, u);
    }

    public UpdateResult updateSync(Query q, Update u) throws Exception {
        return this.collection.updateSync(q, u);
    }

}
