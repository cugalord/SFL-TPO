package ConfigLoader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Vector;

/**
 * A config file loader.
 */
public class ConfigLoader {
    /** The different configurations. */
    private final Vector<DBData> data;
    /** The config path. */
    private final String configPath = "config.xml";

    /**
     * Constructs a ConfigLoader instance.
     */
    public ConfigLoader() {
        this.data = new Vector<>();
    }

    /**
     * Loads the data from the config xml file.
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public void load() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(this.configPath));
            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("schema");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element)node;
                    this.data.addElement(new DBData(
                            element.getAttribute("id"),
                            element.getElementsByTagName("ip").item(0).getTextContent()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the config data.
     * @return Vector\<DBData\> the config data stored internally.
     */
    public Vector<DBData> fetchData() {
        return this.data;
    }

}
