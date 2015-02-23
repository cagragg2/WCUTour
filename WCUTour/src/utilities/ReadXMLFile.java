package utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.Waypoint;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadXMLFile extends DefaultHandler{

	private Waypoint waypoint;
    private List<Waypoint> wayList = null;

    public List<Waypoint> getWayList() {
    	return wayList;
    }
    
    boolean bLat = false;
    boolean bLong = false;
    boolean bDesc = false;
    boolean bInfo = false;
    boolean bHours = false;
    boolean bUses = false;
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
        if (qName.equalsIgnoreCase("Location")) {
            String id = attributes.getValue("id");
            waypoint = new Waypoint();
            waypoint.setID(Integer.parseInt(id));
            //initialize list
            if (wayList == null)
                wayList = new ArrayList<Waypoint>();
        } else if (qName.equalsIgnoreCase("lat")) {
            bLat = true;
        } else if (qName.equalsIgnoreCase("long")) {
            bLong = true;
        } else if (qName.equalsIgnoreCase("desc")) {
            bDesc = true;
        } else if (qName.equals("information")) {
        	bInfo = true;
        } else if (qName.equals("hours")) {
            bHours = true;
        } else if (qName.equals("uses")) {
            bUses = true;
        }
        
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Location")) {
            wayList.add(waypoint);
        }
    }
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
 
        if (bLat) {
            waypoint.setLatitude(Double.parseDouble(new String(ch, start, length)));
            bLat = false;
        } else if (bLong) {
            waypoint.setLongitude(Double.parseDouble(new String(ch, start, length)));
            bLong = false;
        } else if (bDesc) {
            waypoint.setDescription(new String(ch, start, length));
            bDesc = false;
        } else if (bInfo) {
        	waypoint.setInformation(new String(ch, start,length));
        	bInfo = false;
        } else if(bHours) {
            waypoint.setHours(new String(ch,start,length));
            Log.e("Tour",waypoint.getHours());
            bHours =false;
        } else if (bUses) {
            waypoint.setUses(new String(ch,start,length));
            bUses = false;
        }
    }
}
    


