package eppmcp.core.util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 用于XML的相关操作
 */
public class XmlUtil {

	/**
	 * @param doc
	 *            待处理的Document
	 * @return Document转换生成的字符串
	 */
	public static String xmlToString(Document doc) {
		ByteArrayOutputStream byteRep = new ByteArrayOutputStream();
		Format format = Format.getPrettyFormat();
		format.setEncoding("gb2312");
		XMLOutputter docWriter = new XMLOutputter(format);
		try {
			docWriter.output(doc, byteRep);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteRep.toString();
	}

	/**
	 * @param xmlStr
	 *            待转换的字符串
	 * @return 转换后得到的Document
	 */
	public static Document stringToXml(String xmlStr) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(new StringReader(xmlStr));
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return doc;
	}

}
