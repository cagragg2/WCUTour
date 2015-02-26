package models;

import android.content.Intent;

import java.util.ArrayList;

/**
 * @author Caleb Gragg, Jeremiah Griffin
 * @version 2/23/2015
 *
 * Java class to hold the information about the waypoint.
 */
public class WaypointInfo {
    public String waypointName=""; //name of the waypoint
    public String information="";//information about the waypoint
    public String hours="";//hours of the waypoint
    public String uses=""; //uses for the waypoint


    /**
     * Constructor to initialize all of the values for he information about a given waypoint.
     * @param waypointName
     * @param information
     * @param hours
     * @param uses
     */
    public WaypointInfo(String waypointName, String information,String hours,String uses) {
        this.waypointName = waypointName;
        this.information = information;
        this.hours = hours;
        this.uses = uses;
    }

    /**
     * Sets the name of the waypoint
     * @param waypointName
     */
    public void setWaypointName(String waypointName) {
        this.waypointName = waypointName;
    }

    /**
     * Sets the information of the waypoint
     * @param information
     */
    public void setInformation(String information){
        this.information = information;
    }


    /**
     * Gets the name of the waypoint.
     * @return
     */
    public String getWaypointName() {
        return waypointName;
    }

    /**
     * Gets the information of the waypoint.
     * @return
     */
    public String getInformation() {
        return information;
    }

    /**
     * Sets the hours for the waypoint.
     * @param Hours
     */
    public void setHours(String Hours) {
        this.hours = hours;
    }

    /**
     * Gets the hours for the waypoint
     * @return
     */
    public String getHours() {
        return hours;
    }

    /**
     * Sets the uses for the waypoint
     * @param uses
     */
    public void setUses(String uses){
        this.uses = uses;
    }

    /**
     * gets the uses for the waypoint.
     * @return
     */
    public String getUses() {
        return  uses;
    }
}
