package ConfigLoader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A config file loader.
 */
public class ConfigLoader {
    /** The database configuration. */
    private DBData data;
    /** The configuration path. */
    private final String configPath = "config.xml";

    /**
     * Constructs a ConfigLoader instance.
     */
    public ConfigLoader() {
        this.data = null;
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
                    this.data = new DBData(
                            element.getAttribute("id"),
                            element.getElementsByTagName("ip").item(0).getTextContent(),
                            element.getElementsByTagName("port").item(0).getTextContent()
                    );

                    // The config should always contain only 1 database connection data.
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the config data.
     * @return Vector\<DBData\> - The config data stored internally.
     */
    public DBData fetchData() {
        return this.data;
    }

}
