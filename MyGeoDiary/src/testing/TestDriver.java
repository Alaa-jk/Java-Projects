package testing;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.text.Segment;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import gpxParser.gpxCoordinates;
import testing.ResultDataObject.DataObjectOfSegment;
import testing.ResultDataObject.ResultDataOfOnePersonsData;


public class TestDriver {

    ResultDataObject ResultData;
    


    HashMap<String, // Name/Ursprung der getesteten Daten
    HashMap<LocalDateTime,LocalDateTime>> // Liste von Zeitintervalle wo zu Fuß gelaufen wurde
        TestdataForFoot = new HashMap<>();

    HashMap<String, // Name/Ursprung der getesteten Daten
    HashMap<LocalDateTime,LocalDateTime>> // Liste von Zeitintervalle wo zu Fuß gelaufen wurde
        TestdataForCar = new HashMap<>();

    HashMap<String, // Name/Ursprung der getesteten Daten
    HashMap<LocalDateTime,LocalDateTime>> // Liste von Zeitintervalle wo zu Fuß gelaufen wurde
        TestdataForBycicle = new HashMap<>();

    HashMap<String, // Name/Ursprung der getesteten Daten
    HashMap<LocalDateTime,LocalDateTime>> // Liste von Zeitintervalle wo zu Fuß gelaufen wurde
        TestdataForTram = new HashMap<>();

    HashMap<String, // Name/Ursprung der getesteten Daten
    HashMap<LocalDateTime,LocalDateTime>> // Liste von Zeitintervalle wo zu Fuß gelaufen wurde
        TestdataForTrain = new HashMap<>();


    


    public TestDriver() {
        ResultData = new ResultDataObject();

    }



    public void ReadInTestPlan() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse("TestPlan.XML");
        doc.getDocumentElement().normalize(); // Pretty Print wird rausgenommen, zur Vereinheitlichung

        NodeList nodeList = doc.getElementsByTagName("testDaten").item(0).getChildNodes();
        if (nodeList.getLength() == 0) System.out.println("TestDaten not found...");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node DataFieldNode = nodeList.item(i);
            if (DataFieldNode.getNodeType() == Node.ELEMENT_NODE) {
                String Datasource =  DataFieldNode.getNodeName(); // String für Bezeichnung aus welcher Datei die TestDaten stammen

                NodeList TimeIntervalls = DataFieldNode.getChildNodes().item(1).getChildNodes();

                HashMap<LocalDateTime,LocalDateTime> TimeIntervallsToStoreForFoot = new HashMap<>();
                HashMap<LocalDateTime,LocalDateTime> TimeIntervallsToStoreForCar = new HashMap<>();
                HashMap<LocalDateTime,LocalDateTime> TimeIntervallsToStoreByCicle = new HashMap<>();
                HashMap<LocalDateTime,LocalDateTime> TimeIntervallsToStoreTram = new HashMap<>();
                HashMap<LocalDateTime,LocalDateTime> TimeIntervallsToStoreTrain = new HashMap<>();



                for (int j = 0; j < TimeIntervalls.getLength(); j++) {
                    Node TimeIntervall = TimeIntervalls.item(j);

                    if (TimeIntervall.getNodeType() == Node.ELEMENT_NODE) {
                        if (TimeIntervall.getNodeName().equals("Fuss")) {
                            LocalDateTime beginDate = LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("von").getTextContent().toString());
                            LocalDateTime EndDate =LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("bis").getTextContent().toString());
                            TimeIntervallsToStoreForFoot.put(beginDate, EndDate);
                        }

                        if (TimeIntervall.getNodeName().equals("Auto")) {
                            LocalDateTime beginDate = LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("von").getTextContent().toString());
                            LocalDateTime EndDate =LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("bis").getTextContent().toString());
                            TimeIntervallsToStoreForCar.put(beginDate, EndDate);
                        }

                        if (TimeIntervall.getNodeName().equals("Fahrrad")) {
                            LocalDateTime beginDate = LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("von").getTextContent().toString());
                            LocalDateTime EndDate =LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("bis").getTextContent().toString());
                            TimeIntervallsToStoreByCicle.put(beginDate, EndDate);
                        }

                        if (TimeIntervall.getNodeName().equals("Tram")) {
                            LocalDateTime beginDate = LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("von").getTextContent().toString());
                            LocalDateTime EndDate =LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("bis").getTextContent().toString());
                            TimeIntervallsToStoreTram.put(beginDate, EndDate);
                        }

                        if (TimeIntervall.getNodeName().equals("Zug")) {
                            LocalDateTime beginDate = LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("von").getTextContent().toString());
                            LocalDateTime EndDate =LocalDateTime.parse(TimeIntervall.getAttributes().getNamedItem("bis").getTextContent().toString());
                            TimeIntervallsToStoreTrain.put(beginDate, EndDate);
                        }

                    }

                }

                TestdataForFoot.put(Datasource, TimeIntervallsToStoreForFoot);
                TestdataForCar.put(Datasource, TimeIntervallsToStoreForCar);
                TestdataForBycicle.put(Datasource, TimeIntervallsToStoreByCicle);
                TestdataForTram.put(Datasource, TimeIntervallsToStoreTram);
                TestdataForTrain.put(Datasource, TimeIntervallsToStoreTrain);

            }
        }
    }



    public void CheckTheTestData( List<HashMap<gpxCoordinates,gpxCoordinates>> ListOfProgramOutputOfaAllSeg, String DataSource) {

        List<DataObjectOfSegment> SegmentsForOverview = new ArrayList<>(); // Object to store TestResult
         
        ResultDataObject.ResultDataOfOnePersonsData OneTableDataOfATestData = ResultData.new ResultDataOfOnePersonsData() ;
        OneTableDataOfATestData.SourceName = DataSource;
         
        for (int i = 0; i < ListOfProgramOutputOfaAllSeg.size(); i++) {
            HashMap<gpxCoordinates,gpxCoordinates> OneSeg = ListOfProgramOutputOfaAllSeg.get(i);
            if (OneSeg != null && i == 0) {
                DataObjectOfSegment Segment = getSuccessRate(DataSource ,OneSeg, TestdataForFoot);
                Segment.meanOfMobility = "Fuss";
                OneTableDataOfATestData.Segments.add(copyObject(Segment)); // Speichere Segmentdaten in Zusammenhang mit der Testdatenquelle
      
                SegmentsForOverview.add(copyObject(Segment));
            } 

            if (OneSeg != null && i == 1) {
                DataObjectOfSegment Segment = getSuccessRate(DataSource, OneSeg, TestdataForBycicle);
                Segment.meanOfMobility = "Fahrrad";
                OneTableDataOfATestData.Segments.add(copyObject(Segment));

                SegmentsForOverview.add(copyObject(Segment));
            }

            if (OneSeg != null && i == 2) { // die null checker muss dann noch entfernt werden
                DataObjectOfSegment Segment = getSuccessRate(DataSource, OneSeg, TestdataForTram);
                Segment.meanOfMobility = "Tram";
                OneTableDataOfATestData.Segments.add(copyObject(Segment));

                SegmentsForOverview.add(copyObject(Segment));
            }

            if (OneSeg != null && i == 3) {
                DataObjectOfSegment Segment = getSuccessRate(DataSource,OneSeg, TestdataForTrain);
                Segment.meanOfMobility = "Zug";
                OneTableDataOfATestData.Segments.add(copyObject(Segment));

                SegmentsForOverview.add(copyObject(Segment));
            }

            if (OneSeg != null && i == 4) {
                DataObjectOfSegment Segment = getSuccessRate(DataSource, OneSeg, TestdataForCar);
                Segment.meanOfMobility = "Auto";

                

                OneTableDataOfATestData.Segments.add(copyObject(Segment));

                SegmentsForOverview.add(copyObject(Segment));
            }

        }

        

        


        if (ResultData.SegmentsOverview.SegmentsByMobility == null) { // der erste soll einfach so eingespeichert werden und die nächsten sollen addiert werden
            ResultData.SegmentsOverview.SegmentsByMobility = new ArrayList<>(SegmentsForOverview);
        } else AddDataFromGlobal(SegmentsForOverview);



        ResultData.SegmentPerDataView.add(OneTableDataOfATestData);

    }

    DataObjectOfSegment copyObject(DataObjectOfSegment object){
        DataObjectOfSegment copyOb = ResultData.new DataObjectOfSegment();

        copyOb.HighestDeviation = object.HighestDeviation;
        copyOb.TimesOfAddend = object.TimesOfAddend;
        copyOb.TotalDeviation = object.TotalDeviation;
        copyOb.TotalTime =  object.TotalTime; 
        copyOb.meanOfMobility =  object.meanOfMobility; 


        return copyOb;
    }

    void AddDataFromGlobal(List<DataObjectOfSegment> Segments) {

        for (DataObjectOfSegment Segment : Segments) {
            for (DataObjectOfSegment globalData : ResultData.SegmentsOverview.SegmentsByMobility) {
                if (globalData.meanOfMobility.equals(Segment.meanOfMobility)) {

                    if (globalData.HighestDeviation < Segment.HighestDeviation) {
                        globalData.HighestDeviation = Segment.HighestDeviation;
                    }
                      
                    globalData.TimesOfAddend += Segment.TimesOfAddend;
                    globalData.TotalDeviation += Segment.TotalDeviation;
                    globalData.TotalTime +=  Segment.TotalTime;                  
                     
                    break;
                }
            }
        }

      
    }

    public ResultDataObject.DataObjectOfSegment getSuccessRate(String DataSource,HashMap<gpxCoordinates,gpxCoordinates> ProgramOutputOfOneSegment, HashMap<String, HashMap<LocalDateTime, LocalDateTime>> TestdataOfOneMobility){

        ResultDataObject.DataObjectOfSegment DataToFill = ResultData.new DataObjectOfSegment();

        DataToFill.TimesOfAddend =0; 
        DataToFill.HighestDeviation =0;
        DataToFill.TotalTime = 0;


        for (String item : TestdataOfOneMobility.keySet()) {
        
            if (!(item.concat(".gpx").equals(DataSource))) continue;

            HashMap<LocalDateTime,LocalDateTime> TestData = TestdataOfOneMobility.get(item);
            for (LocalDateTime BeginingTime : TestData.keySet()) {
                LocalDateTime EndingTime = TestData.get(BeginingTime);

                
                DataToFill.TotalTime += ChronoUnit.SECONDS.between(BeginingTime, EndingTime);

                for (gpxCoordinates ToTestTimeBegininginGpx : ProgramOutputOfOneSegment.keySet()) {
                    LocalDateTime ToTestBeginingTime=LocalDateTime.of(ToTestTimeBegininginGpx.getDate(), ToTestTimeBegininginGpx.getTime());
                    LocalDateTime ToTestEndingTime=LocalDateTime.of(ProgramOutputOfOneSegment.get(ToTestTimeBegininginGpx).getDate(),ProgramOutputOfOneSegment.get(ToTestTimeBegininginGpx).getTime());

                    if (isBetweenDates(BeginingTime,ToTestBeginingTime,ToTestEndingTime)) {
                        if (isBetweenDates(ToTestEndingTime,BeginingTime,EndingTime)) {
                            BeginingTime = ToTestBeginingTime; // Die noch nicht erkannte Zeit wird von hinten abgeschnitten
                        } else {
                            EndingTime = BeginingTime; // Die Werte vom Computer umschliesen den Intervall der tatsächlichen Werte
                            break;
                        }
                    }

                    if (isBetweenDates(EndingTime,ToTestBeginingTime,ToTestEndingTime)) {
                        if (isBetweenDates(ToTestBeginingTime,BeginingTime,EndingTime)) {
                            EndingTime = ToTestBeginingTime; // Die noch nicht erkannte Zeit wird von vorne abgeschnitten
                        } else {
                            BeginingTime = EndingTime; // Die Werte vom Computer umschliesen den Intervall der tatsächlichen Werte
                            break;
                        }
                    }


                    if (isBetweenDates(ToTestBeginingTime,BeginingTime,EndingTime) && isBetweenDates(ToTestEndingTime,BeginingTime,EndingTime)) {
                        // Tatsächlichen Werte umschließen die generierten Werte
                        if(ChronoUnit.SECONDS.between(BeginingTime, ToTestBeginingTime) > ChronoUnit.SECONDS.between(EndingTime, ToTestEndingTime)){
                            EndingTime = ToTestBeginingTime;
                        } else {
                            BeginingTime = ToTestEndingTime;
                        }
                    }
                }

                long seconds = ChronoUnit.SECONDS.between(BeginingTime, EndingTime);

                if (seconds > DataToFill.HighestDeviation) DataToFill.HighestDeviation = seconds;
                DataToFill.TotalDeviation += seconds;
                DataToFill.TimesOfAddend++;
            }
        }
        // ((double)(TotalTime-TotalDeviation)/(double)TotalTime) * 100.0
        return DataToFill;
    }

    boolean isBetweenDates(LocalDateTime TestDate, LocalDateTime startDate, LocalDateTime endDate ) {
        return !(TestDate.isBefore(startDate) || TestDate.isAfter(endDate));
    }



    public void makeCSVReport() throws IOException {
      
      String Header = "Uebersicht ueber die Segmente; \n Bewegungsart;Gesamtzeit in sec;Erfolgsrate;Durchschnittliche Abweichung in sec;Hoehste Abweichung in sec \n";

      String DataRows =" ";
      for (DataObjectOfSegment OneSegment : ResultData.SegmentsOverview.SegmentsByMobility) {

        if (OneSegment.TotalTime == 0) continue;

        DataRows +=  OneSegment.meanOfMobility + ";"  + OneSegment.TotalTime + ";" + 
        (((double)(OneSegment.TotalTime - OneSegment.TotalDeviation)/(double)OneSegment.TotalTime) * 100.0) 
        +"%;"+(OneSegment.TotalDeviation/OneSegment.TimesOfAddend) + ";" + OneSegment.HighestDeviation +"\n";
      }





      String TableTestData = " \n \n \n ";
      for (ResultDataOfOnePersonsData DataPerTable : ResultData.SegmentPerDataView) {
          String caption = DataPerTable.SourceName + "\n Bewegungsart;Gesamtzeit in sec;Erfolgsrate;Durchschnittliche Abweichung in sec;Hoehste Abweichung in sec \n";
          String Rows = " ";
          for (DataObjectOfSegment OneSegmentRow : DataPerTable.Segments) {
                if (OneSegmentRow.TotalTime == 0) continue;
                Rows +=  OneSegmentRow.meanOfMobility + ";"  + OneSegmentRow.TotalTime + ";" + 
                    (((double)(OneSegmentRow.TotalTime - OneSegmentRow.TotalDeviation)/(double)OneSegmentRow.TotalTime) * 100.0) 
                    +"%;"+(OneSegmentRow.TotalDeviation/OneSegmentRow.TimesOfAddend) + ";" + OneSegmentRow.HighestDeviation +"\n";
          }
        TableTestData += caption + Rows + "\n";
      }
      
      
      
      
      FileWriter CSVFile = new FileWriter("OutputGpxData/TestReport.csv");
      CSVFile.write(Header + DataRows + TableTestData);
     
      CSVFile.close();
        
    }


    public void openCSVReport() throws IOException {

        File file = new File("OutputGpxData/TestReport.csv");   
		    if(Desktop.isDesktopSupported()) {  
				
				Desktop desktop = Desktop.getDesktop();  
				if(file.exists()) {
					desktop.open(file);     
				}        
			}  
    }





}
