package models;

/**
 * 
 * @author Caleb Gragg, Jeremiah Griffin
 *
 * Class waypoint to hold each of the locations as a waypoint.
 */
public class Waypoint {
	
	public double latitude; // lat of the waypoint
	public double longitude; //long of the waypoint
	public String description = ""; // description of the waypoint
	public int id; // id of the waypoint
	public String information = ""; // information about the waypoint.
    public String hours="";
    public String uses="";
    public WaypointInfo info;
	
	/**
	 * Constructor for class waypoint.
	 * 
	 * @param latitude the latitude of the location.
	 * @param longitude the longitude of the location.
	 * @param description the discription of the waypoint.
	 * @param id the id of the waypoint
	 * @param information information about the waypoint
	 */
	public Waypoint(double latitude, double longitude, String description, int id, String information,
                    String hours, String uses) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
		this.id = id;
		this.information = information;
        this.hours = hours;
        this.uses = uses;

        info = new WaypointInfo(this.description,this.information,this.hours,this.uses);
	}
	/**
	 * Constructor of a blank waypoint.
	 */
	public Waypoint() {
		
	}
	
	/**
	 * The latitude of the location
	 * @return the latitude
	 */
	
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * the longitude of the location
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * the description of the location
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the latitude of the location.
	 * @param latitude the new latitude of the waypoint.
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Sets the longitude of the waypoint.
	 * @param longitude the new longitude of the waypoint.
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Sets the description of the waypoint
	 * @param description the new description of the waypoint.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * The id of the waypoint
	 * @return Id of the waypoint
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * sets the id of the waypoint
	 * @param id the new id of the waypoint
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * sets the information of the waypoint
	 * @param info the new information for the location
	 */
	public void setInformation(String info) {
		this.information = info;
	}
	
	/**
	 * gets the information about the waypoint
	 * @return the information of the waypoint
	 */
	public String getInformation() {
		return information;
	}

    /**
     * Gets the information about the waypoint
     * @return the information in an WaypointInfo object.
     */
    public WaypointInfo getInfo() {
        return info;
    }

    /**
     * Sets the info for the waypoint in an WaypointInfo object.
     * @param info The new information for the waypoint.
     */
    public void setInfo(WaypointInfo info) {
        this.info = info;
    }

    /**
     * Sets the hours for the waypoint.
     * @param hours The hours for the waypoint
     */
    public void setHours(String hours){
        this.hours = hours;
    }

    /**
     * Returns the hours for the waypoint.
     * @return The hours of the waypoint.
     */
    public String getHours() {
        return hours;
    }

    /**
     * Sets the uses for the waypoint.
     * @param uses The new uses for the waypoint.
     */
    public void setUses(String uses) {
        this.uses = uses;
    }

    /**
     * Returns the uses for the waypoint
     * @return The uses for the waypoint.
     */
    public String getUses() {
        return uses;
    }
}


