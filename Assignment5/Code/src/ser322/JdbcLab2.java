package ser322;

import java.io.*;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.InputSource;

public class JdbcLab2 {

	public static void main (String[] args) {
		if (args.length != 2) {
			System.err.println("Not the correct amount of arguments");
			System.err.println("Run as: java JdbcLab2 <path_to_file> <language_id");
			System.exit(1);
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(args[0]));
		} catch (IOException e) {
			System.err.println("Check the file existence and path");
			e.printStackTrace();
		}
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory.setValidating(false);
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			Document xmlDoc = builder.parse(args[0]);	

			XPath xpath = XPathFactory.newInstance().newXPath();
			String expr = "//film[language_id=" + args[1] + "]/title/text()";
			System.out.println("Query: " + expr);

			NodeList nodes = (NodeList) xpath.evaluate(expr, new InputSource(new FileReader(args[0])), XPathConstants.NODESET);

			System.out.println("RESULTS FROM QUERY:" + nodes.getLength() + "\n");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = (Node) nodes.item(i);
				System.out.println(node.getNodeValue());			
			}
			System.out.println("\nQuery Complete\n");
		} catch (Exception e) {
			System.err.println("Issue parsing and querying document");
			e.printStackTrace();
		}

	}

}