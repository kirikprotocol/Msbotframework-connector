package com.eyelinecom.whoisd.sads2.msbotframework.adaptors;

import com.eyelinecom.whoisd.sads2.adaptor.DocumentAdaptor;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.common.SADSInitUtils;
import com.eyelinecom.whoisd.sads2.common.XSLTransformer;
import com.eyelinecom.whoisd.sads2.content.ContentResponse;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;
import java.util.Properties;

/**
 * Transforms source document in three steps:
 *
 * <ol>
 *   <li>The usual XSL transformation from content source format into internal document format
 *   (e.g. navigation -> links, input target pageId inlining)
 *
 *   <li>Spaces cleanup, which is usually done just before sending a message as this is much easier
 *   to do in code that using XSL. It should be done before XML formatting is lost.
 *
 *   <li>HTML-like formatting into Markdown conversion.
 * </ol>
 *
 * <h1>Supported Markdown format</h1>
 * <table>
 *   <tr>
 *     <th>Style</th>
 *     <th>Markdown</th>
 *     <th>Description</th>
 *   </tr>
 *   <tr>
 *     <td>Bold</td>
 *     <td>**text**</td>
 *     <td>Make the text bold</td>
 *   </tr>
 *   <tr>
 *     <td>Italic</td>
 *     <td>*text*</td>
 *     <td>Make the text italic</td>
 *   </tr>
 *   <tr>
 *     <td>Header1-5</td>
 *     <td># H1</td>
 *     <td>Mark a line as a header</td>
 *   </tr>
 *   <tr>
 *     <td>Strikethrough</td>
 *     <td>text</td>
 *     <td>Make the text strikethrough</td>
 *   </tr>
 *   <tr>
 *     <td>Hr</td>
 *     <td>---</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>Unordered list</td>
 *     <td>*</td>
 *     <td>Make an unordered list item</td>
 *   </tr>
 *   <tr>
 *     <td>Ordered list</td>
 *     <td>1.</td>
 *     <td>Make an ordered list item starting at 1</td>
 *   </tr>
 *   <tr>
 *     <td>Pre</td>
 *     <td>`text`</td>
 *     <td>Pre-formatted text (can be inline)</td>
 *   </tr>
 *   <tr>
 *     <td>Block quote</td>
 *     <td>&gt; text</td>
 *     <td>Quote a section of text</td>
 *   </tr>
 *   <tr>
 *     <td>link</td>
 *     <td>[bing](http://bing.com)</td>
 *     <td>Create a hyperlink with title</td>
 *   </tr>
 *   <tr>
 *     <td>image link</td>
 *     <td>![duck](http://aka.ms/Fo983c)</td>
 *     <td>Link to an image</td>
 *   </tr>
 * </table>
 *
 * Paragraphs are represented with blank line.
 */
@SuppressWarnings("WeakerAccess")
public class MbfMarkdownAdaptor extends DocumentAdaptor implements Initable {

  private static final String CONF_XSL_URL = "xsl_url";
  private static final String CONF_XSL_MD_URL = "xsl_md_url";

  private String xslFile;
  private String xslMdFile;

  private XSLTransformer transformer;
  private XSLTransformer mdTransformer;

  @Override
  public void init(Properties config) throws Exception {
    xslFile = SADSInitUtils.getFilename(CONF_XSL_URL, config);
    xslMdFile = SADSInitUtils.getFilename(CONF_XSL_MD_URL, config);

    init(
        new XSLTransformer(xslFile),
        new XSLTransformer(xslMdFile)
    );
  }

  void init(XSLTransformer transformer, XSLTransformer mdTransformer) {
    this.transformer = transformer;
    this.mdTransformer = mdTransformer;
  }

  @Override
  public void destroy() {}

  @Override
  public Document transform(Document document,
                            ContentResponse response) throws Exception {
    return mdTransformer.transform(
        fixSpacing(
            transformer.transform(document)
        )
    );
  }

  private Document fixSpacing(final Document doc) throws DocumentException {
    //noinspection unchecked
    for (int i = 0; i < doc.getRootElement().elements().size(); i++) {
      final List elements = doc.getRootElement().elements();

      final Element replacement = fixSpacing((Element) elements.get(i));

      elements.remove(i);
      //noinspection unchecked
      elements.add(i, replacement);
    }

    return doc;
  }

  private Element fixSpacing(Element messageElement) throws DocumentException {

    final String xml = messageElement.asXML().replaceAll("\\s+\\n", "\n").trim();

    messageElement = DocumentHelper.parseText(xml).getRootElement();

    //noinspection unchecked
    for (Node e : (List<Node>) messageElement.selectNodes("//text()")) {
      if (!"pre".equals(e.getParent().getName())) {
        e.setText(e.getText().replaceAll("\\n\\s+", "\n"));
      }
    }

    return messageElement;
  }

  @Override
  public String toString(){
    return this.getClass().toString() +
        ". Params: xsl = [" + xslFile + "], mdXsl = [" + xslMdFile + "]";
  }

}
