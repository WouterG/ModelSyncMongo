package net.wouto.simplemongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wouto.simplemongo.annotations.DBLoad;
import net.wouto.simplemongo.annotations.DBSave;
import net.wouto.simplemongo.callbacks.LoadAllCallback;
import net.wouto.simplemongo.callbacks.MultiReadCallback;
import net.wouto.simplemongo.callbacks.ReadCallback;
import net.wouto.simplemongo.callbacks.WriteCallback;
import net.wouto.simplemongo.query.Query;
import net.wouto.simplemongo.sync.ObjectLoadedCallback;
import net.wouto.simplemongo.sync.SyncedClass;
import net.wouto.simplemongo.update.Update;

public class MongoCollection {

    private MongoScheduler scheduler;
    private DBCollection collection;

    public MongoCollection(MongoScheduler scheduler, DBCollection collection) {
        this.scheduler = scheduler;
        this.collection = collection;
    }

    public WriteResult updateSync(Query q, Update u) throws Exception {
        return this.collection.update(q.getQuery(), u.getUpdateQuery());
    }

    public void update(Query q, Update u) {
        this.update(q, u, null);
    }

    public void update(final Query q, final Update u, final WriteCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    WriteResult wr = MongoCollection.this.updateSync(q, u);
                    if (callback != null) {
                        callback.onQueryDone(wr, null);
                    }
                } catch (Exception ex) {
                    if (callback != null) {
                        callback.onQueryDone(null, ex);
                    }
                }
            }

        });
    }

    public WriteResult updateSync(Query q, Update u, boolean upsert, boolean multi) throws Exception {
        return this.collection.update(q.getQuery(), u.getUpdateQuery(), upsert, multi);
    }

    public void update(Query q, Update u, boolean upsert, boolean multi) {
        this.update(q, u, upsert, multi, null);
    }

    public void update(final Query q, final Update u, final boolean upsert, final boolean multi, final WriteCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    WriteResult wr = MongoCollection.this.updateSync(q, u, upsert, multi);
                    if (callback != null) {
                        callback.onQueryDone(wr, null);
                    }
                } catch (Exception ex) {
                    if (callback != null) {
                        callback.onQueryDone(null, ex);
                    }
                }
            }

        });
    }

    public WriteResult updateOrInsertSync(String key, Object value, Update set) throws Exception {
        set.append(Update.setOnInsert(key, value));
        return this.collection.update(new BasicDBObject(key, value), set.getUpdateQuery(), true, false);
    }

    public void updateOrInsert(String key, Object value, Update set) {
        this.updateOrInsert(key, value, set, null);
    }

    public void updateOrInsert(final String key, final Object value, final Update set, final WriteCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    WriteResult wr = MongoCollection.this.updateOrInsertSync(key, value, set);
                    if (callback != null) {
                        callback.onQueryDone(wr, null);
                    }
                } catch (Exception ex) {
                    if (callback != null) {
                        callback.onQueryDone(null, ex);
                    }
                }
            }

        });
    }

    public WriteResult insertSync(BasicDBObject obj) throws Exception {
        return this.collection.insert(obj);
    }

    public void insert(BasicDBObject obj) {
        this.insert(obj, null);
    }

    public void insert(final BasicDBObject obj, final WriteCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    WriteResult wr = MongoCollection.this.insertSync(obj);
                    if (callback != null) {
                        callback.onQueryDone(wr, null);
                    }
                } catch (Exception ex) {
                    if (callback != null) {
                        callback.onQueryDone(null, ex);
                    }
                }
            }

        });
    }

    public DBObject findOneSync(Query q) throws Exception {
        return this.collection.findOne(q.getQuery());
    }

    public void findOne(final Query q, final ReadCallback callback) {
        this.scheduler.doRead(new Runnable() {

            @Override
            public void run() {
                try {
                    DBObject d = MongoCollection.this.findOneSync(q);
                    if (callback != null) {
                        callback.onQueryDone(d, null);
                    }
                } catch (Exception ex) {
                    if (callback != null) {
                        callback.onQueryDone(null, ex);
                    }
                }
            }

        });
    }

    public DBCursor findSync(Query q) throws Exception {
        return this.collection.find(q.getQuery());
    }

    public void find(final Query q, final MultiReadCallback callback) {
        this.scheduler.doRead(new Runnable() {

            @Override
            public void run() {
                try {
                    DBCursor c = MongoCollection.this.findSync(q);
                    if (callback != null) {
                        callback.onQueryDone(c, null);
                    }
                } catch (Exception ex) {
                    if (callback != null) {
                        callback.onQueryDone(null, ex);
                    }
                }
            }

        });
    }

    public DBObject findAndRemoveSync(Query q) throws Exception {
        return this.collection.findAndRemove(q.getQuery());
    }

    public void findAndRemove(final Query q, final ReadCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    DBObject obj = MongoCollection.this.findAndRemoveSync(q);
                    if (callback != null) {
                        callback.onQueryDone(obj, null);
                    }
                } catch (Exception ex) {
                    if (callback != null) {
                        callback.onQueryDone(null, ex);
                    }
                }
            }

        });
    }

    public WriteResult removeSync(Query q) throws Exception {
        return this.collection.remove(q.getQuery());
    }

    public void remove(Query q) {
        this.remove(q, null);
    }

    public void remove(final Query q, final WriteCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    WriteResult wr = MongoCollection.this.removeSync(q);
                    if (callback != null) {
                        callback.onQueryDone(wr, null);
                    }
                } catch (Exception ex) {
                    if (callback != null) {
                        callback.onQueryDone(null, ex);
                    }
                }
            }

        });
    }

    public <T extends SyncedClass> T[] loadAllSync(Class<T> c) throws Exception {
        Constructor constructor = c.getDeclaredConstructor();
        boolean cAccess = constructor.isAccessible();
        if (!cAccess) {
            constructor.setAccessible(true);
        }
        DBCursor cursor = this.findSync(Query.empty);
        ArrayList<T> data = new ArrayList();
        for (DBObject obj : cursor) {
            T instance = (T) constructor.newInstance();
            MongoCollection.this.fromDBObject(instance, obj);
            data.add(instance);
        }
        constructor.setAccessible(cAccess);
        return (T[]) data.toArray((T[]) Array.newInstance(c, data.size()));
    }

    public <T extends SyncedClass> void loadAll(final Class<T> c, final LoadAllCallback callback) {
        this.scheduler.doRead(new Runnable() {
            @Override
            public void run() {
                try {
                    T[] t = MongoCollection.this.loadAllSync(c);
                    if (callback != null) {
                        callback.onQueryDone(t, null);
                    }
                } catch (Exception ex) {
                    if (callback != null) {
                        callback.onQueryDone(null, ex);
                    }
                }
            }
        });
    }

    public final <T extends SyncedClass> void save(T instance) {
        Query index = new Query();
        Update update = new Update();
        String key = null;
        Object value = null;
        Method[] methods = instance.getClass().getMethods();
        for (Method m : methods) {
            try {
                if (!m.isAnnotationPresent(DBSave.class)) {
                    continue;
                }
                DBSave saveData = m.getAnnotation(DBSave.class);
                if (saveData == null) {
                    continue;
                }
                if (m.getReturnType().equals(Void.TYPE)) {
                    continue;
                }
                if (m.getParameterTypes().length > 0) {
                    continue;
                }
                boolean hadAccess = m.isAccessible();
                if (!hadAccess) {
                    m.setAccessible(true);
                }
                Object o = m.invoke(instance);
                m.setAccessible(hadAccess);
                if (saveData.index() && o != null && !saveData.value().equals("_id")) {
                    key = saveData.value();
                    value = o;
                    index.append(Query.equals(saveData.value(), o));
                } else if (!saveData.value().equals("_id")) {
                    update.append(Update.set(saveData.value(), o));
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        this.updateOrInsert(key, value, update);
    }

    private final <T extends SyncedClass> T fromDBObject(Class<T> type, DBObject data) {
        try {
            Constructor c = type.getDeclaredConstructor();
            boolean a = c.isAccessible();
            if (!a) {
                c.setAccessible(true);
            }
            T obj = (T) c.newInstance();
            return fromDBObject(obj, data);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(MongoCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private final <T extends SyncedClass> T fromDBObject(T instance, DBObject data) {
        Method[] methods = instance.getClass().getMethods();
        for (Method m : methods) {
            if (!m.isAnnotationPresent(DBLoad.class)) {
                continue;
            }
            DBLoad load = m.getAnnotation(DBLoad.class);
            if (load == null) {
                continue;
            }
            if (m.getParameterTypes().length != 1) {
                continue;
            }
            boolean accessible = m.isAccessible();
            if (!accessible) {
                m.setAccessible(true);
            }
            boolean normal = true;
            if (m.getParameterTypes()[0].isInstance(SyncedClass.class)) {
                normal = false;
            }
            Object o = data.get(load.value());
            try {
                if (normal) {
                    m.invoke(instance, o);
                } else {
                    m.invoke(instance, MongoCollection.this.fromDBObject(((Class<? extends SyncedClass>) m.getParameterTypes()[0]), (DBObject) o));
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(MongoCollection.class.getName()).log(Level.SEVERE, null, ex);
            }
            m.setAccessible(accessible);
        }
        return instance;
    }

    public final <T extends SyncedClass> void load(T instance) {
        this.load(instance, null);
    }

    public final <T extends SyncedClass> void load(final T instance, final ObjectLoadedCallback callback) {
        Method[] methods = instance.getClass().getMethods();
        Query q = new Query();
        final Class<?> cast = instance.getClass();
        boolean exc = true;
        for (Method m : methods) {
            try {
                if (!m.isAnnotationPresent(DBSave.class)) {
                    continue;
                }
                DBSave saveData = m.getAnnotation(DBSave.class);
                if (saveData == null) {
                    continue;
                }
                if (m.getParameterTypes().length != 0) {
                    continue;
                }
                boolean access = m.isAccessible();
                if (!access) {
                    m.setAccessible(access);
                }
                if (saveData.index()) {
                    Object value = m.invoke(instance);
                    if (saveData.value() != null && value != null) {
                        q.append(Query.equals(saveData.value(), value));
                        exc = false;
                    }
                }
                m.setAccessible(access);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        if (exc) {
            System.out.println("No indexes set to query instance of " + instance.getClass().getName());
            callback.onObjectLoaded(cast.cast(instance));
            return;
        }
        this.findOne(q, new ReadCallback() {

            @Override
            public void onQueryDone(DBObject result, Exception err) {
                if (err != null) {
                    err.printStackTrace();
                } else if (result != null) {
                    MongoCollection.this.fromDBObject(instance, result);
                    callback.onObjectLoaded(cast.cast(instance));
                } else {
                    System.out.println("Failed loading data for " + instance.getClass().getName());
                }
            }

        });
    }

    public static <T extends SyncedClass> BasicDBObject asDBObject(T instance) {
        BasicDBObject b = new BasicDBObject();
        Method[] methods = instance.getClass().getMethods();
        for (Method m : methods) {
            try {
                DBSave saveData = m.getAnnotation(DBSave.class);
                if (saveData == null) {
                    continue;
                }
                if (m.getReturnType().equals(Void.TYPE)) {
                    continue;
                }
                if (m.getParameterTypes().length > 0) {
                    continue;
                }
                boolean accessible = m.isAccessible();
                if (!accessible) {
                    m.setAccessible(true);
                }
                Object o = m.invoke(instance);
                if (!saveData.value().equals("_id")) {
                    Object d = o;
                    if (o instanceof SyncedClass) {
                        d = asDBObject((SyncedClass) o);
                    }
                    b.put(saveData.value(), d);
                }
                m.setAccessible(accessible);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        return b;
    }
}
