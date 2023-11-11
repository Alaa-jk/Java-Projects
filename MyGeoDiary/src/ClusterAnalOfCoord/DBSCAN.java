package ClusterAnalOfCoord;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import gpxParser.gpxCoordinates;

public class DBSCAN {

    /**
     * Im Algorithmus ist es das c bzw cn und beschreibt die Clusterzugehörigkeit eines Punkts. Die Cluster sind nummieriert
     */
    // static HashMap<gpxCoordinates,Integer> ClusterOfCoord = new HashMap<>(); 
    static List<ClusterOfCoordinates> Clusters; // Index 0 ist für RauschCluster reserviert, Index 1 ist für NullCluster reserviert

    
    
    static int ClusterNumber =0;


    static void initilazer(List<gpxCoordinates> Coordinates) {
            ClusterOfCoordinates RauschCluster = new ClusterOfCoordinates(-1);
            Clusters.add(RauschCluster);

            ClusterOfCoordinates NullCluster = new ClusterOfCoordinates(0); 
            NullCluster.setCoordinatesInCluster(Coordinates);
            Clusters.add(NullCluster); // Zuerst werden alle Coord zu 0 initialisiert, was bedeutet, dass diese Punkte noch zu keinem Cluster zugeordnet wurden
    }

    static void algorithm(double e, int m ) {
        while(!(Clusters.get(1).getCoordinatesInCluster().isEmpty())) {
            gpxCoordinates coordinateToAnalyse = Clusters.get(1).getCoordinatesInCluster().get(0); // Es wird immer den ersten Punkt genommen

            Clusters.get(1).getCoordinatesInCluster().remove(coordinateToAnalyse);

            List<gpxCoordinates> coordinatesInRadiusOfAnalyzedCoord = new ArrayList<>();

            SearchNeighbour(coordinateToAnalyse,coordinatesInRadiusOfAnalyzedCoord,e);

            if (calcWorthOfClusterList(coordinatesInRadiusOfAnalyzedCoord) < m) {  // coordinateToAnalyse ist dann ein Rauschpunkt
                Clusters.get(0).getCoordinatesInCluster().add(coordinateToAnalyse);                   
            } else if (calcWorthOfClusterList(coordinatesInRadiusOfAnalyzedCoord) >= m) { // coordinateToAnalyse ist dann ein Kernpunkt
                ClusterNumber++;
                ClusterOfCoordinates Cluster = new ClusterOfCoordinates(ClusterNumber);
                Cluster.getCoordinatesInCluster().add(coordinateToAnalyse);

                for (int i = 0; i < coordinatesInRadiusOfAnalyzedCoord.size(); i++) {  // Schleife für die Punkte innerhalb des Radius des Kernpunktes

                    if (Clusters.get(0).getCoordinatesInCluster().remove(coordinatesInRadiusOfAnalyzedCoord.get(i))) {  // Falls remove fehlschlägt, ist gpxCoo nicht rauschpunkt, falls doch ordne gpx neu zu
                        Cluster.getCoordinatesInCluster().add(coordinatesInRadiusOfAnalyzedCoord.get(i));
                    }

                    if (Clusters.get(1).getCoordinatesInCluster().remove(coordinatesInRadiusOfAnalyzedCoord.get(i))) {
                        Cluster.getCoordinatesInCluster().add(coordinatesInRadiusOfAnalyzedCoord.get(i));

                        List<gpxCoordinates> coordinatesInRadiusOfCatchedCoord = new ArrayList<>();
                        SearchNeighbour(coordinatesInRadiusOfAnalyzedCoord.get(i),coordinatesInRadiusOfCatchedCoord,e);
                        if (calcWorthOfClusterList(coordinatesInRadiusOfAnalyzedCoord) >= m) coordinatesInRadiusOfAnalyzedCoord = unifyTwoLists(coordinatesInRadiusOfAnalyzedCoord,coordinatesInRadiusOfCatchedCoord);
                    }
                }
                Clusters.add(Cluster);  
            }
        }
    } 

    public static void SearchNeighbour(gpxCoordinates coordinateToAnalyse, List<gpxCoordinates> coordinatesInRadiusOfAnalyzedCoord, double e) {
        List<gpxCoordinates> freeCoordinates = new ArrayList<>(Clusters.get(1).getCoordinatesInCluster()); // Performance Möglichkeit
        freeCoordinates.addAll(Clusters.get(0).getCoordinatesInCluster());

        for (int i = 0; i < freeCoordinates.size(); i++)  { // Nachschauen, welcher der Koordinaten innerhalb des Radius des ausgeählten Koordinate liegen
            gpxCoordinates IfcoordinatesInRadius = freeCoordinates.get(i);
            
            if( cordToDist.Calc.calcDistance(IfcoordinatesInRadius, coordinateToAnalyse) <= e ) {
                    coordinatesInRadiusOfAnalyzedCoord.add(IfcoordinatesInRadius); // Speichere Alle Koordinaten, welche innerhalb des Radius liegen
                } 
        }
    }



    public static <t> List<t> unifyTwoLists(List<t> List1, List<t> List2) {
        HashSet<t> Set1 = new HashSet<>(List1);
        HashSet<t> Set2 = new HashSet<>(List2);
        
        return (ArrayList<t>) Stream.concat(Set1.stream(),Set2.stream()).collect(Collectors.toSet()).stream().collect(Collectors.toList());
    }





    public static void findCentersOfClusters() {
        for (int i =2; i < Clusters.size(); i++) { 
            ClusterOfCoordinates Cluster = Clusters.get(i);

            double Averagenorthern_latitude = 0;
            double AverageEastern_longitude = 0;
            long AverageHeight = 0;

            LocalDateTime MINTime = LocalDateTime.of(Cluster.getCoordinatesInCluster().get(0).getDate(), Cluster.getCoordinatesInCluster().get(0).getTime());
            LocalDateTime MAXTime = LocalDateTime.of(Cluster.getCoordinatesInCluster().get(0).getDate(), Cluster.getCoordinatesInCluster().get(0).getTime());
            
            int divident = 0;
            for (gpxCoordinates Coordinate : Cluster.getCoordinatesInCluster()) {
                

                for (int j = 0; j < Coordinate.getWeight()+1 && j < 150; j++) {
                    Averagenorthern_latitude += Coordinate.getNorthern_latitude();
                    AverageEastern_longitude += Coordinate.getEastern_longitude();
                    divident++;
                }

                // Averagenorthern_latitude += Coordinate.getNorthern_latitude();
                // AverageEastern_longitude += Coordinate.getEastern_longitude();

                
                AverageHeight += Coordinate.getheight();
                
                if (LocalDateTime.of(Coordinate.getDate(),Coordinate.getTime()).compareTo(MAXTime) > 0) MAXTime = LocalDateTime.of(Coordinate.getDate(),Coordinate.getTime());  
                if (LocalDateTime.of(Coordinate.getDate(),Coordinate.getTime()).compareTo(MINTime) < 0) MINTime = LocalDateTime.of(Coordinate.getDate(),Coordinate.getTime());  
          
            //     if (Duration.between(MINTime, MAXTime).toHours() > 22) {
            //     System.out.println("max: " + MAXTime);
            //     System.out.println("min: " + MINTime);    
            // }
            }

            Averagenorthern_latitude /= divident;
            AverageEastern_longitude /= divident;
            AverageHeight /= Cluster.getCoordinatesInCluster().size();
            
            
            
            long days = Duration.between(MINTime, MAXTime).toSeconds()/ 86399; // Zählt Tage



            gpxCoordinates Center = new gpxCoordinates(Averagenorthern_latitude, AverageEastern_longitude, LocalDate.ofEpochDay(days),  LocalTime.ofSecondOfDay(Duration.between(MINTime, MAXTime).toSeconds()%86399) , AverageHeight);
            Cluster.setCenterOfCluster(Center);
            
        }
    }







    public static void findCentersOfOneCluster(ClusterOfCoordinates Cluster) {

            double Averagenorthern_latitude = 0;
            double AverageEastern_longitude = 0;
            long AverageHeight = 0;

            LocalDateTime MINTime = LocalDateTime.of(Cluster.getCoordinatesInCluster().get(0).getDate(), Cluster.getCoordinatesInCluster().get(0).getTime());
            LocalDateTime MAXTime = LocalDateTime.of(Cluster.getCoordinatesInCluster().get(0).getDate(), Cluster.getCoordinatesInCluster().get(0).getTime());
            
            int divident = 0;

            for (gpxCoordinates Coordinate : Cluster.getCoordinatesInCluster()) {

                
                for (int i = 0; i < Coordinate.getWeight()+1 && i < 150; i++) {
                    Averagenorthern_latitude += Coordinate.getNorthern_latitude();
                    AverageEastern_longitude += Coordinate.getEastern_longitude();
                    divident++;
                }

                // Averagenorthern_latitude += Coordinate.getNorthern_latitude();
                // AverageEastern_longitude += Coordinate.getEastern_longitude();


                
                AverageHeight += Coordinate.getheight();
                
                if (LocalDateTime.of(Coordinate.getDate(),Coordinate.getTime()).compareTo(MAXTime) > 0) MAXTime = LocalDateTime.of(Coordinate.getDate(),Coordinate.getTime());  
                if (LocalDateTime.of(Coordinate.getDate(),Coordinate.getTime()).compareTo(MINTime) < 0) MINTime = LocalDateTime.of(Coordinate.getDate(),Coordinate.getTime());  
          
            //     if (Duration.between(MINTime, MAXTime).toHours() > 22) {
            //     System.out.println("max: " + MAXTime);
            //     System.out.println("min: " + MINTime);    
            // }
            }


            Averagenorthern_latitude /= divident;
            AverageEastern_longitude /= divident;
            AverageHeight /= Cluster.getCoordinatesInCluster().size();
            
            
            
            long days = Duration.between(MINTime, MAXTime).toSeconds()/ 86399; // Zählt Tage



            gpxCoordinates Center = new gpxCoordinates(Averagenorthern_latitude, AverageEastern_longitude, LocalDate.ofEpochDay(days),  LocalTime.ofSecondOfDay(Duration.between(MINTime, MAXTime).toSeconds()%86399) , AverageHeight);
            Cluster.setCenterOfCluster(Center);
            
        
    }


    public static int calcWorthOfClusterList(List<gpxCoordinates> Coordinates){
        int WeightValue =0;
        for (gpxCoordinates gpxCoordinate : Coordinates) {
            WeightValue += gpxCoordinate.getWeight() +1;
        }

        return WeightValue;
    }


    public static List<ClusterOfCoordinates> DBSCANmain(List<gpxCoordinates> Coordinates, double e, int m) {
        Clusters = new ArrayList<>();
        initilazer(Coordinates);

        // long start = System.currentTimeMillis();
        algorithm(e, m);
        // long finish = System.currentTimeMillis();
        // long timeElapsed = finish - start;
        // System.out.println("executionTime: " + timeElapsed);

        findCentersOfClusters();
        
        return Clusters;

    }


}
// Math.sqrt(Math.pow(IfcoordinatesInRadius.getEastern_longitude() - coordinateToAnalyse.getEastern_longitude(), 2) + Math.pow(IfcoordinatesInRadius.getNorthern_latitude() - coordinateToAnalyse.getNorthern_latitude(), 2) )