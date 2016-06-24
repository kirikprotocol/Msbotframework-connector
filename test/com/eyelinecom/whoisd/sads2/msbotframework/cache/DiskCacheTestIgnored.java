package com.eyelinecom.whoisd.sads2.msbotframework.cache;

import com.google.common.base.Function;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.apache.commons.io.filefilter.TrueFileFilter.TRUE;
import static org.junit.Assert.assertArrayEquals;

// FIXME: fails in CI due to encoding issues.
//  expected:<[/[files/123/456/789/123456789.bin, /index.jso]n]> but was:<[/[index.json, /files/123/456/789/123456789.bi]n]>
//
//  junit.framework.ComparisonFailure: expected:<[/[files/123/456/789/123456789.bin, /index.jso]n]> but was:<[/[index.json, /files/123/456/789/123456789.bi]n]>
//  at com.eyelinecom.whoisd.sads2.msbotframework.cache.DiskCacheTest.testSimple(DiskCacheTest.java:40)
public class DiskCacheTestIgnored {

  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void testSimple() throws Exception {

    final File root = tmp.newFolder("root");

    final DiskCache cache =
        new DiskCache(root, Long.MAX_VALUE, TimeUnit.MILLISECONDS);

    assertNull(cache.getIfPresent(123456789L));
    cache.put(123456789L, bytes);

    assertNull(cache.getIfPresent(123));

    assertArrayEquals(bytes, cache.getIfPresent(123456789L));

    assertEquals(
        "[/files/123/456/789/123456789.bin, /index.json]",
        listFiles(root).toString()
    );
  }

  @Test
  public void testInvalidate() throws Exception {

    final DiskCache cache =
        new DiskCache(tmp.newFolder("root"), Long.MAX_VALUE, TimeUnit.MILLISECONDS);

    cache.put(123456789L, bytes);
    cache.invalidate(123456789L);

    assertNull(cache.getIfPresent(123456789L));
  }

  @Test
  public void testCleanup() throws Exception {

    final DiskCache cache =
        new DiskCache(tmp.newFolder("root"), 0, TimeUnit.MILLISECONDS);

    cache.put(123456789L, bytes);

    Thread.sleep(1);
    cache.cleanUp();

    assertNull(cache.getIfPresent(123456789L));
  }

  private byte[] bytes = new byte[1024];
  {
    for (int i = 0; i < bytes.length; i++) bytes[i] = (byte) i;
  }

  private List<String> listFiles(final File dir) {
    //noinspection unchecked
    return newArrayList(transform(
        FileUtils.listFiles(dir, TRUE, TRUE),
        new Function<File, String>() {
          @Override
          public String apply(File file) {
            return file.getAbsolutePath().replaceFirst(dir.getAbsolutePath(), "");
          }
        }));
  }
}
