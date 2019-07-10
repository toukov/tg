package com.example.tg;

import static com.example.tg.EnvironmentalConstants.MONGODB_DATABASE_NAME;
import static com.example.tg.EnvironmentalConstants.MONGODB_HOSTNAME;
import static com.example.tg.EnvironmentalConstants.MORPHIA_MODEL_PACKAGE_NAME;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class DataBaseProvider {
    private static final Datastore datastore;
    private static final MongoClient mongo;

    static {
        Morphia morphia = new Morphia();
        morphia.mapPackage(MORPHIA_MODEL_PACKAGE_NAME);

        MongoClientURI uri = new MongoClientURI(
                String.format("mongodb+srv://%s:%s@%s/%s",
                        EnvironmentalConstants.MONGODB_USER,
                        EnvironmentalConstants.MONGODB_PASSWORD,
                        MONGODB_HOSTNAME,
                        MONGODB_DATABASE_NAME));

        mongo = new MongoClient(uri);

        datastore = morphia.createDatastore(mongo, MONGODB_DATABASE_NAME);
        datastore.ensureIndexes();
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public MongoClient getMongo() {
        return mongo;
    }
}
