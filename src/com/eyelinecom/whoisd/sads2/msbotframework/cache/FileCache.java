package com.eyelinecom.whoisd.sads2.msbotframework.cache;

import com.eyelinecom.whoisd.sads2.resource.ResourceFactory;
import com.google.common.cache.AbstractCache;
import com.google.common.cache.Cache;
import com.google.common.collect.Maps;
import org.apache.commons.configuration.HierarchicalConfiguration;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class FileCache {

  public static class Factory implements ResourceFactory {

    @Override
    public Cache<Long, byte[]> build(String id,
                                     Properties properties,
                                     HierarchicalConfiguration config) throws Exception {

      final String path = properties.getProperty("file.cache.path", null);
      final long lifetime = Long.parseLong(properties.getProperty("file.cache.lifetime.seconds", "0"));

      return path == null ?
          new MockCache<Long, byte[]>() :
          new DiskCache(new File(path), lifetime, TimeUnit.SECONDS);
    }

    @Override public boolean isHeavyResource() { return false; }
  }

  /**
   * Stores nothing.
   */
  @SuppressWarnings("NullableProblems")
  private static class MockCache<K, V> extends AbstractCache<K, V> {

    @Override
    public V get(K key, Callable<? extends V> loader) throws ExecutionException {
      try {
        return loader.call();

      } catch (Exception e) {
        throw new ExecutionException(e);
      }
    }

    @Override public V getIfPresent(Object key) { return null; }
    @Override public void put(K key, V value) {}
    @Override public long size() { return 0; }
    @Override public void invalidate(Object key) {}
    @Override public void invalidateAll() {}
    @Override public ConcurrentMap<K, V> asMap() { return Maps.newConcurrentMap(); }

  }
}
