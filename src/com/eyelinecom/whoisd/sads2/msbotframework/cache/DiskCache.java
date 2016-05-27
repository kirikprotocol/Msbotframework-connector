package com.eyelinecom.whoisd.sads2.msbotframework.cache;

import com.eyelinecom.whoisd.sads2.msbotframework.cache.DiskCache.Journal.Entry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.cache.AbstractCache;
import com.google.common.util.concurrent.Striped;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.google.common.collect.Maps.filterValues;
import static org.apache.commons.io.FileUtils.readFileToByteArray;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * Trivial disk-based cache implementation with a fixed entry lifetime.
 *
 * <p>MT-safe, but local to a single application instance.
 */
@SuppressWarnings("NullableProblems")
class DiskCache extends AbstractCache<Long, byte[]> {

  private final Logger log = Logger.getLogger(DiskCache.class);

  private final File filePath;
  private final File indexPath;

  private final long lifetimeMillis;

  private final ObjectMapper mapper = new ObjectMapper() {{
    configure(WRITE_DATES_AS_TIMESTAMPS, true);
  }};

  /** Global lock guards journal modifications. */
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  /** Per-entry locks. */
  private final Striped<Lock> fileLocks = Striped.lazyWeakLock(16);

  DiskCache(File path,
            long lifetime, TimeUnit unit) throws IOException {

    this.filePath       = checkDir(new File(path, "files"));
    this.indexPath      = checkFile(new File(path, "index.json"));

    this.lifetimeMillis = unit.toMillis(lifetime);

    initJournal();
  }


  //
  //  Public cache interface impl.
  //

  @Override
  public byte[] getIfPresent(Object key) {
    if (!(key instanceof Long)) {
      return null;
    }

    final Long id = (Long) key;

    try {
      return locked(lock.readLock(),
          new Callable<byte[]>() {
            public byte[] call() throws IOException {
              return readFileToByteArray(pathOf(id));
            }
          }).call();

    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public byte[] get(final Long id,
                    final Callable<? extends byte[]> valueLoader) throws ExecutionException {

    try {
      return locked(lock.readLock(),
          locked(fileLocks.get(id),
              new Callable<byte[]>() {
                @Override
                public byte[] call() throws Exception {
                  final byte[] present = getIfPresent(id);
                  if (present != null) {
                    return present;

                  } else {
                    final byte[] value = valueLoader.call();
                    put(id, value);
                    return value;
                  }
                }
              })).call();

    } catch (Exception e) {
      throw new CacheException(e);
    }
  }

  @Override
  public void put(final Long key, final byte[] value) {
    try {
      locked(fileLocks.get(key),
          locked(lock.writeLock(),
              new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                  final Journal journal = getJournal();
                  journal.put(key, new Entry());

                  writeByteArrayToFile(checkFile(pathOf(key)), value);

                  setJournal(journal);

                  return null;
                }
              })).call();

    } catch (Exception e) {
      throw new CacheException(e);
    }
  }

  @Override
  public void cleanUp() {
    final Predicate<Entry> isExpired = new Predicate<Entry>() {
      final Date now = new Date();

      @Override
      public boolean apply(Entry it) {
        return (now.getTime() - it.created.getTime()) > lifetimeMillis;
      }
    };

    try {
      locked(lock.writeLock(), new Callable<Void>() {
        @Override
        public Void call() throws Exception {
          final Journal journal = getJournal();

          final Map<Long, Entry> expired = journal.filter(isExpired);

          for (Long key : expired.keySet()) {
            FileUtils.deleteDirectory(dirOf(key));
          }
          expired.clear();

          setJournal(journal);
          return null;
        }
      }).call();

    } catch (Exception e) {
      throw new CacheException(e);
    }
  }

  @Override
  public void invalidate(Object key) {
    if (!(key instanceof Long)) {
      return;
    }

    final Long id = (Long) key;

    try {
      locked(lock.writeLock(), new Callable<Void>() {
        @Override
        public Void call() throws Exception {
          final Journal journal = getJournal();

          FileUtils.deleteDirectory(dirOf(id));
          journal.remove(id);

          setJournal(journal);
          return null;
        }
      }).call();

    } catch (Exception e) {
      throw new CacheException(e);
    }
  }

  private <T> Callable<T> locked(final Lock l, final Callable<T> action) {
    return new Callable<T>() {
      @Override
      public T call() throws Exception {
        l.lock();
        try {
          return action.call();

        } finally {
          l.unlock();
        }
      }
    };
  }


  //
  //  Index file.
  //

  static class Journal {
    @JsonProperty
    private Map<Long, Entry> index = new HashMap<>();

    Map<Long, Entry> filter(Predicate<Entry> predicate) {
      return filterValues(index, predicate);
    }

    Entry remove(Long id) {
      return index.remove(id);
    }

    void put(Long id, Entry e) {
      index.put(id, e);
    }

    static class Entry {
      @JsonProperty
      Date created = new Date();
    }
  }

  private Journal getJournal() throws IOException {
    return mapper.readerFor(Journal.class).readValue(readFileToByteArray(indexPath));
  }

  private void setJournal(Journal journal) throws IOException {
    writeByteArrayToFile(indexPath, mapper.writer().writeValueAsBytes(journal));
  }

  private void initJournal() {
    try {
      locked(lock.writeLock(), new Callable<Void>() {
        @Override
        public Void call() throws Exception {
          if (isEmpty(readFileToByteArray(indexPath))) {
            setJournal(new Journal());
          }
          return null;
        }
      }).call();
    } catch (Exception e) {
      throw new CacheException(e);
    }
  }


  //
  //  IO utils.
  //

  private static File checkDir(File it) throws IOException {
    if (!it.exists() && !it.mkdirs())
      throw new IOException("Missing and cannot be created: [" + it + "]");

    if (!it.isDirectory() || !it.canWrite())
      throw new IOException("Not a directory or readonly: [" + it + "]");

    return it;
  }

  private static File checkFile(File it) throws IOException {
    checkDir(it.getParentFile());

    if (!it.exists() && !it.createNewFile())
      throw new IOException("Missing and cannot be created: [" + it + "]");

    if (!it.isFile() || !it.canWrite())
      throw new IOException("Not a file or readonly: [" + it + "]");

    return it;
  }


  //
  //  Filename conventions.
  //

  private File pathOf(Long id) throws IOException {
    return new File(dirOf(id), id + ".bin");
  }

  private File dirOf(Long id) {
    final long d3 = id % 1000;
    id /= 1000;
    final long d2 = id % 1000;
    id /= 1000;
    final long d1 = id % 1000;

    return new File(filePath, join(new long[] {d1, d2, d3}, File.separatorChar));
  }

  private static class CacheException extends RuntimeException {
    CacheException(Throwable cause) {
      super(cause);
    }
  }
}
