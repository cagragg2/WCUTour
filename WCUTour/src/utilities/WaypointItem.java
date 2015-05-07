package utilities;

/**
 * Created by Caleb Gragg, Jeremiah Griffin on 2/25/2015.
 */
public class WaypointItem {

    /**
     * Title of WaypointItem.
     */
    private String title;
    /**
     *  Distance of WaypointItem.
     */
    private double distance;
    /**
     * Count of waypoint Item.
     */
    private String count = "0";

    /**
     * boolean to set visiblity of the counter
     */
    private boolean isCounterVisible = false;

    /**
     * No Arg constructor.
     */
    public WaypointItem(){}

    /**
     * Constructor for WaypointItem.
     * @param title - title of Waypoint Item.
     * @param distance - distance of WaypointItem.
     */
    public WaypointItem(String title, double distance){
        this.title = title;
        this.distance = distance;
    }

    /**
     * Get the title of the Waypoint Item.
     * @return the title .
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * Get the distance of the Waypoint Item.
     * @return the distance.
     */
    public double getDistance(){
        return this.distance;
    }

    /**
     * Get the count of the Waypoint Item.
     * @return the count.
     */
    public String getCount(){
        return this.count;
    }

    /**
     * See if the WaypointItem is visible.
     * @return true if visible.
     */
    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    /**
     * Set the title of waypoint item.
     * @param title - new title.
     */
    public void setTitle(String title){
        this.title = title;
    }


}
