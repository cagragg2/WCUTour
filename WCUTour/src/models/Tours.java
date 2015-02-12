package models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *@author Caleb Gragg, Jeremiah Griffin
 *
 * The tours object to hold a list of waypoints for each of the locations
 * in a tour.
 */
public class Tours {

	List<Waypoint> waypoints;
	String tourName = "";
	/**
	 * Constructor takes a list of waypoints and a name for the tour.
	 */
	public Tours(List<Waypoint> way, String name) {
		tourName = name;
		waypoints = way;
	}
	
	public Tours() {
	}
	/**
	 * returns the tour name
	 * @return Name of the tour.
	 */
	public String getTourName() {
		return tourName;
	}
	/**
	 * sets the tour name.
	 * @param name The tour name.
	 */
	public void setTourName(String name) {
		tourName = name;
	}
	/**
	 * sets the waypoints of the tour.
	 * @param way the waypoints to add to the tour.
	 */
	public void setTour(List<Waypoint> way) {
		waypoints = way;
	}
	/**
	 * returns the list of waypoints
	 * @return the list of waypoints in the tour.
	 */
	public List<Waypoint> getTour() {
		return waypoints;
	}
}
