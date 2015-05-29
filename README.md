# [ Warning! ]

This is not a big official mongo library, it's made in a short time by one person, 
and I do not recommend using it in production before it's been looked at by several others. 
For private use I also advice you to only use it if you're okay with sometimes renaming 
for instance class names due to how unfinished this still is.

Feedback is always welcome.


# SimpleMongo
A simplified MongoDB interaction library, featuring Query building and object syncing

## Installing
There's currently no Maven repository setup yet, so to use the project you'll have to download it, and either compile it and add the jar to your project as dependency, or if you're using maven simply link to the project.

## Usage

#### Opening connection
First off you want to get a `SimpleConnection` instance, and connect it to your server
```java
    private SimpleConnection connection;
    
    public void makeConnection() throws UnknownHostException {
        connection = new SimpleConnection("mongo.domain.com", 27017); // defaults to "localhost" and 27017
        connection.setAuthentication("admin", "qwerty", "Users"); // authenticate if needed
        connection.connect(); // throws UnknownHostException
    }
```

#### Getting a collection instance
Now you can get a `SimpleCollection` instance from the connection.
```java
    private SimpleCollection collection;
    
    public void initCollection(String db) {
        collection = connection.getCollection(db, "Users");
    }
```

#### Querying data from the database
Now you can start grabbing data from the database. A few examples:

**Querying a single document**
```java
    Query getVerifiedWouter = Query.and()
            .add(Query.equals("username", "WouterG"))
            .add(Query.not()
                .add(Query.size("verifiedEmails", 0)).finish()
            ).finish();

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
        public void onQueryDone(MongoCursor result, Exception err) {
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
    collection.update(matchingDocument, update, false, true, new UpdateCallback() {

        @Override
        public void onQueryDone(UpdateResult result, Exception err) {
            if (err == null) {
                // success
            }
        }

    });
```

#### Using the object syncing part
First off you'll want a database model class:
```java
    public class UserModel {
        @DBSync(index = true) public String uuid; // set as index value
        @DBSync public String username;
        @DBSync public int permission;
        @DBSync("data") public byte[] randomData; // read from different DB field name
    }
```

After that you can load an object with the model, modify it, and then save it back with the changes made.
```java
    UserModel model = new UserModel();
    model.uuid = "abcdefghijklmnopqrstuvwxyz123456";

    collection.load(model, new ObjectLoadedCallback<UserModel>() {

        @Override
        public void onObjectLoaded(UserModel object) {
            if (object != null) {
                object.permission = Permission.BANNED.getId());
                collection.save(object);
            }
        }

    });
```

A few notes on this method, right now the model uses only methods as saving/loading fields. I'm planning on putting the main annotations on the fields instead, and have the method option available incase you want to serialize an object in a specific way.

It's also currently required to have a constructor of the model that's empty (which may be private). I'd like to change this as well though.
