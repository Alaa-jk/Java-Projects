package nominatimRequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class Request {

     
    private Document Response;
    private String place_id;
     

    // public Request(double northern_latitude, double eastern_longitude) throws SAXException, IOException, ParserConfigurationException {
    //     DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    //     Response = builder.parse("https://nominatim.openstreetmap.org/reverse?lat=" + northern_latitude + "&lon=" + eastern_longitude );
        
    // }

    

    public void makeNewRequest(double northern_latitude, double eastern_longitude) throws SAXException, IOException, ParserConfigurationException, URISyntaxException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Response = builder.parse("https://nominatim.openstreetmap.org/reverse?lat=" + northern_latitude + "&lon=" + eastern_longitude );
        getPlaceID();    
    }
    

    public void getPlaceID() {
    	NodeList result = Response.getElementsByTagName("result");
   
    	place_id = result.item(0).getAttributes().getNamedItem("place_id").getTextContent();
    	


    }
    
    public String makeNewDetails() throws SAXException, IOException, ParserConfigurationException, URISyntaxException {
    	
        String s = readStringFromURL("https://nominatim.openstreetmap.org/details.php?place_id=" + place_id + "%40formatjson&addressdetails=1&hierarchy=0&group_hierarchy=1&format=json");
        int beginingIndex =  s.indexOf("\"type\"") + 8;
        int endingIndex =s.indexOf('"', beginingIndex);
            
      	return s.substring(beginingIndex, endingIndex);
    }

    public String readStringFromURL(String requestURL) throws IOException
    {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }












    
    public String lookUpWhatLocation() throws SAXException, IOException, ParserConfigurationException, URISyntaxException {

        String EndResult = "";

  
        if (LookUpTagInResponse("amenity") != null) {
            EndResult += LookUpTagInResponse("amenity") + " an der ";
        }

        if (LookUpTagInResponse("road") != null) {
            EndResult += LookUpTagInResponse("road");
        }

        if (LookUpTagInResponse("house_number") != null) {
            EndResult += " " + LookUpTagInResponse("house_number");
        }

        if (LookUpTagInResponse("city") != null) {
            EndResult += " in " + LookUpTagInResponse("city");
        } else if (LookUpTagInResponse("town") != null) {
            EndResult += " in " + LookUpTagInResponse("town");
        }


        return EndResult;
    }

    public String lookUpLocationIfShop()  {



        if (LookUpTagInResponse("shop") != null) {
            String shopAnswer = LookUpTagInResponse("shop");


            shopAnswer += " an der Straße " + LookUpTagInResponse("road");

            if (LookUpTagInResponse("city") != null) {
                shopAnswer += " in " + LookUpTagInResponse("city");
            } else if (LookUpTagInResponse("town") != null) {
                shopAnswer += " in " + LookUpTagInResponse("town");
            }
            
            return shopAnswer;
        }

        return null;
    }


    public Boolean LookResponseIfRailway(){
        NodeList RailNodes = Response.getElementsByTagName("railway");

                if(RailNodes.getLength() > 0) {
            return true;
        } else return false;

    }

    public String LookUpResponseIfRailway() throws SAXException, IOException, ParserConfigurationException, URISyntaxException{
        if (LookUpTagInResponse("railway") != null) {
            String RailwayAnswer = LookUpTagInResponse("railway") + " in ";

            if (LookUpTagInResponse("city") != null) {
                RailwayAnswer += LookUpTagInResponse("city");

            } else if (LookUpTagInResponse("town") != null) {
                RailwayAnswer += LookUpTagInResponse("town");
            }

            
            return RailwayAnswer;
            
        } else {
             return lookUpWhatLocation();
        }


        
    }

    public void LookResponseIfBusStop()  {
        
    }

    public void LookResponseIfTramStop() {
        
    }

    public void LookResponseIfRailwayStop() {
        
    }



    private String LookUpTagInResponse(String tag) {

        NodeList results= Response.getElementsByTagName(tag);
        String strAnswer = null;

        if (results.getLength() > 0) {
            strAnswer = "";
            for (int i = 0; i < results.getLength(); i++) {
                Node OneLocationNode = results.item(i);
                if(OneLocationNode.getNodeType() == Node.ELEMENT_NODE) {
                    // OneLocationNode.getTextContent().split(",");

                    strAnswer += OneLocationNode.getTextContent() ;
                }

            }
        }
        return strAnswer;
    }




    public Document getResponse() {
        return Response;
    }




}