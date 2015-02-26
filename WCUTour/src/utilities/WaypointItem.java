package utilities;

/**
 * Created by Caleb Gragg, Jeremiah Griffin on 2/25/2015.
 */
public class WaypointItem {

    private String title;
    //private int icon;
    private double distance;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public WaypointItem(){}

    public WaypointItem(String title, double distance){
        this.title = title;
        this.distance = distance;
    }

    public WaypointItem(String title, double distance, boolean isCounterVisible, String count){
        this.title = title;
        this.distance = distance;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle(){
        return this.title;
    }

    public double getDistance(){
        return this.distance;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}
