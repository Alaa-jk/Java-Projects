import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import ClusterAnalOfCoord.ClusterOfCoordinates;
import ClusterAnalOfCoord.DBSCAN;
import cordToDist.Calc;
import gpxParser.DeParser;
import gpxParser.ParsingGpx;
import gpxParser.gpxCoordinates;
import speedClustering.CalcVelocityCluster;
import testing.TestDriver;
import nominatimRequests.Request;
public class GeoDiary {

	public static void main(String[] args) throws TransformerException, URISyntaxException {
		
		try {

    		Scanner scanner = new Scanner(System.in);
			System.out.println("Soll eine spezifische Datei für Debugzwecke eingelesen werden [1], ein Testlauf gestartet werden [2], oder" 
				+ " für eine spezifische Datei ein GeoDiary erstellt werden [3]? ");
			int response =  scanner.nextInt();
			scanner.nextLine();
			List<gpxCoordinates> liste;
			if (response == 1) {
				liste = ParsingGpx.parseGPXtoJavaObject(ParsingGpx.ParsingSelection());
					CalcVelocityCluster VelocityClutering = makeVelocityClusterObject(liste);

				for (gpxCoordinates BeginingGpx : VelocityClutering.getSegmentsOfFoot().keySet()) {
					System.out.println("FußSegment: " + BeginingGpx.getDate() + "," + BeginingGpx.getTime() + " - " +
					VelocityClutering.getSegmentsOfFoot().get(BeginingGpx).getDate() + "," + VelocityClutering.getSegmentsOfFoot().get(BeginingGpx).getTime());
				}
				System.out.println("--------------------------------");

				for (gpxCoordinates BeginingGpx : VelocityClutering.getByCycleSegments().keySet()) {
					System.out.println("FahrradSegment: " + BeginingGpx.getDate() + "," + BeginingGpx.getTime() + " - " +
					VelocityClutering.getByCycleSegments().get(BeginingGpx).getDate() + "," + VelocityClutering.getByCycleSegments().get(BeginingGpx).getTime());
				}
				System.out.println("--------------------------------");

				for (gpxCoordinates BeginingGpx : VelocityClutering.getTramSegments().keySet()) {
					System.out.println("TramSegment: " + BeginingGpx.getDate() + "," + BeginingGpx.getTime() + " - " +
					VelocityClutering.getTramSegments().get(BeginingGpx).getDate() + "," + VelocityClutering.getTramSegments().get(BeginingGpx).getTime());
				}
				System.out.println("--------------------------------");

				for (gpxCoordinates BeginingGpx : VelocityClutering.getTrainSegments().keySet()) {
					System.out.println("ZugSegment: " + BeginingGpx.getDate() + "," + BeginingGpx.getTime() + " - " +
					VelocityClutering.getTrainSegments().get(BeginingGpx).getDate() + "," + VelocityClutering.getTrainSegments().get(BeginingGpx).getTime());
				}
				System.out.println("--------------------------------");
	

				for (gpxCoordinates BeginingGpx : VelocityClutering.getUnmarkedSegments().keySet()) {
					System.out.println("Auto: " + BeginingGpx.getDate() + "," + BeginingGpx.getTime() + " - " +
					VelocityClutering.getUnmarkedSegments().get(BeginingGpx).getDate() + "," + VelocityClutering.getUnmarkedSegments().get(BeginingGpx).getTime());
				}	



				List<ClusterOfCoordinates> Clusters = new ArrayList<>(); // Verlagerung
 
				for (gpxCoordinates BeginingGpx : VelocityClutering.getSegmentsOfFoot().keySet()) {
					List<gpxCoordinates> footListForOneSegment = new ArrayList<>();
					for (gpxCoordinates gpxCoordinates : liste) {	


						gpxCoordinates EndingGpx = VelocityClutering.getSegmentsOfFoot().get(BeginingGpx);
						
						if (isBetweenDates(LocalDateTime.of(gpxCoordinates.getDate(),gpxCoordinates.getTime()),
							LocalDateTime.of(BeginingGpx.getDate(),BeginingGpx.getTime()),LocalDateTime.of(EndingGpx.getDate(),EndingGpx.getTime()))) {
							footListForOneSegment.add(gpxCoordinates);
							
						}
						
					}
					if (footListForOneSegment.size() > 0 ) {
						Clusters.addAll(DBSCAN.DBSCANmain(footListForOneSegment,0.025 ,8));

					}

				} // END:Verlagerung 

			 	 
				

				
			Request ClusterReq = new Request();	

				



			System.out.println("Es wurden " + (Clusters.size() -2) + " Cluster gefunden:"); 
			System.out.println();
			for (ClusterOfCoordinates Cluster : Clusters) {
				if (Cluster.getCoordinatesInCluster().size() != 0 && Cluster.getClusterNumber() != -1) {	

					ClusterReq.makeNewRequest(Cluster.getCenterOfCluster().getEastern_longitude(),
					Cluster.getCenterOfCluster().getNorthern_latitude());	

					System.out.println("Clusternr: " +  Cluster.getClusterNumber() + " besitzt " + Cluster.getCoordinatesInCluster().size() + " Coordinaten" );
					System.out.println("	Clusterzentrum: EL=" + Cluster.getCenterOfCluster().getEastern_longitude() + " NL=" + Cluster.getCenterOfCluster().getNorthern_latitude());
					System.out.println("	Clusterzentrum ReverseSearch: " + ClusterReq.lookUpWhatLocation());

					System.out.println("	Verweildauer im Cluster: " + Cluster.getCenterOfCluster().getTime() + " und " + Cluster.getCenterOfCluster().getDate().toEpochDay() + " Tag(e)" );
				}
				// System.out.println("GPX-Punkt mit den Koordinaten: " + gpxCoordinates.getEastern_longitude() + " und " + gpxCoordinates.getNorthern_latitude());

				
			}
			DeParser.DeParsCenters(Clusters);
			}


			if (response == 2) {
		
				TestDriver StatisticalTests = new TestDriver();
				StatisticalTests.ReadInTestPlan();

				for (File file : ParsingGpx.ParsAllForTests()) {
					liste = ParsingGpx.parseGPXtoJavaObject(file);
					CalcVelocityCluster VelocityClutering = makeVelocityClusterObject(liste);
					
					List<HashMap<gpxCoordinates,gpxCoordinates>> AllSegs = new ArrayList<>();
					AllSegs.add(VelocityClutering.getSegmentsOfFoot());
					AllSegs.add(VelocityClutering.getByCycleSegments());
					AllSegs.add(VelocityClutering.getTramSegments());
					AllSegs.add(VelocityClutering.getTrainSegments());
					AllSegs.add(VelocityClutering.getUnmarkedSegments());

					StatisticalTests.CheckTheTestData(AllSegs, file.getName());
				}
				StatisticalTests.makeCSVReport();

				System.out.println("Analyse der Testdaten abgeschlossen.");
				System.out.println("Testbericht wurde erfolgreich angefertigt");	
				
				System.out.println("Soll der Testbericht geöffnet werden [J/N]");	
				char c =scanner.next().charAt(0);
				if (c == 'J') StatisticalTests.openCSVReport();
				
			}  

			if (response == 3) {
				liste = ParsingGpx.parseGPXtoJavaObject(ParsingGpx.ParsingSelection());
					CalcVelocityCluster VelocityClutering = makeVelocityClusterObject(liste);
				


				List<gpxCoordinates> AllSeg = new ArrayList<>();
				AllSeg.addAll(VelocityClutering.getSegmentsOfFoot().keySet());
				AllSeg.addAll(VelocityClutering.getByCycleSegments().keySet());
				AllSeg.addAll(VelocityClutering.getTramSegments().keySet());
				AllSeg.addAll(VelocityClutering.getTrainSegments().keySet());
				AllSeg.addAll(VelocityClutering.getUnmarkedSegments().keySet());
				Collections.sort(AllSeg);
	
				System.out.println("In welchen Style soll das GeoDiary geschrieben werden?");	
				System.out.println("[1] Bericht (Für Debuggen)  ");	
				System.out.println("[2] TageBuch  ");	

				


 


				int answer = scanner.nextInt();

				




				if (answer == 1) {

					for (int i = 0; i < AllSeg.size(); i++) {
					
					gpxCoordinates gpxCoordinates = AllSeg.get(i);

					if (VelocityClutering.getSegmentsOfFoot().get(gpxCoordinates) != null) {
 

						System.out.println("Von " + gpxCoordinates.getDate() + "," + gpxCoordinates.getTime() + " - " +
			 				VelocityClutering.getSegmentsOfFoot().get(gpxCoordinates).getDate() + "," + 
								VelocityClutering.getSegmentsOfFoot().get(gpxCoordinates).getTime() + " warst du zu "  + ConsoleColors.WHITE_BOLD_BRIGHT + " Fuß " + ConsoleColors.RESET +  " unterwegs");


						List<ClusterOfCoordinates> Clusters= FindClustersForSegment(liste,VelocityClutering,gpxCoordinates);
						
				
						Request ClusterReq = new Request();	
						for (int j = 0; j < Clusters.size(); j++) {
							ClusterOfCoordinates Cluster = Clusters.get(j);
										
							

							if (AllSeg.size()-1 == i && j==0) {
								
								
								ClusterReq.makeNewRequest(VelocityClutering.getSegmentsOfFoot().get(AllSeg.get(i)).getEastern_longitude(),
								VelocityClutering.getSegmentsOfFoot().get(AllSeg.get(i)).getNorthern_latitude());
								
								
								System.out.println(" 	Am Ende warst du in "  + ConsoleColors.YELLOW + ClusterReq.lookUpWhatLocation() + ConsoleColors.RESET);
								break;

							} else if(Cluster.getCoordinatesInCluster().size() != 0 && Cluster.getClusterNumber() != -1) {	
								ClusterReq.makeNewRequest(Cluster.getCenterOfCluster().getEastern_longitude(),
								Cluster.getCenterOfCluster().getNorthern_latitude());	

								
								if (ClusterReq.LookResponseIfRailway()) { //TODO: Railway stop

									System.out.println(" 	Du musstest für " + Cluster.getCenterOfCluster().getTime() + " und " + 
									Cluster.getCenterOfCluster().getDate().toEpochDay() + " Tag(e) in " + ClusterReq.lookUpWhatLocation()
									+ " auf deinen Zug/Tram warten");
									
								} else {
									System.out.println(" 	Dabei warst du für " + Cluster.getCenterOfCluster().getTime() + " und " + 
									Cluster.getCenterOfCluster().getDate().toEpochDay() + " Tag(e) in " + ClusterReq.lookUpWhatLocation());
								}
								
								System.out.println("Anzahl: " + Cluster.getCoordinatesInCluster().size() );
									System.out.println("Wert des Clusters: " + DBSCAN.calcWorthOfClusterList(Cluster.getCoordinatesInCluster()));

								if (j > 2) {  // DEBUGGING
									System.out.println("Distanz: " + Calc.calcDistance(Cluster.getCenterOfCluster(), Clusters.get(j-1).getCenterOfCluster()));
									System.out.println( );

									
								}

							}


							

						}						

						System.out.println( );
						System.out.println("------------------------------------------------------------------------");
						System.out.println( );

					}

					if (VelocityClutering.getByCycleSegments().get(gpxCoordinates) != null) {
						System.out.println("Von  " + gpxCoordinates.getDate() + "," + gpxCoordinates.getTime() + " - " +
			 				VelocityClutering.getByCycleSegments().get(gpxCoordinates).getDate() + "," + 
								VelocityClutering.getByCycleSegments().get(gpxCoordinates).getTime() + " warst du mit dem Fahrrad unterwegs");
						
						
						System.out.println( );
						System.out.println("------------------------------------------------------------------------");
						System.out.println( );

					}

					if (VelocityClutering.getTramSegments().get(gpxCoordinates) != null) {
						System.out.println("Von  " + gpxCoordinates.getDate() + "," + gpxCoordinates.getTime() + " - " +
			 				VelocityClutering.getTramSegments().get(gpxCoordinates).getDate() + "," + 
								VelocityClutering.getTramSegments().get(gpxCoordinates).getTime() + " warst du mit dem Tram unterwegs");
						
						
						System.out.println( );
						System.out.println("------------------------------------------------------------------------");
						System.out.println( );
					}

					if (VelocityClutering.getTrainSegments().get(gpxCoordinates) != null) {
						System.out.println("Von  " + gpxCoordinates.getDate() + "," + gpxCoordinates.getTime() + " - " +
			 				VelocityClutering.getTrainSegments().get(gpxCoordinates).getDate() + "," + 
								VelocityClutering.getTrainSegments().get(gpxCoordinates).getTime() + " warst du mit dem Zug unterwegs");
						
						
						System.out.println( );
						System.out.println("------------------------------------------------------------------------");
						System.out.println( );
					}

					if (VelocityClutering.getUnmarkedSegments().get(gpxCoordinates) != null) {
						System.out.println("Von  " + gpxCoordinates.getDate() + "," + gpxCoordinates.getTime() + " - " +
			 				VelocityClutering.getUnmarkedSegments().get(gpxCoordinates).getDate() + "," + 
								VelocityClutering.getUnmarkedSegments().get(gpxCoordinates).getTime() + " warst du mit dem Auto unterwegs");
						
						
						System.out.println( );
						System.out.println("------------------------------------------------------------------------");
						System.out.println( );
					}



				}
					
				}
	
				if (answer == 2) {
					
					System.out.println(" Die Strecke began am " + ConsoleColors.GREEN + AllSeg.get(0).getDate() + ConsoleColors.RESET + ", um " +
								ConsoleColors.RED + 	AllSeg.get(0).getTime().plusHours(2) + ConsoleColors.RESET);
					LocalDate startDay = AllSeg.get(0).getDate();
					Request ClusterReq = new Request();
					Request SecClusterReq = new Request();

					for (int i = 0; i < AllSeg.size(); i++) {
						
						gpxCoordinates gpxCoordinates = AllSeg.get(i);


						if (VelocityClutering.getSegmentsOfFoot().get(gpxCoordinates) != null) {
	
						if (i < AllSeg.size()-1 && AllSeg.get(i+1).getDate().isAfter(startDay)) {
							System.out.println("Am " +  ConsoleColors.GREEN +  startDay+ ConsoleColors.RESET + " ,um " + ConsoleColors.RED +  gpxCoordinates.getTime().plusHours(2) + ConsoleColors.RESET + "  war ich bis zum " + 
								ConsoleColors.GREEN +  AllSeg.get(i+1).getDate() + ConsoleColors.RESET + ", um " + ConsoleColors.RED + 	VelocityClutering.getSegmentsOfFoot().get(gpxCoordinates).getTime().plusHours(2) + ConsoleColors.RESET
								 	 + " zu " + ConsoleColors.WHITE_BOLD_BRIGHT + " Fuß " + ConsoleColors.RESET + " unterwegs " );
							
							
							startDay = AllSeg.get(i+1).getDate();


						} else {
							System.out.println("Um " + ConsoleColors.RED +  gpxCoordinates.getTime().plusHours(2) + ConsoleColors.RESET + "  war ich bis " + 
								 ConsoleColors.RED + 	VelocityClutering.getSegmentsOfFoot().get(gpxCoordinates).getTime().plusHours(2) + ConsoleColors.RESET
								 	 + " zu " + ConsoleColors.WHITE_BOLD_BRIGHT + " Fuß " + ConsoleColors.RESET + " unterwegs " );
						}


							

							
							List<ClusterOfCoordinates> Clusters= FindClustersForSegment(liste,VelocityClutering,gpxCoordinates);
							
							for (int j = 0; j < Clusters.size(); j++) {
								ClusterOfCoordinates Cluster = Clusters.get(j);
											
								

								if (AllSeg.size()-1 == i && j==0) {
									
									
									ClusterReq.makeNewRequest(VelocityClutering.getSegmentsOfFoot().get(AllSeg.get(i)).getEastern_longitude(),
									VelocityClutering.getSegmentsOfFoot().get(AllSeg.get(i)).getNorthern_latitude());
									
									
									System.out.println(" 	Am Ende war ich in "  + ClusterReq.lookUpWhatLocation());
									break;

								} else if(Cluster.getCoordinatesInCluster().size() != 0 && Cluster.getClusterNumber() != -1) {	
									ClusterReq.makeNewRequest(Cluster.getCenterOfCluster().getEastern_longitude(),
									Cluster.getCenterOfCluster().getNorthern_latitude());	

									
									if (ClusterReq.LookResponseIfRailway()) { //TODO: Railway stop

										System.out.println(" 	Ich musste für " + ConsoleColors.BLUE + Cluster.getCenterOfCluster().getTime() 
										 + " und " + Cluster.getCenterOfCluster().getDate().toEpochDay() + " Tag(e) " + ConsoleColors.YELLOW + " in " + ClusterReq.lookUpWhatLocation()
										 + ConsoleColors.RESET + " auf deinen Zug/Tram warten");
										
									} else if(ClusterReq.lookUpLocationIfShop() != null) {
										System.out.println(" 	Dabei ging ich für " + ConsoleColors.BLUE +  Cluster.getCenterOfCluster().getTime() + 
										" und "+Cluster.getCenterOfCluster().getDate().toEpochDay() + " Tag(e) " + ConsoleColors.YELLOW + " im "  +   ClusterReq.lookUpLocationIfShop() 
										+ ConsoleColors.RESET + " einkaufen");
									} else if(ClusterReq.makeNewDetails().equals("fast_food") || ClusterReq.makeNewDetails().equals("restaurant") ){ 
										System.out.println(" 	Dabei bekam ich Hunger und war für " + ConsoleColors.BLUE +  Cluster.getCenterOfCluster().getTime() + 
										" und "+Cluster.getCenterOfCluster().getDate().toEpochDay() + " Tag(e) " + ConsoleColors.YELLOW + " in "  +   ClusterReq.lookUpWhatLocation() 
										+ ConsoleColors.RESET + " essen");
									}
									
									else {
										System.out.println(" 	Dabei war ich für " + ConsoleColors.BLUE +  Cluster.getCenterOfCluster().getTime() + 
										" und "+Cluster.getCenterOfCluster().getDate().toEpochDay() + " Tag(e) " + ConsoleColors.YELLOW + " in "  +   ClusterReq.lookUpWhatLocation() 
										+ ConsoleColors.RESET);
									}
									
									// System.out.println("Anzahl: " + Cluster.getCoordinatesInCluster().size() );
									// 	System.out.println("Wert des Clusters: " + DBSCAN.calcWorthOfClusterList(Cluster.getCoordinatesInCluster()));

									// if (j > 2) {  // DEBUGGING
									// 	System.out.println("Distanz: " + Calc.calcDistance(Cluster.getCenterOfCluster(), Clusters.get(j-1).getCenterOfCluster()));
									// 	System.out.println( );

										
									// }

								}


								

							}						

							System.out.println( );
							System.out.println("------------------------------------------------------------------------");
							System.out.println( );

						}

						if (VelocityClutering.getByCycleSegments().get(gpxCoordinates) != null) {
							
							ClusterReq.makeNewRequest(gpxCoordinates.getEastern_longitude(), gpxCoordinates.getNorthern_latitude());
							SecClusterReq.makeNewRequest(VelocityClutering.getByCycleSegments().get(gpxCoordinates).getEastern_longitude(),
								 VelocityClutering.getByCycleSegments().get(gpxCoordinates).getNorthern_latitude());

							System.out.println("Um " + ConsoleColors.RED +  gpxCoordinates.getTime().plusHours(2) + ConsoleColors.RESET + "  bin ich von " + ConsoleColors.YELLOW
								 + ClusterReq.lookUpWhatLocation()  + ConsoleColors.RESET+  " bis " + ConsoleColors.YELLOW +
								SecClusterReq.lookUpWhatLocation() + ConsoleColors.WHITE_BOLD_BRIGHT + " Fahrrad " + ConsoleColors.RESET + 
								" gefahren und kam gegen " +  ConsoleColors.RED +
								VelocityClutering.getByCycleSegments().get(gpxCoordinates).getTime().plusHours(2) +  ConsoleColors.RESET + " Uhr an");
							
							
							System.out.println( );
							System.out.println("------------------------------------------------------------------------");
							System.out.println( );

						}

						if (VelocityClutering.getTramSegments().get(gpxCoordinates) != null) {
							ClusterReq.makeNewRequest(gpxCoordinates.getEastern_longitude(), gpxCoordinates.getNorthern_latitude());
							SecClusterReq.makeNewRequest(VelocityClutering.getTramSegments().get(gpxCoordinates).getEastern_longitude(),
								 VelocityClutering.getTramSegments().get(gpxCoordinates).getNorthern_latitude());


	
							System.out.println("Um " + ConsoleColors.RED + gpxCoordinates.getTime().plusHours(2) + ConsoleColors.RESET +
							 "  bin ich von der " + ConsoleColors.WHITE_BOLD_BRIGHT +"  Straßenbahnhaltestelle " + ConsoleColors
							.YELLOW + ClusterReq.LookUpResponseIfRailway()  + ConsoleColors.RESET+  " bis " + ConsoleColors.YELLOW + 
								SecClusterReq.LookUpResponseIfRailway() + ConsoleColors.RESET + " mit der Straßenbahn gefahren und kam gegen " +
								ConsoleColors.RED + VelocityClutering.getTramSegments().get(gpxCoordinates).getTime().plusHours(2) + ConsoleColors.RESET + " Uhr an");

							System.out.println( );
							System.out.println("------------------------------------------------------------------------");
							System.out.println( );
						}

						if (VelocityClutering.getTrainSegments().get(gpxCoordinates) != null) {
							ClusterReq.makeNewRequest(gpxCoordinates.getEastern_longitude(), gpxCoordinates.getNorthern_latitude());
							SecClusterReq.makeNewRequest(VelocityClutering.getTrainSegments().get(gpxCoordinates).getEastern_longitude(),
								 VelocityClutering.getTrainSegments().get(gpxCoordinates).getNorthern_latitude());

							System.out.println("Um " +  ConsoleColors.RED + gpxCoordinates.getTime().plusHours(2) + ConsoleColors.RESET +  "  bin ich vom Bahnhof " +ConsoleColors.YELLOW + 
							 ClusterReq.lookUpWhatLocation() + ConsoleColors.RESET+  " bis " + ConsoleColors.YELLOW + SecClusterReq.lookUpWhatLocation()
							  + ConsoleColors.RESET  + " mit dem " + ConsoleColors.WHITE_BOLD_BRIGHT+ " Zug "+ ConsoleColors.RESET + " gefahren und kam gegen " +
								ConsoleColors.RED + VelocityClutering.getTrainSegments().get(gpxCoordinates).getTime().plusHours(2) + ConsoleColors.RESET + " Uhr an");

							System.out.println( );
							System.out.println("------------------------------------------------------------------------");
							System.out.println( );
						}

						if (VelocityClutering.getUnmarkedSegments().get(gpxCoordinates) != null) {
							ClusterReq.makeNewRequest(gpxCoordinates.getEastern_longitude(), gpxCoordinates.getNorthern_latitude());
							SecClusterReq.makeNewRequest(VelocityClutering.getUnmarkedSegments().get(gpxCoordinates).getEastern_longitude(),
								 VelocityClutering.getUnmarkedSegments().get(gpxCoordinates).getNorthern_latitude());

							System.out.println("Um " + ConsoleColors.RED + gpxCoordinates.getTime().plusHours(2) + ConsoleColors.RESET + " bin ich von " + ConsoleColors.YELLOW +
							 ClusterReq.lookUpWhatLocation()  + ConsoleColors.RESET+  " bis " + ConsoleColors.YELLOW + SecClusterReq.lookUpWhatLocation() +
							 ConsoleColors.RESET + " mit dem " + ConsoleColors.WHITE_BOLD_BRIGHT +" Auto " + ConsoleColors.RESET +" gefahren und kam gegen " +
								ConsoleColors.RED + VelocityClutering.getUnmarkedSegments().get(gpxCoordinates).getTime().plusHours(2) + ConsoleColors.RESET + " Uhr an");

							System.out.println( );
							System.out.println("------------------------------------------------------------------------");
							System.out.println( );
						}



					}

				}

	


			}

			scanner.close();
			


	
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		
	}


	static boolean isBetweenDates(LocalDateTime TestDate, LocalDateTime startDate, LocalDateTime endDate ) {
        return !(TestDate.isBefore(startDate) || TestDate.isAfter(endDate));
    }

	static CalcVelocityCluster makeVelocityClusterObject(List<gpxCoordinates> liste) throws SAXException, IOException, ParserConfigurationException, URISyntaxException {
		return new CalcVelocityCluster(true,5,12.0,0.09,
						40.0, 3,
						75.0, 3,4,
						liste);
	}

	static List<ClusterOfCoordinates> FindClustersForSegment(List<gpxCoordinates> liste, CalcVelocityCluster VelocityClutering,gpxCoordinates gpxCoordinates) {
		

		List<ClusterOfCoordinates> Clusters = new ArrayList<>(); // Verlagerung
		List<gpxCoordinates> footListForOneSegment = new ArrayList<>();

		for (gpxCoordinates gpxCoordinatesOfFoot : liste) {	

			gpxCoordinates EndingGpx = VelocityClutering.getSegmentsOfFoot().get(gpxCoordinates);

			if (isBetweenDates(LocalDateTime.of(gpxCoordinatesOfFoot.getDate(),gpxCoordinatesOfFoot.getTime()),
				LocalDateTime.of(gpxCoordinates.getDate(),gpxCoordinates.getTime()), LocalDateTime.of(EndingGpx.getDate(),EndingGpx.getTime()))) {
				footListForOneSegment.add(gpxCoordinatesOfFoot);
									
			}
							
		}
		if (footListForOneSegment.size() > 0) {

			double dis =0;
			for (int i = 1; i < footListForOneSegment.size(); i++) {
				dis += Calc.calcDistance(footListForOneSegment.get(i-1), footListForOneSegment.get(i));
			}

			// System.out.println("Durchschnittlicher Abstand: " + (dis/(double)footListForOneSegment.size()));
			// System.out.println("Anzahl an GPX Punkten: " + footListForOneSegment.size());

			// System.out.println( ); // DEBUGGING

			Clusters.addAll(DBSCAN.DBSCANmain(footListForOneSegment,(dis/(double)footListForOneSegment.size()) * 2.0 ,20));

			

			for (int i = 2; i < Clusters.size();) { 
			
				if (Clusters.get(i).getCenterOfCluster().getTime().compareTo(LocalTime.of(0,4,0)) < 0 ) { // Zu kurze Cluster werden gelöscht
					Clusters.remove(i);
					continue;
				}

				if ( (i+1) < Clusters.size() && (Calc.calcDistance(Clusters.get(i).getCenterOfCluster(), Clusters.get(i+1).getCenterOfCluster()) < 0.1) ) { // DistanzParameter
					Clusters.get(i).getCoordinatesInCluster().addAll(Clusters.remove(i+1).getCoordinatesInCluster());  // Cluster die zu nah sind, werden miteinander verschmolzen
				
					DBSCAN.findCentersOfOneCluster(Clusters.get(i)); 
				} else i++;
			}




		}



		return Clusters;
	}
}
