/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package sanbing.jcpp.infrastructure.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CaffeineCacheTransaction<K extends Serializable, V extends Serializable> implements CacheTransaction<K, V> {
    @Getter
    private final UUID id = UUID.randomUUID();
    private final CaffeineTransactionalCache<K, V> cache;
    @Getter
    private final List<K> keys;
    @Getter
    @Setter
    private boolean failed;

    private final Map<K, V> pendingPuts = new LinkedHashMap<>();

    @Override
    public void put(K key, V value) {
        pendingPuts.put(key, value);
    }

    @Override
    public boolean commit() {
        return cache.commit(id, pendingPuts);
    }

    @Override
    public void rollback() {
        cache.rollback(id);
    }


}
