package utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.Waypoint;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class reads an XML file of Waypoint data and builds a list representing the data.
 *
 * @author Jeremiah Griffin, Caleb Gragg
 * @version 3/12/2015
 */
public class ReadXMLFile extends DefaultHandler{
    /**
     * Waypoint object to add to list.
     */
    private Waypoint waypoint;
    /**
     * LIst of waypoints.
     */
    private List<Waypoint> wayList = null;
    /**
     * True if we have received a latitude value.
     */
    boolean bLat = false;
    /**
     * True if we have recieved a longitude value.
     */
    boolean bLong = false;
    /**
     * True if we have received a description.
     */
    boolean bDesc = false;
    /**
     * True if we have received info.
     */
    boolean bInfo = false;
    /**
     * True if we have hours of operation.
     */
    boolean bHours = false;
    /**
     * True if we have used for waypoint.
     */
    boolean bUses = false;

    /**
     * This method returns a the list of waypoints.
     * @return
     */
    public List<Waypoint> getWayList() {
        return wayList;
    }


    /**
     * THis method handles the start element of an XML block.
     * @param uri - the uri of the element.
     * @param localName - the name of the tag.
     * @param qName - name of element.
     * @param attributes - attributes of element.
     * @throws SAXException -
     */
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
    /**
     * This method handles when the parser has reached the end of an element. It adds that point to
     * the list.
     * @param uri - uri of the element
     * @param localName - name of block
     * @param qName - name of element.
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Location")) {
            wayList.add(waypoint);
        }
    }
    /**
     * this method handles the data within tags and builds a point to add to the list.
     * @param ch -  character array.
     * @param start - start index of array.
     * @param length - length of array.
     * @throws SAXException
     */
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
            bHours =false;
        } else if (bUses) {
            waypoint.setUses(new String(ch,start,length));
            bUses = false;
        }
    }
}
    


