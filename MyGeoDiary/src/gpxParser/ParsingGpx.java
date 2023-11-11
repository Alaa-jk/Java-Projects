package gpxParser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParsingGpx {

    public static List<gpxCoordinates> parseGPXtoJavaObject(File file) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize(); // Pretty Print wird rausgenommen, zur Vereinheitlichung
        System.out.println("Starte GeoAnalyse der Datei" + file.getName());


        NodeList nodeList = doc.getElementsByTagName("wpt");
        if (nodeList.getLength() == 0) {
            nodeList = doc.getElementsByTagName("trkpt");
        }

        List<gpxCoordinates> gpxCoordinatesList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {     
            Node node = nodeList.item(i);

            LocalDateTime t = null; Double Height = null;  // Initialisierung


            for (int j = 0; j < node.getChildNodes().getLength(); j++) { // Iteration über alle Kinder eines Wegpunktes
                if (node.getChildNodes().item(j).getNodeName() == "ele" ) { // Wenn Kind den Namen ele (Höhe) hat, dann soll dieser geispeichert werden
                    Height= Double.parseDouble(node.getChildNodes().item(j).getTextContent().toString());
                }    
    
                if (node.getChildNodes().item(j).getNodeName() == "time" ) { // Wenn Kind den Namen time hat, dann soll dieser geispeichert werden
                    t=LocalDateTime.parse(node.getChildNodes().item(j).getTextContent().toString().substring(0,
                    (node.getChildNodes().item(j).getTextContent().toString().length())-1));
                }
            }
            
            if(Height == null){
                System.out.println("Keine Höhe gefunden, setze 0 ein...");
                Height = 0.0;
            } 

            if(t == null){
                System.out.println("Keine Zeit gefunden, setze jetzige Zeit ein...");
                t =LocalDateTime.now();
            } 

            gpxCoordinates gpx = new gpxCoordinates(Double.parseDouble(node.getAttributes().getNamedItem("lon").getTextContent().toString()),
            Double.parseDouble(node.getAttributes().getNamedItem("lat").getTextContent().toString()) , t.toLocalDate() , t.toLocalTime(),Height);

            gpxCoordinatesList.add(gpx);
            
        }

        return gpxCoordinatesList;
    }

    public static File[] listFilesForFolder(File folder) {

        for (int i = 0; i < folder.listFiles().length; i++) {
            if (!(folder.listFiles()[i].isDirectory())) System.out.println("["+ i +"]: " + folder.listFiles()[i].getName());        
        }

        return folder.listFiles();
    }

/**
 * Listet in der Kommandozeile alle GpxDateien auf.
 * @return File, welches dann auch durch einen CommandoInput ausgewählt wurde
 * @throws SAXException
 * @throws IOException
 * @throws ParserConfigurationException
 */
    public static File ParsingSelection() throws SAXException, IOException, ParserConfigurationException {
      Scanner scanner = new Scanner(System.in);

      File Directory =  new File("InputGpxData/");
      File[] Files = listFilesForFolder(Directory);
    
      System.out.println("Welche Datei soll eingelesen werden?");
      int FileNumber = scanner.nextInt();
      scanner.nextLine();  

      
      return Files[FileNumber];
    }



      public static List<File> ParsAllForTests() throws SAXException, IOException, ParserConfigurationException {

      List<String> AllFileNamesToTest = getAllTestDataFromTestPlan();

      File Directory =  new File("InputGpxData/");
 
      List<File> ToTestFiles = new ArrayList<>();

       for (int i = 0; i < Directory.listFiles().length; i++) {

            if ((!(Directory.listFiles()[i].isDirectory())) && containString(AllFileNamesToTest,Directory.listFiles()[i].getName())) {
                ToTestFiles.add(Directory.listFiles()[i]);
            }
        }
    
      return ToTestFiles;
    }




    static List<String> getAllTestDataFromTestPlan() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse("TestPlan.XML");
        NodeList nodeList = doc.getElementsByTagName("testDaten").item(0).getChildNodes();

        if (nodeList.getLength() == 0) System.out.println("TestDaten not found...");    
            
        List<String> AllFileNamesToTest = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node DataFieldNode = nodeList.item(i);
            if (DataFieldNode.getNodeType() == Node.ELEMENT_NODE) AllFileNamesToTest.add(DataFieldNode.getNodeName());
            
        }

        return AllFileNamesToTest;
    }


    static boolean containString(List<String> list, String object ){
        for (String item : list) {
            if (item.concat(".gpx").equals(object)) return true;
        }
        return false;
    }

}
