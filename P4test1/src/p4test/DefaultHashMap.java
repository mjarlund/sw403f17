package p4test;

import java.util.HashMap;

/**
 * Created by Mysjkin on 24-02-2017.
 */

/* Copied from stackoverflow */

public class DefaultHashMap<K,V> extends HashMap<K,V> {
    protected V defaultValue;
    public DefaultHashMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }
    @Override
    public V get(Object k) {
        return containsKey(k) ? super.get(k) : defaultValue;
    }
}
