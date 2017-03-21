package DataStructures;

import java.util.HashMap;

/**
 * Created by Anders Brams on 3/21/2017.
 */

/* Copied from stackoverflow
 * http://stackoverflow.com/questions/7519339/hashmap-to-return-default-value-for-non-found-keys*/

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
