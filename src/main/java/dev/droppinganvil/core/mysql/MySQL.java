/*
 * Copyright (c) DroppingAnvil 2021.
 * All Rights Reserved.
 */

package dev.droppinganvil.core.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.xdevapi.*;
import dev.droppinganvil.core.exceptions.TypeNotSetException;
import dev.droppinganvil.core.mysql.annotations.Key;
import dev.droppinganvil.core.mysql.annotations.MemoryOnly;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MySQL {
    private String url = "mysqlx://localhost:33060/test";
    private final String user;
    private final String pass;
    private final String collectionName;
    public Session session;
    public Collection collection;
    public String schema;
    public ObjectMapper mapper = new ObjectMapper();
    public final Class<?> type;

    public MySQL(String username, String password, String collection, String schema) {
        this.user = username;
        this.pass = password;
        this.collectionName = collection;
        this.type = null;
        this.schema = schema;
        session = new SessionFactory().getSession(url + "?user=" + user + "&password=" + pass);
    }
    public MySQL(String username, String password, String collection, String url, String schema) {
        this.user = username;
        this.pass = password;
        this.collectionName = collection;
        this.url = url;
        this.type = null;
        this.schema = schema;
        session = new SessionFactory().getSession(url + "?user=" + user + "&password=" + pass);
    }
    public MySQL(String username, String password, String collection, String url, Class<?> type, String schema) {
        this.user = username;
        this.pass = password;
        this.collectionName = collection;
        this.url = url;
        this.type = type;
        this.schema = schema;
        session = new SessionFactory().getSession(url + "?user=" + user + "&password=" + pass);
    }

    public void saveData(Object object, String key, String value) throws IllegalAccessException, IOException {
        checkCollection();
        DocResult docs = collection.find("$."+key+" = '"+value+"'").execute();
        Object o = object;
        if (docs.hasNext()) {
            for (Field f : object.getClass().getDeclaredFields()) {
                if (!f.isAnnotationPresent(MemoryOnly.class) && !f.isAnnotationPresent(Key.class)) {
                    collection.modify("$."+key+" = "+value+"").set("$."+f.getName(), new JsonString().setValue(mapper.writeValueAsString(f.get(o)))).execute();
                }
            }
        } else {
            DbDoc dbDoc = new DbDocImpl().add(key, new JsonString().setValue(value));
            for (Field f : object.getClass().getDeclaredFields()) {
                if (!f.isAnnotationPresent(MemoryOnly.class)) {
                    dbDoc.add(f.getName(), new JsonString().setValue(mapper.writeValueAsString(f.get(o))));
                }
            }
            collection.add(dbDoc).execute();
        }
    }
    public <T> T getObject(String key, String value) throws IllegalAccessException, InstantiationException, IOException, TypeNotSetException {
        if (type == null) throw new TypeNotSetException();
        return (T) getObject(key, value, type);
    }
    public <T> T getObject(String key, String value, Class<T> clazz) throws IllegalAccessException, InstantiationException, IOException {
        checkCollection();
        //This statement is for unique searches Ex. UUID
        DocResult docs = collection.find("$."+key+" = '"+value+"'").execute();
        Object o = clazz.newInstance();
        if (docs.hasNext()) {
            DbDoc dbClass = docs.next();
            for (Field f : clazz.getDeclaredFields()) {
                if (!f.isAnnotationPresent(MemoryOnly.class)) {
                    f.set(o, mapper.readValue(dbClass.get(f.getName()).toString(), f.getType()));
                }
            }
        } else {
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(Key.class)) {
                    f.set(o, value);
                }
            }
        }
        return (T) o;
    }

    public void checkCollection() {
            if (session.getSchema(schema).getCollection(collectionName).existsInDatabase() == DatabaseObject.DbObjectStatus.NOT_EXISTS) {
                session.getSchema(schema).createCollection(collectionName);
            }
        collection = session.getSchema(schema).getCollection(collectionName);
    }

    public List<Object> getObjects(Class<?> clazz) {
        try {
            List<Object> objl = new ArrayList<>();
            checkCollection();
            System.out.println("[MySQL] Loading all objects in collection " + collectionName + " this might take some time");
            Long now = System.currentTimeMillis();
            DocResult docs = collection.find().execute();
            for (DbDoc doc : docs.fetchAll()) {
                Object o = clazz.newInstance();
                if (docs.hasNext()) {
                    DbDoc dbClass = docs.next();
                    for (Field f : clazz.getDeclaredFields()) {
                        if (!f.isAnnotationPresent(MemoryOnly.class)) {
                            f.set(o, mapper.readValue(dbClass.get(f.getName()).toString(), f.getType()));
                        }
                    }
                }
            }

            System.out.println("[MySQL] " + collectionName + " has completed loading. Time taken: " + (System.currentTimeMillis() - now) + "ms");
            return objl;
        } catch (Exception e) {
            System.out.println("[MySQL] An error has occurred while loading " + collectionName);
            e.printStackTrace();
        }
        return null;
    }
}
