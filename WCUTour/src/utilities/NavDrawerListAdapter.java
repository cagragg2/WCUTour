package utilities;

import edu.wcu.wcutour.*;


import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 *
 * @author Caleb Gragg, Jeremiah Griffin
 *
 * Class to set up the navigation drawer.
 *
 */
public class NavDrawerListAdapter extends BaseAdapter {
    /**
     * Context object.
     */
    private Context context;
    /**
     * ArrayList for nav drawer items.
     */
    private ArrayList<NavDrawerItem> navDrawerItems;

    /**
     * Constructor for nav drawer list adapter.
     * @param context - context for adapter.
     * @param navDrawerItems - list of items to display in nav drawer.
     */
    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    /**
     * Get the count of nav drawer items.
     * @return number of items in nav drawer.
     */
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    /**
     * Get an item from the nav drawer.
     * @param position - the position of the desired item.
     * @return - the nav drawer item.
     */
    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    /**
     * Get the id of the item.
     * @param position - position of the item.
     * @return - the id of the item.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method gets the desired View.
     * @param position - the position of the view requested.
     * @param convertView - the actual view.
     * @param parent the parent of the View object.
     * @return - the view requested.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
        // Finding all the views.
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
        // Setting icon and text.
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if(navDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }

}