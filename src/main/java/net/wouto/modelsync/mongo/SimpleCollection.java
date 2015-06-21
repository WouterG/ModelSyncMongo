package net.wouto.modelsync.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wouto.modelsync.mongo.annotations.DBSync;
import net.wouto.modelsync.mongo.callbacks.DeleteCallback;
import net.wouto.modelsync.mongo.callbacks.LoadAllCallback;
import net.wouto.modelsync.mongo.callbacks.MultiReadCallback;
import net.wouto.modelsync.mongo.callbacks.ReadCallback;
import net.wouto.modelsync.mongo.callbacks.UpdateCallback;
import net.wouto.modelsync.mongo.callbacks.WriteCallback;
import net.wouto.modelsync.mongo.query.Query;
import net.wouto.modelsync.mongo.sync.ObjectLoadedCallback;
import net.wouto.modelsync.mongo.update.Update;
import org.apache.commons.lang.ClassUtils;
import org.bson.Document;

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

    public <T> T[] loadAllSync(Class<T> c) throws Exception {
        Constructor constructor = c.getDeclaredConstructor();
        boolean cAccess = constructor.isAccessible();
        if (!cAccess) {
            constructor.setAccessible(true);
        }
        MongoCursor cursor = this.findSync(Query.empty);
        ArrayList<T> data = new ArrayList();
        while (cursor.hasNext()) {
            DBObject obj = (DBObject) JSON.parse(((Document) cursor.next()).toJson());
            T instance = (T) constructor.newInstance();
            SimpleCollection.this.fromDBObject(instance, obj);
            data.add(instance);
        }
        constructor.setAccessible(cAccess);
        return (T[]) data.toArray((T[]) Array.newInstance(c, data.size()));
    }

    public <T> void loadAll(final Class<T> c, final LoadAllCallback callback) {
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

    private static Collection<Field> getAnnotationFields(Class<?> clazz, Class<? extends Annotation> a) {
        List<Field> fields = new ArrayList();
        while (clazz != null && clazz != Object.class) {
            Field[] fs = clazz.getDeclaredFields();
            for (Field f : fs) {
                if (f.isAnnotationPresent(a)) {
                    fields.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static final Set<Class<?>> primitives = new HashSet<Class<?>>() {
        {
            add(Boolean.class);
            add(Character.class);
            add(Byte.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(Object.class);
            add(String.class);
        }
    };

    private static Class<?> getCollectionType(Field f) {
        return (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
    }

    private static Class<?> getArrayType(Field f) {
        return f.getType().getComponentType();
    }

    private static boolean isSerializeAble(Field f) {
        Class<?> type = f.getType();
        if (type.isArray()) {
            type = type.getComponentType();
        }
        if (Collection.class.isAssignableFrom(f.getType())) {
            type = getCollectionType(f);
        }
        if (isPrimitive(type)) {
            return true;
        }
        return (!getAnnotationFields(type, DBSync.class).isEmpty());
    }

    private static boolean isPrimitive(Class<?> type) {
        if (type.isPrimitive()) {
            return true;
        }
        for (Class<?> p : primitives) {
            if (type.isAssignableFrom(p)) {
                return true;
            }
        }
        return false;
    }

    public static <T> BasicDBObject asDBObject(T instance) {
        BasicDBObject b = new BasicDBObject();
        Collection<Field> fieldMap = getAnnotationFields(instance.getClass(), DBSync.class);
        for (Field f : fieldMap) {
            try {
                DBSync saveData = f.getAnnotation(DBSync.class);
                if (saveData == null) {
                    continue;
                }
                if (!isSerializeAble(f)) {
                    continue;
                }
                boolean hadAccess = f.isAccessible();
                if (!hadAccess) {
                    f.setAccessible(true);
                }
                String fieldName = f.getName();
                if (!saveData.value().isEmpty()) {
                    fieldName = saveData.value();
                }
                if (f.getType().isArray()) { // array
                    Object[] arrayData = (Object[]) f.get(instance);
                    Class<?> arrayType = getArrayType(f);
                    if (isPrimitive(arrayType)) {
                        b.put(fieldName, f.get(instance));
                        f.setAccessible(hadAccess);
                        continue;
                    } else {
                        BasicDBList list = new BasicDBList();
                        for (Object arrayObject : arrayData) {
                            list.add(asDBObject(arrayObject));
                        }
                        b.put(fieldName, list);
                        f.setAccessible(hadAccess);
                        continue;
                    }
                } else if (Collection.class.isAssignableFrom(f.getType())) { // Collection
                    Collection collectionData = (Collection) f.get(instance);
                    Class<?> collectionType = getCollectionType(f);
                    if (isPrimitive(collectionType)) {
                        b.put(fieldName, f.get(instance));
                        f.setAccessible(hadAccess);
                        continue;
                    } else {
                        BasicDBList list = new BasicDBList();
                        for (Object collectionObject : collectionData) {
                            BasicDBObject collectionDBObject = asDBObject(collectionType.cast(collectionObject));
                            list.add(collectionDBObject);
                        }
                        b.put(fieldName, list);
                        f.setAccessible(hadAccess);
                        continue;
                    }
                } else if (!isPrimitive(f.getType())) {
                    BasicDBObject data = asDBObject(f.get(instance));
                    b.put(fieldName, data);
                    f.setAccessible(hadAccess);
                    continue;
                }
                Object o = f.get(instance);
                f.setAccessible(hadAccess);
                b.put(fieldName, o);
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return b;
    }

    public final <T> void save(T instance) {
        BasicDBObject obj = asDBObject(instance);
        Collection<Field> fields = getAnnotationFields(instance.getClass(), DBSync.class);
        String key = null;
        Object value = null;
        for (Field f : fields) {
            if (f.getAnnotation(DBSync.class).index()) {
                key = f.getName();
                value = obj.remove(f.getName());
                break;
            }
        }
        if (key == null && value == null) {
            System.out.println("Cannot save object: " + instance.getClass().getName());
            return;
        }
        Update update = new Update(obj);
        this.updateOrInsert(key, value, update);
    }

    private <T> T instantiate(Class<T> cls) throws Exception {
        final Constructor<T> constr = (Constructor<T>) cls.getConstructors()[0];
        boolean a = constr.isAccessible();
        if (!a) {
            constr.setAccessible(true);
        }
        final List<Object> params = new ArrayList();
        for (Class<?> pType : constr.getParameterTypes()) {
            params.add((pType.isPrimitive()) ? ClassUtils.primitiveToWrapper(pType).newInstance() : null);
        }
        final T instance = constr.newInstance(params.toArray());
        constr.setAccessible(a);
        return instance;
    }

    private <T> T fromDBObject(Class<T> type, DBObject data) {
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

    private <T> T fromDBObject(T instance, DBObject data) {
        Collection<Field> fields = getAnnotationFields(instance.getClass(), DBSync.class);
        for (Field f : fields) {
            DBSync load = f.getAnnotation(DBSync.class);
            if (load == null) {
                continue;
            }
            String varName = f.getName();
            if (!load.value().isEmpty()) {
                varName = load.value();
            }
            if (!data.containsField(varName)) {
                continue;
            }
            boolean accessible = f.isAccessible();
            if (!accessible) {
                f.setAccessible(true);
            }
            if (f.getType().isArray()) { // array
                Class<?> arrayType = getArrayType(f);
                if (isPrimitive(arrayType)) {
                    try {
                        f.set(instance, data.get(varName));
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        Logger.getLogger(SimpleCollection.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    f.setAccessible(accessible);
                    continue;
                }
                BasicDBList arrayData = (BasicDBList) data.get(varName);
                Collection arrayResult = new ArrayList();
                for (Object arrayObject : arrayData) {
                    DBObject arrayObjectDB = (DBObject) arrayObject;
                    Object arrayObjectOutput = fromDBObject(arrayType, arrayObjectDB);
                    arrayResult.add(arrayObjectOutput);
                }
                try {
                    f.set(instance, arrayResult.toArray());
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(SimpleCollection.class.getName()).log(Level.SEVERE, null, ex);
                }
                f.setAccessible(accessible);
                continue;
            } else if (Collection.class.isAssignableFrom(f.getType())) { // collection
                Class<?> collectionType = getCollectionType(f);
                if (isPrimitive(collectionType)) {
                    try {
                        f.set(instance, data.get(varName));
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        Logger.getLogger(SimpleCollection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    f.setAccessible(accessible);
                    continue;
                }
                BasicDBList collectionData = (BasicDBList) data.get(varName);
                Collection collectionResult = new ArrayList();
                for (Object collectionObject : collectionData) {
                    DBObject collectionObjectDB = (DBObject) collectionObject;
                    Object collectionObjectOutput = fromDBObject(collectionType, collectionObjectDB);
                    collectionResult.add(collectionObjectOutput);
                }
                try {
                    f.set(instance, collectionResult);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(SimpleCollection.class.getName()).log(Level.SEVERE, null, ex);
                }
                f.setAccessible(accessible);
                continue;
            } else if (!isPrimitive(f.getType())) {
                Object o = fromDBObject(f.getType(), (DBObject) data.get(varName));
                try {
                    f.set(instance, o);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(SimpleCollection.class.getName()).log(Level.SEVERE, null, ex);
                }
                f.setAccessible(accessible);
                continue;
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

    public final <T> void load(T instance) {
        this.load(instance, null);
    }

    public final <T> void load(final T instance, final ObjectLoadedCallback callback) {
        Field[] fields = instance.getClass().getDeclaredFields();
        Query q = new Query();
        final Class<?> cast = instance.getClass();
        boolean exc = true;
        for (Field f : fields) {
            try {
                if (!f.isAnnotationPresent(DBSync.class)) {
                    continue;
                }
                DBSync saveData = f.getAnnotation(DBSync.class);
                boolean access = f.isAccessible();
                if (!access) {
                    f.setAccessible(access);
                }
                String varName = f.getName();
                if (!saveData.value().isEmpty()) {
                    varName = saveData.value();
                }
                if (saveData.index()) {
                    Object value = f.get(instance);
                    if (varName != null && value != null) {
                        q.append(Query.equals(varName, value));
                        exc = false;
                    }
                }
                f.setAccessible(access);
            } catch (IllegalAccessException | IllegalArgumentException ex) {
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

    public MongoCollection getHandle() {
        return this.collection;
    }

}
