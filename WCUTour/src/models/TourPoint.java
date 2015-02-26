package models;

import com.google.android.gms.maps.model.LatLng;
/**
 * A TourPoint is just a pair of coordinates on the tour. A list of these will create
 * the polyline for a tour.
 * @author Jeremiah Griffin, Caleb Gragg
 *
 */

public class TourPoint {


    int id;
    double lat;
    double lng;

    public TourPoint(){

        this.id=0;
        this.lat=0.0;
        this.lng=0.0;

    }

    public TourPoint(int id, double lat, double lng){

        this.id = id;
        this.lat = lat;
        this.lng = lng;

    }

    public double getLatitude(){
        return lat;
    }

    public double getLongitude(){ return lng;}

    public int getId(){ return this.id;}



    public void setLongitude(double value){  this.lng = value; }

    public void setLatitude(double value){
        this.lat = value;
    }
    public void setId(int value){
        this.id = value;
    }

    public LatLng getPoint(){
        LatLng point = new LatLng(this.lat,this.lng);
        return point;
    }

    public String toString(){
        String s = "("+ this.lat +", "+ this.lng+")";
        return s;

    }
}

