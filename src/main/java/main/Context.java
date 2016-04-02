package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by morev on 01.04.16.
 */
public class Context {
    private Map<Class, Object> contextMap = new HashMap<>();

    public void add(Class<?> clazz, Object object) {
        if (!contextMap.containsKey(clazz)) {
            contextMap.put(clazz, object);
        }
    }

    public Object get(Class<?> clazz) {
        return contextMap.get(clazz);
    }
}
