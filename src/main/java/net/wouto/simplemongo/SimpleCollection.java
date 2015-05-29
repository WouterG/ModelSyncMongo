package net.wouto.simplemongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wouto.simplemongo.annotations.DBSync;
import net.wouto.simplemongo.callbacks.DeleteCallback;
import net.wouto.simplemongo.callbacks.LoadAllCallback;
import net.wouto.simplemongo.callbacks.MultiReadCallback;
import net.wouto.simplemongo.callbacks.ReadCallback;
import net.wouto.simplemongo.callbacks.UpdateCallback;
import net.wouto.simplemongo.callbacks.WriteCallback;
import net.wouto.simplemongo.query.Query;
import net.wouto.simplemongo.sync.ObjectLoadedCallback;
import net.wouto.simplemongo.sync.SyncedClass;
import net.wouto.simplemongo.update.Update;
import org.apache.commons.lang.ClassUtils;

public class SimpleCollection {

    private SimpleScheduler scheduler;
    private MongoCollection collection;

    public SimpleCollection(SimpleScheduler scheduler, MongoCollection collection) {
        this.scheduler = scheduler;
        this.collection = collection;
    }

    public UpdateResult updateSync(Query q, Update u) throws Exception {
        return this.collection.updateOne((BasicDBObject) q.getQuery(), (BasicDBObject) u.getUpdateQuery());
    }

    public void update(Query q, Update u) {
        this.update(q, u, null);
    }

    public void update(final Query q, final Update u, final UpdateCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    UpdateResult wr = SimpleCollection.this.updateSync(q, u);
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

    public UpdateResult updateSync(Query q, Update u, boolean upsert, boolean multi) throws Exception {
        UpdateOptions options = new UpdateOptions();
        options.upsert(upsert);
        if (multi) {
            return this.collection.updateMany((BasicDBObject) q.getQuery(), (BasicDBObject) u.getUpdateQuery(), options);
        } else {
            return this.collection.updateOne((BasicDBObject) q.getQuery(), (BasicDBObject) u.getUpdateQuery(), options);
        }
    }

    public void update(Query q, Update u, boolean upsert, boolean multi) {
        this.update(q, u, upsert, multi, null);
    }

    public void update(final Query q, final Update u, final boolean upsert, final boolean multi, final UpdateCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    UpdateResult wr = SimpleCollection.this.updateSync(q, u, upsert, multi);
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

    public UpdateResult updateOrInsertSync(String key, Object value, Update set) throws Exception {
        set.append(Update.setOnInsert(key, value));
        UpdateOptions u = new UpdateOptions();
        u.upsert(true);
        return this.collection.updateOne(new BasicDBObject(key, value), (BasicDBObject) set.getUpdateQuery(), u);
    }

    public void updateOrInsert(String key, Object value, Update set) {
        this.updateOrInsert(key, value, set, null);
    }

    public void updateOrInsert(final String key, final Object value, final Update set, final UpdateCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    UpdateResult wr = SimpleCollection.this.updateOrInsertSync(key, value, set);
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

    public void insertSync(BasicDBObject obj) throws Exception {
        this.collection.insertOne(obj);
    }

    public void insert(BasicDBObject obj) {
        this.insert(obj, null);
    }

    public void insert(final BasicDBObject obj, final WriteCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    SimpleCollection.this.insertSync(obj);
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

    public DBObject findOneSync(Query q) throws Exception {
        MongoCursor cursor = this.collection.find((BasicDBObject) q.getQuery()).iterator();
        Object o = cursor.next();
        if (o == null) {
            return null;
        }
        return (DBObject) o;
    }

    public void findOne(final Query q, final ReadCallback callback) {
        this.scheduler.doRead(new Runnable() {

            @Override
            public void run() {
                try {
                    DBObject d = SimpleCollection.this.findOneSync(q);
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

    public MongoCursor findSync(Query q) throws Exception {
        return this.collection.find((BasicDBObject) q.getQuery()).iterator();
    }

    public void find(final Query q, final MultiReadCallback callback) {
        this.scheduler.doRead(new Runnable() {

            @Override
            public void run() {
                try {
                    MongoCursor c = SimpleCollection.this.findSync(q);
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

    public DBObject findOneAndRemoveSync(Query q) throws Exception {
        Object o = this.collection.findOneAndDelete((BasicDBObject) q.getQuery());
        if (o == null) {
            return null;
        }
        return (DBObject) o;
    }

    public void findAndRemove(final Query q, final ReadCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    DBObject obj = SimpleCollection.this.findOneAndRemoveSync(q);
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

    public DeleteResult removeSync(Query q) throws Exception {
        return this.collection.deleteMany((BasicDBObject) q.getQuery());
    }

    public void remove(Query q) {
        this.remove(q, null);
    }

    public void remove(final Query q, final DeleteCallback callback) {
        this.scheduler.doWrite(new Runnable() {

            @Override
            public void run() {
                try {
                    DeleteResult wr = SimpleCollection.this.removeSync(q);
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
        MongoCursor cursor = this.findSync(Query.empty);
        ArrayList<T> data = new ArrayList();
        while (cursor.hasNext()) {
            DBObject obj = (DBObject) cursor.next();
            T instance = (T) constructor.newInstance();
            SimpleCollection.this.fromDBObject(instance, obj);
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
                    T[] t = SimpleCollection.this.loadAllSync(c);
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
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if (!f.isAnnotationPresent(DBSync.class)) {
                    continue;
                }
                DBSync saveData = f.getAnnotation(DBSync.class);
                if (saveData == null) {
                    continue;
                }
                boolean hadAccess = f.isAccessible();
                if (!hadAccess) {
                    f.setAccessible(true);
                }
                Object o = f.get(instance);
                String fieldName = f.getName();
                if (!saveData.value().isEmpty()) {
                    fieldName = saveData.value();
                }
                f.setAccessible(hadAccess);
                if (saveData.index()) {
                    key = fieldName;
                    value = o;
                    index.append(Query.equals(fieldName, o));
                } else if (!fieldName.equals("_id")) {
                    update.append(Update.set(fieldName, o));
                }
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        this.updateOrInsert(key, value, update);
    }

    private <T> T instantiate(Class<T> cls) throws Exception {
        final Constructor<T> constr = (Constructor<T>) cls.getConstructors()[0];
        boolean a = constr.isAccessible();
        if (!a) {
            constr.setAccessible(true);
        }
        final List<Object> params = new ArrayList<Object>();
        for (Class<?> pType : constr.getParameterTypes()) {
            params.add((pType.isPrimitive()) ? ClassUtils.primitiveToWrapper(pType).newInstance() : null);
        }
        final T instance = constr.newInstance(params.toArray());
        constr.setAccessible(a);
        return instance;
    }

    private final <T extends SyncedClass> T fromDBObject(Class<T> type, DBObject data) {
        try {
            T obj = (T) instantiate(type);
            return fromDBObject(obj, data);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(SimpleCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SimpleCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private final <T extends SyncedClass> T fromDBObject(T instance, DBObject data) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (!f.isAnnotationPresent(DBSync.class)) {
                continue;
            }
            DBSync load = f.getAnnotation(DBSync.class);
            if (load == null) {
                continue;
            }
            boolean accessible = f.isAccessible();
            if (!accessible) {
                f.setAccessible(true);
            }
            String varName = f.getName();
            if (!load.value().isEmpty()) {
                varName = load.value();
            }
            Object o = data.get(varName);
            try {
                f.set(instance, o);
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                Logger.getLogger(SimpleCollection.class.getName()).log(Level.SEVERE, null, ex);
            }
            f.setAccessible(accessible);
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
                if (!m.isAnnotationPresent(DBSync.class)) {
                    continue;
                }
                DBSync saveData = m.getAnnotation(DBSync.class);
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
                    SimpleCollection.this.fromDBObject(instance, result);
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
                DBSync saveData = m.getAnnotation(DBSync.class);
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

    public MongoCollection getHandle() {
        return this.collection;
    }

}
