package ClusterAnalOfCoord;

import java.util.ArrayList;
import java.util.List;

import gpxParser.gpxCoordinates;

public class ClusterOfCoordinates {
    private int ClusterNumber;
    private List<gpxCoordinates> coordinatesInCluster;
    private gpxCoordinates centerOfCluster;





    
    public ClusterOfCoordinates(int clusterNumber) {
        ClusterNumber = clusterNumber;

        coordinatesInCluster = new ArrayList<>();
    }

    public int getClusterNumber() {
        return ClusterNumber;
    }
    public void setClusterNumber(int clusterNumber) {
        ClusterNumber = clusterNumber;
    }
    public List<gpxCoordinates> getCoordinatesInCluster() {
        return coordinatesInCluster;
    }
    public void setCoordinatesInCluster(List<gpxCoordinates> coordinatesInCluster) {
        this.coordinatesInCluster = coordinatesInCluster;
    }
    public gpxCoordinates getCenterOfCluster() {
        return centerOfCluster;
    }
    public void setCenterOfCluster(gpxCoordinates centerOfCluster) {
        this.centerOfCluster = centerOfCluster;
    }

    

    
}
