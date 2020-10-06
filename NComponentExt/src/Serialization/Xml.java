package Serialization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

public class Xml
{
  public static String serialize(BComponent component) {
    if (null == component) return null;

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;

    if (null != docFactory) {
      try { docBuilder = docFactory.newDocumentBuilder(); }
      catch (ParserConfigurationException e) { log.warning(e.getMessage()); }
    }

    if (null == docBuilder) return null;

    Document doc = docBuilder.newDocument();
    Element root = doc.createElement(component.getType().getTypeName());

    Property[] properties = component.getPropertiesArray();
    for (Property property : properties ) {
      String propName = property.getName();
      String val = "";

      try { val = component.get(property).toDataValue().encodeToString(); }
      catch (IOException e) { log.warning(e.getMessage()); }

      root.setAttribute(propName, val);
    }
    doc.appendChild(root);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = null;
    try { transformer = transformerFactory.newTransformer();}
    catch (TransformerConfigurationException e) { log.warning(e.getMessage());}

    if (null == transformer) return null;

    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

    DOMSource source = new DOMSource(doc);
    StringWriter sw = new StringWriter();
    StreamResult xmlResult = new StreamResult(sw);

    try {
      transformer.transform(source, xmlResult);
      return xmlResult.getWriter().toString();
    } catch (TransformerException e) {
      log.warning(e.getMessage());
    }

    return null;
  }

  public static BComponent deSerialize(String xmlString) {
    return null;
  }
}
