package models;

import com.google.android.gms.maps.model.LatLng;
/**
 * A TourPoint is just a pair of coordinates on the tour. A list of these will create
 * the polyline for a tour.
 * @author Jeremiah Griffin, Caleb Gragg
 *
 */

public class TourPoint {

    /** ID of the pair of coordinates. */
    int id;
    /** latitude value. */
    double lat;
    /** longitude value. */
    double lng;

    /**
     * Constructor for TourPoint object. 
     */
    public TourPoint(){

        this.id=0;
        this.lat=0.0;
        this.lng=0.0;

    }
    /**
     * Constructor for TourPoint object. 
     * @param id - id of the point. 
     * @param lat - latitude
     * @param lng - longitude
     */
    public TourPoint(int id, double lat, double lng){

        this.id = id;
        this.lat = lat;
        this.lng = lng;

    }
    /**
     * This method returns the latitude value of a point.
     * @return double - latitude
     */
    public double getLatitude(){ return lat;}
    /**
     * This method returns the longitude value of a point.
     * @return double - longitude
     */
    public double getLongitude(){ return lng;}
    /**
     * This method returns the id of the point. 
     * @return int - id
     */
    public int getId(){ return this.id;}
    /**
     * This method sets the longitude value.
     * @param value - longitude
     */
    public void setLongitude(double value){  this.lng = value; }
    /**
     * This method sets the latitude value.
     * @param value - latitude
     */
    public void setLatitude(double value){ this.lat = value;}
    /**
     * This method sets the id for the point.
     * @param value - id
     */
    public void setId(int value){ this.id = value;}
    /**
     * This method returns a LatLng point with the latitude, longitude values.
     * @return LatLng - new point to build polyline for tour.
     */
    public LatLng getPoint(){
        LatLng point = new LatLng(this.lat,this.lng);
        return point;
    }
    /**
     * Basic toString method for TourPoint object.
     * @return String - String representation of TourPoint object.
     */
    public String toString(){
        String s = "("+ this.lat +", "+ this.lng+")";
        return s;

    }
}

