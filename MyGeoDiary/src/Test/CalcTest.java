package Test;

import cordToDist.Calc;
import gpxParser.gpxCoordinates;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;



class CalcTest {
    gpxCoordinates coordinates_a = new gpxCoordinates(11.97179947,51.34364472,LocalDate.of(2022,04,29),LocalTime.of(13,2,31),3.0);
    gpxCoordinates coordinates_b = new gpxCoordinates(11.97179000,51.34364500,LocalDate.of(2022,04,29),LocalTime.of(12,2,31),3.0);
    Calc calc = new Calc();
    @Test
    void calcDistance() {
        calc.calcDistance(coordinates_a,coordinates_b);



    }

    @Test
    void distance2() {
        calc.distance2(coordinates_a,coordinates_b);
    }


    @Test
    void calcSpeed(){
        assertEquals(60,calc.calcSpeed(50,3000));

    }
}