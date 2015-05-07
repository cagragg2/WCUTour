package utilities;

/**
 *
 * @author Caleb Gragg, Jeremiah Griffin
 *
 * For each of the items in the Navigation drawer.
 *
 */
public class NavDrawerItem {
    /**
     * Title of the nav drawer.
     */
    private String title;
    /**
     * Icon of the item.
     */
    private int icon;
    /**
     * Count of the item
     */
    private String count = "0";
    /**
     *  boolean to set visiblity of the counter
     */

    private boolean isCounterVisible = false;

    /**
     * No Arg constructor for nav drawer item.
     */
    public NavDrawerItem(){}

    /**
     * Constructor for nav drawer item.
     * @param title - title of item .
     * @param icon - icon on item.
     */
    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    /**
     * Constructor for nav drawer item.
     * @param title - title of item.
     * @param icon
     * @param isCounterVisible
     * @param count
     */
    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    /**
     * This method returns the title of the item.
     * @return - the title
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * this method gets the icon of the item.
     * @return - the icon
     */
    public int getIcon(){
        return this.icon;
    }

    /**
     * Get the count of the item.
     * @return value of count.
     */
    public String getCount(){
        return this.count;
    }

    /**
     * Returns whether or not counter is visible.
     * @return true if visible.
     */
    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    /**
     * Set the title of the item.
     * @param title - value for the new title.
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Set the icon of the item.
     * @param icon - the new icon id.
     */

    public void setIcon(int icon){
        this.icon = icon;
    }

    /**
     * Setting the count of the item.
     * @param count - value of new count.
     */
    public void setCount(String count){
        this.count = count;
    }

    /**
     * Setting visibility of item.
     * @param isCounterVisible - true or false.
     */
    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}