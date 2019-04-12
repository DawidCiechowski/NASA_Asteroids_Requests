import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Main {
	
static String API_KEY = "uxickliHQnKSJqa7sl3gfdWt6Fw1Oct7rzzxDzHB";
	
	
	
	
//	static String START_DATE = "2018-12-21";
//	static String END_DATE = "2018-12-28";
	
	
	
	
	static String urlBaseString = "https://api.nasa.gov/neo/rest/v1/feed?";
	
	
	
	
	static ObjectMapper mapper = new ObjectMapper();

	public static String getAsteroidsRootJSON(LocalDate start_date, LocalDate end_date) {
		String urlString = urlBaseString + "start_date=" + start_date + "&" + "end_date=" + end_date + "&" + "api_key=" + API_KEY;
		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			con.setUseCaches(false);
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine = "";
			StringBuffer response = new StringBuffer();
			while ((inputLine = rd.readLine()) != null) {
				response.append(inputLine);
			}
			rd.close();
			return response.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonNode getDateFields(String jsonAsteroidObjectHierarchy) {
		try {
			return mapper.readTree(jsonAsteroidObjectHierarchy).get("near_earth_objects");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<JsonNode> getAsteroidsJSON(JsonNode nearEarthObjectsJSON) {
		ArrayList<JsonNode> jsonNodes = new ArrayList<JsonNode>();
		for (JsonNode dateField : nearEarthObjectsJSON) {
			for (JsonNode asteroidField : dateField) {
				jsonNodes.add(asteroidField);
			}
		}
		return jsonNodes;
	}
	
	public static ArrayList<Asteroid> getAsteroids(ArrayList<JsonNode> jsonNodes) {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
		for (JsonNode jsonNode : jsonNodes) {
			try {
				asteroids.add(mapper.readValue(jsonNode.toString(), Asteroid.class));
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return asteroids;
	}

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		Instant start = Instant.now();
		System.out.println("Start time: " + Calendar.getInstance().getTime());
		LocalDate START_DATE = LocalDate.parse("2017-01-01");
		LocalDate END_DATE = LocalDate.parse("2022-01-01");
		//System.out.println(daysOverall(START_DATE, END_DATE));
		ArrayList<Asteroid> asteroids = roids(START_DATE, END_DATE);
//		Asteroid[] asteroidArr = asteroids.toArray(new Asteroid[asteroids.size()]);
//		Arrays.sort(asteroidArr, new SortBySize());
		
		System.out.println("A list of info of near-Earth Asteroids (NEAs) between " +  START_DATE + " and " + END_DATE);
		 
//		for (Asteroid asteroid : asteroidArr) {
//			System.out.println(asteroid); 
//		}
		
//		for(Asteroid asteroid : asteroids) {
//			System.out.println(asteroid);
//		}
		
		//Print out information regarding asteroids
		System.out.println("Number of asteroids:" + asteroids.size());
		Instant end = Instant.now();
		System.out.println("End time: " + Calendar.getInstance().getTime());
		long difference = Duration.between(start, end).toMillis();
		System.out.println("Time taken: " + difference/1000 + " seconds");

	}
	
	public static ArrayList<Asteroid> roids(LocalDate startDate, LocalDate endDate) {
		//Variables
		ArrayList<Asteroid> ast = new ArrayList<>();
		int numberOfDays = daysOverall(startDate, endDate);
		int numberOfWeeks = numberOfDays / 7;
		int leftover = numberOfDays % 7;
		
		System.out.println("Weeks: " + numberOfWeeks + " Leftover: " + leftover);
		
		if(numberOfWeeks == 0) {
			while(startDate.isBefore(endDate)) {
				ast.addAll(getAsteroids(getAsteroidsJSON(getDateFields(getAsteroidsRootJSON(startDate, startDate)))));
				startDate = startDate.plusDays(1);
			}
		} else if(numberOfWeeks > 0) {
			if(leftover > 0) {
				LocalDate leftoverDays = endDate.minusDays(leftover);
				while(!leftoverDays.isAfter(endDate)) {
					ast.addAll(getAsteroids(getAsteroidsJSON(getDateFields(getAsteroidsRootJSON(leftoverDays, endDate)))));
					leftoverDays = leftoverDays.plusDays(1);
				}
			}
			LocalDate s = startDate;
			LocalDate e = startDate.plusDays(7);
			
			for(int i = 0; i < numberOfWeeks; i++) {	
				ast.addAll(getAsteroids(getAsteroidsJSON(getDateFields(getAsteroidsRootJSON(s, e)))));
				s = s.plusDays(7);
				e = e.plusDays(7);
			}

		}
		
		return ast;
	}
	
	public static int daysOverall(LocalDate s, LocalDate e) {
		int days = 0;
		
		while(!s.isEqual(e)) {
			days++;
			s = s.plusDays(1);
		}
		
		return days;
	}

}

class SortBySize implements Comparator<Asteroid> {
	
	public int compare(Asteroid a, Asteroid b) {
		return Math.round(a.estimatedDiameter) - Math.round(b.estimatedDiameter);
	}
	
}
