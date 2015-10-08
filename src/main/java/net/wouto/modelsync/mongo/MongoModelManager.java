package net.wouto.modelsync.mongo;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import net.wouto.modelsync.mongo.callbacks.*;
import net.wouto.modelsync.mongo.query.Query;
import net.wouto.modelsync.mongo.update.Update;
import org.bson.Document;

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
    
    public SimpleCollection getLibraryHandle() {
        return this.collection;
    }
    
    public void save(T object) {
        this.collection.save(object);
    }
    
    public void saveMany(T[] objects) {
        for (T t : objects) {
            this.save(t);
        }
    }

    public void loadAll(LoadMultiCallback callback) {
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

    public void findAndRemove(Query q, final LoadOneCallback callback) {
        this.collection.findAndRemove(q, new ReadCallback() {

            @Override
            public void onQueryDone(DBObject result, Exception err) {
                if (err != null) {
                    callback.onQueryDone(null, err);
                } else {
                    callback.onQueryDone(MongoModelManager.this.collection.fromDBObject(MongoModelManager.this.type, result), err);
                }
            }
            
        });
    }

    public DBObject findOneAndRemoveSync(Query q) throws Exception {
        return this.collection.findOneAndRemoveSync(q);
    }

    public void find(Query q, final LoadMultiCallback callback) {
        this.collection.find(q, new MultiReadCallback() {

            @Override
            public void onQueryDone(DBObject[] result, Exception err) {
                if (err != null) {
                    callback.onQueryDone(null, err);
                } else {
                    List<T> objects = new ArrayList();
                    for (DBObject r : result) {
                        objects.add(MongoModelManager.this.collection.fromDBObject(type, r));
                    }
                    callback.onQueryDone((T[])objects.toArray((T[])Array.newInstance(type, objects.size())), err);
                }
            }
            
        });
    }

    public MongoCursor findSync(Query q) throws Exception {
        return this.collection.findSync(q);
    }

    public void findOne(Query q, final LoadOneCallback callback) {
        this.collection.findOne(q, new ReadCallback() {

            @Override
            public void onQueryDone(DBObject result, Exception err) {
                if (err != null) {
                    callback.onQueryDone(null, err);
                } else {
                    callback.onQueryDone(MongoModelManager.this.collection.fromDBObject(type, result), err);
                }
            }
            
        });
    }

    public DBObject findOneSync(Query q) throws Exception {
        return this.collection.findOneSync(q);
    }

    public void insert(Document obj, DocumentWriteCallback callback) {
        this.collection.insert(obj, callback);
    }

    public void insert(Document obj) {
        this.collection.insert(obj);
    }

    public void insertSync(Document obj) throws Exception {
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
