package com.leverx.driver;

import java.util.Enumeration;
import java.util.Hashtable;

public abstract class ObjectPool<T> {

    private Hashtable<T, Long> inUse, available;
    private long deadTime;

    public ObjectPool() {
        deadTime = 10000;
        inUse = new Hashtable<>();
        available = new Hashtable<>();
    }

    public abstract T create();

    public abstract boolean validate(T object);

    public abstract void dead(T object);

    public synchronized T takeOut() {
        long now = System.currentTimeMillis();
        T object;
        if (available.size() > 0) {
            Enumeration<T> e = available.keys();
            while (e.hasMoreElements()) {
                object = e.nextElement();
                if ((now - available.get(object)) > deadTime) {
                    available.remove(object);
                    dead(object);
                    object = null;
                } else {
                    if (validate(object)) {
                        available.remove(object);
                        inUse.put(object, now);
                        return (object);
                    } else {
                        available.remove(object);
                        dead(object);
                        object = null;
                    }
                }
            }
        }
        object = create();
        inUse.put(object, now);
        return (object);
    }

    public synchronized void takeIn(T t) {
        inUse.remove(t);
        available.put(t, System.currentTimeMillis());
    }
}