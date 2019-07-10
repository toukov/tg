package com.example.tg;

public class EnvironmentalConstants {
    public final static String MORPHIA_MODEL_PACKAGE_NAME = "com.example.tg.models";
    public final static String MONGODB_HOSTNAME = "training-dbhj9.mongodb.net";
    public final static String MONGODB_DATABASE_NAME = "tg";
    public final static String MONGODB_USER = "test";
    public final static String MONGODB_PASSWORD = System.getenv("DB_PASSWORD");
}
