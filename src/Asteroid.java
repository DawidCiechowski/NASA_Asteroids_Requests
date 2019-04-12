import java.sql.Date;
import java.util.Formatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class Asteroid {

	// Metric measurements 
	
	@JsonProperty("id") String id;
			
	Date getApproachDate() {
		return Date.valueOf(closeApproachData.get(0).get("close_approach_date").asText());
	}
	
	protected @JsonProperty("close_approach_data") JsonNode closeApproachData;
	
	Float getRelativeVelocity() {
		return Float.parseFloat(closeApproachData.get(0).get("relative_velocity").get("kilometers_per_hour").textValue());
	}
	
	@JsonProperty("name") String name;
		
	@JsonProperty("estimated_diameter") Float estimatedDiameter;
	
	Float getEstimatedDiameter() {
		return estimatedDiameter;
	}
	
	void setEstimatedDiameter(JsonNode json) {
		estimatedDiameter = json.get(("meters")).get("estimated_diameter_max").floatValue();
	}
	
	@JsonProperty("is_potentially_hazardous_asteroid") boolean isPotentiallyHazardous;
	
	boolean getIsPotentiallyHazardous() {
		return isPotentiallyHazardous;
	}
	
	void setIsPotentiallyHazardous(boolean json) {
		isPotentiallyHazardous = json;
	}
		
	Float getMissDistance() {
		return Float.parseFloat(closeApproachData.get(0).get("miss_distance").get("kilometers").textValue());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter fmt = new Formatter(sb);
		fmt.format("Name: %s%n", name);
		fmt.format("ID: %s%n", id);
		fmt.format("Approach Date: %s%n", getApproachDate());
		fmt.format("Potentially Hazardous: %b%n", isPotentiallyHazardous);
		fmt.format("Estimated Diameter: %d meters%n", Math.round(estimatedDiameter));
		fmt.format("Miss Distance: %d km%n", Math.round(getMissDistance()) / 1000);
		fmt.format("Relative Velocity: %d km/h%n", Math.round(getRelativeVelocity()));
		fmt.close();
		return sb.toString();
	}
	
}