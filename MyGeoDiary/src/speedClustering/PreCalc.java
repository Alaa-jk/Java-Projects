package speedClustering;

import java.util.List;

import cordToDist.Calc;
import gpxParser.gpxCoordinates;

public class PreCalc {
    

    void CalcSpeedAndVelocity(List<gpxCoordinates> GPXCoord) {
        for (int i = 0; i < GPXCoord.size(); i++) {
            Double speed = Calc.calcSpeed(Calc.calcTime(GPXCoord.get(i), GPXCoord.get(i+1)), Calc.calcDistance(GPXCoord.get(i), GPXCoord.get(i+1)));
            
        }
    }


}
