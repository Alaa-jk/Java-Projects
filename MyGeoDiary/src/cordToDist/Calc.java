package cordToDist;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import gpxParser.gpxCoordinates;

public class Calc {
	
	// berechnet die Distanz in Kilometern simple Variante
	public static double calcDistance(gpxCoordinates a, gpxCoordinates b) {
		

		double lat = (a.getNorthern_latitude() + b.getNorthern_latitude()) / 2 * 0.01745;

		double dx = 111.3 * Math.cos(lat) * (a.getEastern_longitude() - b.getEastern_longitude());
		double dy = 111.3 * (a.getNorthern_latitude() - b.getNorthern_latitude());
		double distance = Math.sqrt(dx * dx + dy * dy);
		
		return distance;
		
	}
	
	// berechnet die Distanz in Kilometern komplexer
		public static double distance2(gpxCoordinates a, gpxCoordinates b) {
			
			double dist = 111.324 *( Math.acos(Math.sin(a.getNorthern_latitude()) * Math.sin(b.getNorthern_latitude()) + Math.cos(a.getNorthern_latitude()) * Math.cos(b.getNorthern_latitude()) * Math.cos(b.getEastern_longitude() - a.getEastern_longitude())));

			return dist;
			
		}
		
		public static double calcTime(gpxCoordinates a, gpxCoordinates b) {

			ChronoUnit.HOURS.between(LocalDateTime.of(a.getDate(),a.getTime()), LocalDateTime.of(b.getDate(),b.getTime()));

			double timeHour = ChronoUnit.HOURS.between(LocalDateTime.of(a.getDate(),a.getTime()), LocalDateTime.of(b.getDate(),b.getTime()));
			double timeMinute = ((ChronoUnit.MINUTES.between(LocalDateTime.of(a.getDate(),a.getTime()), LocalDateTime.of(b.getDate(),b.getTime()))) % 60);
			double timeSecond = ((ChronoUnit.SECONDS.between(LocalDateTime.of(a.getDate(),a.getTime()), LocalDateTime.of(b.getDate(),b.getTime()))) % 60);
			double time = timeHour + timeMinute/60 + timeSecond/(60*60); 
			return time;
		}
	
	public static double calcSpeed(double time, double distance) {
		
		double speed = distance/time;
		return speed;
	}
			
}

