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
    public String waypointName="";
    public String information="";
    public String hours="";
    public String uses="";

    public WaypointInfo(String waypointName, String information,String hours,String uses) {
        this.waypointName = waypointName;
        this.information = information;
        this.hours = hours;
        this.uses = uses;
    }

    public void setWaypointName(String waypointName) {
        this.waypointName = waypointName;
    }
    public void setInformation(String information){
        this.information = information;
    }
    public String getWaypointName() {
        return waypointName;
    }
    public String getInformation() {
        return information;
    }
    public void setHours(String Hours) {
        this.hours = hours;
    }
    public String getHours() {
        return hours;
    }
    public void setUses(String uses){
        this.uses = uses;
    }
    public String getUses() {
        return  uses;
    }
}
