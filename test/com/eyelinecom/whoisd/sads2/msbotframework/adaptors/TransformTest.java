package com.eyelinecom.whoisd.sads2.msbotframework.adaptors;


import com.eyelinecom.whoisd.sads2.common.XSLTransformer;
import com.google.common.io.Resources;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TransformTest {

  @SuppressWarnings("FieldCanBeLocal")
  private final String XSL_RESOURCE = "/sads2-msbotframework.xsl";
  @SuppressWarnings("FieldCanBeLocal")
  private final String XSL_MD_RESOURCE = "/sads2-html2md.xsl";

  private MbfMarkdownAdaptor adaptor;

  @Before
  public void setUp() throws Exception {
    adaptor = new MbfMarkdownAdaptor();
    adaptor.init(
        new XSLTransformer(getClass().getResourceAsStream(XSL_RESOURCE)),
        new XSLTransformer(getClass().getResourceAsStream(XSL_MD_RESOURCE))
    );
  }

  private void checkTransform(String resourceRaw,
                              String resourceExpected) throws Exception {

    final Document rawDocument =
        new SAXReader().read(getClass().getResourceAsStream(resourceRaw));
    final String actual = adaptor.transform(rawDocument, null).asXML().replaceAll(" \\n", "\n");

    final String expected =
        Resources.toString(getClass().getResource(resourceExpected), UTF_8);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test1() throws Exception {
    checkTransform("content-01.xml", "response-01.xml");
  }

  @Test
  public void test2() throws Exception {
    checkTransform("content-02.xml", "response-02.xml");
  }
}
