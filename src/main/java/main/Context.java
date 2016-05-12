package main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by morev on 01.04.16.
 */
public class Context {
    private final Map<Class, Object> contextMap = new HashMap<>();

    public <T> void add(@NotNull Class<T> clazz, @NotNull T object) {
        if (contextMap.containsKey(clazz)) {
            throw new IllegalStateException("Context already contains service " + clazz.getName());
        }
        contextMap.put(clazz, object);
    }

    @Nullable
    public <T> T get(@NotNull Class<T> clazz) {
        //noinspection unchecked
        return (T) contextMap.get(clazz);
    }
}
