package gpxParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.text.html.parser.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ClusterAnalOfCoord.ClusterOfCoordinates;

public class DeParser {

    public static void DeParsCenters(List<ClusterOfCoordinates> Clusters) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File("OutputGpxData/DeparsedCenters.gpx"));
        // doc.getDocumentElement().normalize(); // Pretty Print wird rausgenommen, zur Vereinheitlichung

        Node node = doc.getElementsByTagName("gpx").item(0); // Vorherigen Daten entfernen
        int Childs = node.getChildNodes().getLength();
        for (int i = 0; i < Childs; i++) {
            node.removeChild(node.getChildNodes().item(0));
        }



        for (int i =2; i < Clusters.size();i++) {
            ClusterOfCoordinates clusterOfCoordinates = Clusters.get(i);

            org.w3c.dom.Element Newwpt = doc.createElement("wpt"); 
            Newwpt.setAttribute("lat", Double.toString(clusterOfCoordinates.getCenterOfCluster().eastern_longitude));
            Newwpt.setAttribute("lon", Double.toString(clusterOfCoordinates.getCenterOfCluster().northern_latitude));
    
            org.w3c.dom.Element XMLHeight = doc.createElement("ele"); 
            XMLHeight.setTextContent(Double.toString(clusterOfCoordinates.getCenterOfCluster().height));
            org.w3c.dom.Element XMLTime = doc.createElement("time"); 
            XMLTime.setTextContent("2023-05-30T16:54:20Z");
    
    
            Newwpt.appendChild(XMLHeight);
            Newwpt.appendChild(XMLTime);

            
            node.appendChild(Newwpt);
        }


        TransformerFactory transformerFactory = TransformerFactory.newInstance(); // geparstes GPX-XML speichern
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("OutputGpxData/DeparsedCenters.gpx"));
        transformer.transform(source, result);


    } 
}
