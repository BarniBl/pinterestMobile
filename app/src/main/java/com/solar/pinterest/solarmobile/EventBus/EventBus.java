package com.solar.pinterest.solarmobile.EventBus;

import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class EventBus {
    private static final String TAG = "Solar.EventBus";
    private static EventBus instance;
    private SortedMap<Event, List<EventListener>> mMap;

    public static EventBus get() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    private EventBus() {
        mMap = Collections.synchronizedSortedMap(new TreeMap<>());
    }

    public void subscribe(Event event, EventListener l) {
        synchronized (mMap) {
            List<EventListener> listeners = mMap.get(event);
            if (listeners == null) {
                listeners = new LinkedList<>();
                mMap.put(event, listeners);
            }
            listeners.add(l);
        }
    }

    public void unsubscribe(Event event, EventListener l) {
        synchronized (mMap) {
            List<EventListener> listeners = mMap.get(event);
            if (listeners == null) {
                return;
            }
            listeners.remove(l);
        }
    }

    public void emit(Event event) {
        Log.e(TAG, "emit");
        List<EventListener> list = mMap.get(event);
        if (list == null) {
            Log.e(TAG, "empry listeners");
            return;
        }
        for (EventListener listener : list) {
            Log.e(TAG, "call");
            listener.onEvent(event);
        }
    }
}
