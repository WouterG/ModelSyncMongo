# SimpleMongo
A simplified MongoDB interaction library, featuring Query building and object syncing

## Installing
There's currently no Maven repository setup yet, so to use the project you'll have to download it, and either compile it and add the jar to your project as dependency, or if you're using maven simply link to the project.

## Usage

#### Opening connection
First off you want to get a `MongoConnection` instance, and connect it to your server
```java
    private MongoConnection connection;
    
    public void makeConnection() throws UnknownHostException {
        connection = new MongoConnection("mongo.domain.com", 27017); // defaults to "localhost" and 27017
        connection.connect(); // throws UnknownHostException
    }
```

#### Getting a scheduler instance
Once you've got your `MongoConnection` instance, which we'll further refer to as `connection` we can get a `MongoScheduler` instance. The scheduler is used to run all tasks in it's own threaded asynchronous environment (or in sync if the specific sync marked methods are used).
```java
    private MongoScheduler scheduler;
    
    public void makeScheduler() {
        scheduler = new MongoScheduler(connection);
    }
```

#### Getting a collection instance
Now you can get a `MongoCollection` instance from the scheduler.
```java
    private MongoCollection collection;
    
    public void initCollection(String db) {
        collection = scheduler.getCollection(db, "Users");
    }
```

#### Querying data from the database
Now you can start grabbing data from the database. A few examples:

**Querying a single document**
```java
    Query getVerifiedWouter = Query.and()
            .add(Query.equals("username", "WouterG"))
            .add(Query.not().add(Query.size("verifiedEmails", 0)))
            .finish();

    collection.findOne(getVerifiedWouter, new ReadCallback() {

        @Override
        public void onQueryDone(DBObject result, Exception err) {
            if (err != null) {
                err.printStackTrace();
            } else {
                UserManager.loadFromDBObject(result);
            }
        }

    });
```

**Querying multiple documents**
```java
    Query getAdmins = Query.greaterThan("permission", 5);
    collection.find(getAdmins, new MultiReadCallback() {

        @Override
        public void onQueryDone(DBCursor result, Exception err) {
            if (err != null) {
                err.printStackTrace();
            } else {
                UserManager.preloadAdmins(result);
            }
        }

    });
```

**Updating a document**
```java
    Query matchingDocument = Query.equals("userID", uid);
    Update update = Update.push("emails", newEmail);
    collection.update(matchingDocument, update);
```

**Updating documents with callback**
```java
    Query matchingDocument = Query.greaterThan("permission", 3);
    Update update = Update.pull("userPermissions", "mod.users.update");
    // query, update, upsert, multi, callback
    collection.update(matchingDocument, update, false, true, new WriteCallback() {

        @Override
        public void onQueryDone(WriteResult result, Exception err) {
            if (err == null) {
                // success
            }
        }

    });
```

#### Using the object syncing part
First off you'll want a database model class:
```java
    public class UserModel implements SyncedClass {
        private String username;
        private Permission permission;

        @DBSave(value = "username", index = true)
        public String getUsername() {
            return username;
        }

        @DBLoad(value = "username", index = true)
        public void setUsername(String username) {
            this.username = username;
        }

        @DBSave("permission")
        public int getPermission() {
            return permission.getId();
        }

        @DBLoad("permission")
        public void setPermission(int permission) {
            this.permission = Permission.fromId(permission);
        }
    }
```

After that you can load an object with the model, modify it, and then save it back with the changes made.
```java
    UserModel model = new UserModel();
    model.setUsername("WouterG");

    collection.load(model, new ObjectLoadedCallback<UserModel>() {

        @Override
        public void onObjectLoaded(UserModel object) {
            if (object != null) {
                object.setPermission(Permission.BANNED.getId());
                collection.save(object);
            }
        }

    });
```

A few notes on this method, right now the model uses only methods as saving/loading fields. I'm planning on putting the main annotations on the fields instead, and have the method option available incase you want to serialize an object in a specific way.

It's also currently required to have a constructor of the model that's empty (which may be private). I'd like to change this as well though.
