package testing;

import java.util.ArrayList;
import java.util.List;

/**
 * ResultClass
 */
public class ResultDataObject {

    public class DataObjectOfSegment {
        public String meanOfMobility;
        
        public long TotalDeviation;
        public long TotalTime;
        public int TimesOfAddend;
        public long HighestDeviation;

        public long PositveRightDeviation;
        public long NegativeRightDeviation;

        public long PositveLeftDeviation;
        public long NegativeLeftDeviation;


    }

    public class ResultDataOfOnePersonsData {

        public String SourceName;
        public List<DataObjectOfSegment> Segments;
        public long TotalTimeTest; 

        public ResultDataOfOnePersonsData(){
            Segments = new ArrayList<>();
        } 
    }

    public class ResultDataOfSegments {
        // DataObjectOfSegment Foot;
        // DataObjectOfSegment ByCycle; 
        // DataObjectOfSegment Tram;        
        // DataObjectOfSegment Train;
        // DataObjectOfSegment Car;
        public  List<DataObjectOfSegment> SegmentsByMobility;
 
    }

    public ResultDataOfSegments SegmentsOverview;
    public  List<ResultDataOfOnePersonsData> SegmentPerDataView;

    public ResultDataObject()  {
        SegmentsOverview = new ResultDataOfSegments(); 
        SegmentPerDataView =new ArrayList<>();
        
    }
    
}