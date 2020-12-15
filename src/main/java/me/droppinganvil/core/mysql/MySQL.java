/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.xdevapi.*;
import me.droppinganvil.core.mysql.annotations.Key;
import me.droppinganvil.core.mysql.annotations.MemoryOnly;

import java.io.IOException;
import java.lang.reflect.Field;

public class MySQL {
    private String url = "mysqlx://localhost:33060/test";
    private String user;
    private String pass;
    private String collectionName;
    public Session session = new SessionFactory().getSession(url + "?user=" + user + "&password=" + pass);
    public Collection collection;
    public ObjectMapper mapper = new ObjectMapper();

    public MySQL(String username, String password, String collection) {
        this.user = username;
        this.pass = password;
        this.collectionName = collection;
    }
    public MySQL(String username, String password, String collection, String url) {
        this.user = username;
        this.pass = password;
        this.collectionName = collection;
        this.url = url;
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
    public Object getObject(String key, String value, Class<?> clazz) throws IllegalAccessException, InstantiationException, IOException {
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
        return o;
    }

    public void checkCollection() {
        if (session.getDefaultSchema().getCollection(collectionName).existsInDatabase() == DatabaseObject.DbObjectStatus.NOT_EXISTS) {
            session.getDefaultSchema().createCollection(collectionName);
        }
        collection = session.getDefaultSchema().getCollection(collectionName);
    }
}
