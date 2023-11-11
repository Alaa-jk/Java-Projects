package speedClustering;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import cordToDist.Calc;
import gpxParser.gpxCoordinates;
import nominatimRequests.Request;

public class CalcVelocityCluster {
    int InitFoottolerance;
    Double SpeedLimitByFoot;
    Double MinumFootTimeInHours; // 0.09

    Double SpeedLimitForBycycle;
    int InitByCycleTolerance;

    Double SpeedLimitForTram;
    int InitTramTolerance;
    int miniumCatchedStops; 

    HashMap<gpxCoordinates,gpxCoordinates> SegmentsOfFoot;
    HashMap<gpxCoordinates,gpxCoordinates> ByCycleSegments;
    HashMap<gpxCoordinates,gpxCoordinates> TramSegments;
    HashMap<gpxCoordinates,gpxCoordinates> TrainSegments;    

    HashMap<gpxCoordinates,gpxCoordinates> UnmarkedSegments; // wird in den getFootSegments instanziiert und befüllt 

    List<gpxCoordinates> GPXCoord;
    /*
     * Kriterien:
     *  1. Geschwindigkeit unter 12Km/h
     * // 2. Beschleunigung unter 10Km/h
     *  3. mindestdauer 5 minunten
     *  
     */
    public CalcVelocityCluster(boolean FullCalc, int foottolerance, Double speedLimitByFoot, Double minumFootTimeInHours,
        Double SpeedLimitForBycycle, int ByCycleTolerance, 
            Double SpeedLimitForTram, int TramTolerance, int miniumCatchedStops,
            List<gpxCoordinates> GPXCoord) throws SAXException, IOException, ParserConfigurationException, URISyntaxException {
        InitFoottolerance = foottolerance;
        SpeedLimitByFoot = speedLimitByFoot;
        MinumFootTimeInHours = minumFootTimeInHours;
        this.GPXCoord = GPXCoord;
        this.SpeedLimitForBycycle = SpeedLimitForBycycle;
        InitByCycleTolerance = ByCycleTolerance;
        this.SpeedLimitForTram = SpeedLimitForTram;
        InitTramTolerance = TramTolerance;
        this.miniumCatchedStops = miniumCatchedStops;

        if (FullCalc) {
            CalcFootSegments();
            CalcCycleSegment();
            CalcTramSegments();

            
        }
    }

     


     public void CalcFootSegments() {
        HashMap<gpxCoordinates,gpxCoordinates> AllFootSegments = new HashMap<>();
        UnmarkedSegments = new HashMap<>();

        gpxCoordinates LastGpx = null; // Beschreibt den AnfangsGpx dient dazu um die Segments wiederzufinden,da Anfang immer der Schlüssel ist
        int Foottolerance = InitFoottolerance;

        gpxCoordinates LastGpxOfNonFootSeg = null;


        for (int i = 1; i < GPXCoord.size(); i++) {
            Double speed = Calc.calcSpeed(Calc.calcTime(GPXCoord.get(i-1), GPXCoord.get(i)), Calc.calcDistance(GPXCoord.get(i-1), GPXCoord.get(i)));



            if (LastGpx != null) {
                
                // if (speed != 0) {
                //     double InverseSpeed = 1/speed;

                //     if (InverseSpeed > 40) InverseSpeed =40;
                //     GPXCoord.get(i).setWeight((int)(InverseSpeed ));

                //     // if ((InverseSpeed *5) > 25.0) {
                //     //     GPXCoord.get(i).setWeight(25);
                //     // } 
                //     // else {
                //     //     GPXCoord.get(i).setWeight((int)(InverseSpeed * 5.0));
                //     // }


                // } else {
                //     GPXCoord.get(i).setWeight(60);
                // }



                int ExtraimaGPX = (int)(Calc.calcTime(GPXCoord.get(i-1), GPXCoord.get(i))/0.006);
                GPXCoord.get(i).setWeight(ExtraimaGPX);

            }


            if ((speed <= SpeedLimitByFoot && LastGpx == null) || i ==1 ) { // openNewSegment, wenn die Geschwindigkeit im Bereich eines Fußgängers ist
                AllFootSegments.put(GPXCoord.get(i-1),GPXCoord.get(i));
                LastGpx = GPXCoord.get(i-1);


                if (LastGpxOfNonFootSeg!=null) {  // for nonFootSeg

                    UnmarkedSegments.put(LastGpxOfNonFootSeg, GPXCoord.get(i-1));
                    LastGpxOfNonFootSeg = null;
                   


                }
            }

            if (speed > SpeedLimitByFoot && LastGpx != null ) { // Falls die Geschwindigkeit für den Fußgänger überschritten wurde, dann soll geschaut werden ob das Seg geschlossen werden soll
                Foottolerance--; // FootTolerance sind die Extra Chancen bevor das Segment geschlossen wird. Soll Messfehler abfangen

                if (Foottolerance == 0) {  
                    Foottolerance = InitFoottolerance;
                    if (Calc.calcTime(LastGpx, GPXCoord.get(i-Foottolerance+1)) < MinumFootTimeInHours ){ 
                        AllFootSegments.remove(LastGpx);

                        LastGpxOfNonFootSeg = getKeyByValue(UnmarkedSegments,LastGpx);
                        UnmarkedSegments.remove(LastGpxOfNonFootSeg); 
                        
                        LastGpx = null;


                    } else { 

                        gpxCoordinates BeginingOfLastNonFoot = getKeyByValue(UnmarkedSegments,LastGpx);
                        if (BeginingOfLastNonFoot!=null && Calc.calcTime(BeginingOfLastNonFoot, LastGpx) < 0.02 ) { // Zwischen zwei Fußsegmenten muss mindestens 1 Minuten vergehen 
           
                            AllFootSegments.remove(LastGpx); //delete old segment
                            LastGpx = getKeyByValue(AllFootSegments,BeginingOfLastNonFoot); // Mark begining of old segment as Begining of new Segment                

                            UnmarkedSegments.remove(BeginingOfLastNonFoot); // remove Gap
                        }
                        AllFootSegments.put(LastGpx, GPXCoord.get(i-Foottolerance+1));
                        LastGpx = null;
                        LastGpxOfNonFootSeg =  GPXCoord.get(i-Foottolerance+1); // for nonFootSeg                       
                    }                    
                }
            } else {
                Foottolerance = InitFoottolerance;
            }

            if (i == GPXCoord.size()-1 && LastGpx != null ) { // Fall wenn Schleife zu ende ist, ohne dass ein Segment geschlossen wurde
                if ((Calc.calcTime(LastGpx, GPXCoord.get(i-Foottolerance+1)) < MinumFootTimeInHours) && !(i == GPXCoord.size()-1) ){ 
                        AllFootSegments.remove(LastGpx);

                        LastGpxOfNonFootSeg = getKeyByValue(UnmarkedSegments,LastGpx);
                        // UnmarkedSegments.remove(LastGpxOfNonFootSeg); 
                        UnmarkedSegments.put(LastGpxOfNonFootSeg, GPXCoord.get(i-Foottolerance+1));

                    } else {
                        AllFootSegments.put(LastGpx, GPXCoord.get(i));
                    }        
                
            }
        }
        SegmentsOfFoot = AllFootSegments;
    }



    public void CalcCycleSegment() {
        ByCycleSegments = new HashMap<>();

        for(int j = 0; j < UnmarkedSegments.keySet().size();) {
            gpxCoordinates gpxCoordinates = (gpxParser.gpxCoordinates) UnmarkedSegments.keySet().toArray()[j];
            boolean isCycle = true;
            int CycleTolerance = InitByCycleTolerance; 

            for (int i = 1; i < GPXCoord.size(); i++) {  

                if(isBetweenDates(LocalDateTime.of(GPXCoord.get(i).getDate(), GPXCoord.get(i).getTime()) ,
                    LocalDateTime.of(gpxCoordinates.getDate(), gpxCoordinates.getTime()) ,
                    LocalDateTime.of(UnmarkedSegments.get(gpxCoordinates).getDate(), UnmarkedSegments.get(gpxCoordinates).getTime()))) {

                        Double speed = Calc.calcSpeed(Calc.calcTime(GPXCoord.get(i-1), GPXCoord.get(i)), Calc.calcDistance(GPXCoord.get(i-1), GPXCoord.get(i)));


                        if (speed > SpeedLimitForBycycle) { //  man könnte noch die Beschleunigung einbauen
                            if (CycleTolerance == 0) isCycle = false; // Fahrrad
                            CycleTolerance--;

                        } else {
                            CycleTolerance = 3;
                        }
                }  
            }

            if (isCycle) {
                ByCycleSegments.put(gpxCoordinates, UnmarkedSegments.remove(gpxCoordinates)); // lösch den Segment aus der Liste der unbesetzten und setze ihn zu den Fährradern 
            } else j++;
        }
    }

    public void CalcTramSegments() throws SAXException, IOException, ParserConfigurationException, URISyntaxException{
        TrainSegments = new HashMap<>();                   
        TramSegments = new HashMap<>();

        for(int j = 0; j != UnmarkedSegments.keySet().size(); ) { // UnmarkedSeg wird immer kleiner
            gpxCoordinates gpxCoordinates = (gpxParser.gpxCoordinates) UnmarkedSegments.keySet().toArray()[j];
            boolean isTram = true;
            int TramTolerance = InitTramTolerance; 

  

            int Catchedstops = 0;
            int CatchedstopsOnRailway = 0;
            LocalTime TimeOfLastStop = LocalTime.of(0, 0, 0);
            boolean onRailwayIsSet = false;


            for (int i = 1; i < GPXCoord.size(); i++) {  

                if(isBetweenDates(LocalDateTime.of(GPXCoord.get(i).getDate(), GPXCoord.get(i).getTime()) ,
                    LocalDateTime.of(gpxCoordinates.getDate(), gpxCoordinates.getTime()) ,
                    LocalDateTime.of(UnmarkedSegments.get(gpxCoordinates).getDate(), UnmarkedSegments.get(gpxCoordinates).getTime()))) {

                        Double speed = Calc.calcSpeed(Calc.calcTime(GPXCoord.get(i-1), GPXCoord.get(i)), Calc.calcDistance(GPXCoord.get(i-1), GPXCoord.get(i)));

                        if(speed < 5 && !onRailwayIsSet) { 
                            Catchedstops++;
                            if (TimeOfLastStop.until(TimeOfLastStop,ChronoUnit.SECONDS) < 200 || 
                                ((TimeOfLastStop.getHour() == 0) && TimeOfLastStop.getMinute() ==0) ) {
                                    Request requestToLookUpStop = new Request();
                                    requestToLookUpStop.makeNewRequest(GPXCoord.get(i).getEastern_longitude(),GPXCoord.get(i).getNorthern_latitude());

                                if (requestToLookUpStop.LookResponseIfRailway()) CatchedstopsOnRailway++;
                                
                                if (CatchedstopsOnRailway >= miniumCatchedStops && ((double)CatchedstopsOnRailway/(double)Catchedstops > 0.5) ) { // parametrisieren
                                    onRailwayIsSet = true;
                                }

                            }

                            if (Catchedstops > 6 && ((double)CatchedstopsOnRailway/(double)Catchedstops < 0.5)) {  // TODO: parametrieserien 
                                break; // Hier ist es klar, dass es nicht auf der Schiene ist
                            }

                        }

                        if (speed > SpeedLimitForTram) { 
                            if (TramTolerance == 0) isTram = false; 
                            TramTolerance--;

                        } else {
                            TramTolerance = 3;
                        }
                }  
            }

            if (Catchedstops !=0) {
                if ((double)CatchedstopsOnRailway/(double)Catchedstops > 0.7) onRailwayIsSet = true; // TODO: para   
            }

            if (isTram && onRailwayIsSet) {
                TramSegments.put(gpxCoordinates, UnmarkedSegments.remove(gpxCoordinates)); // lösch den Segment aus der Liste der unbesetzten und setze ihn zu den Fährradern 
            }
            else if (!isTram && onRailwayIsSet) {
                TrainSegments.put(gpxCoordinates, UnmarkedSegments.remove(gpxCoordinates)); // lösch den Segment aus der Liste der unbesetzten und setze ihn zu den Fährradern 
            } else { // falls es kein Schiene 
                j++;
            }
        }



    }



    public void CalcCarSegments(){

        
    }




    
    public static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
        for (T keys : map.keySet() ) {
            if (map.get(keys).equals(value) ) {
                return keys;
            }
        }
        return null;
    }


    boolean isBetweenDates(LocalDateTime TestDate, LocalDateTime startDate, LocalDateTime endDate ) {
        return !(TestDate.isBefore(startDate) || TestDate.isAfter(endDate));
    }




    public HashMap<gpxCoordinates, gpxCoordinates> getSegmentsOfFoot() {
        return SegmentsOfFoot;
    }




    public HashMap<gpxCoordinates, gpxCoordinates> getByCycleSegments() {
        return ByCycleSegments;
    }




    public HashMap<gpxCoordinates, gpxCoordinates> getUnmarkedSegments() {
        return UnmarkedSegments;
    }




    public HashMap<gpxCoordinates, gpxCoordinates> getTramSegments() {
        return TramSegments;
    }




    public HashMap<gpxCoordinates, gpxCoordinates> getTrainSegments() {
        return TrainSegments;
    }




    



}
