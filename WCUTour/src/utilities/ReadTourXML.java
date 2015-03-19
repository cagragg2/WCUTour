package utilities;
import java.util.ArrayList;
import java.util.List;

import models.TourPoint;
import models.Waypoint;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * This class parses XML files that contain the points that make up the polyline for the tour on the map.
 */
public class ReadTourXML extends DefaultHandler{
    /** TourPoint object */
    private TourPoint point;
    /** Boolean values for latitude/longitude */
    private boolean bLat = false;
    private boolean bLat1 = false;
    private boolean bLong = false;
    private boolean bLong2 = false;
    /** List of TourPoint objects that make up the tour */
    private List<TourPoint> tourList = null;

    /**
     * This method returns the list of TourPoint objects.
     */
    public List<TourPoint> getTourList(){
        return tourList;
    }




    /**
     * This method handles the first element of the xml file.
     * @param uri - resource identifier
     * @param localName - location of the element
     * @param qName - name 
     * @param attributes - the attributes of the element
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.equalsIgnoreCase("Point")) {
            String id = attributes.getValue("id");
            point = new TourPoint();
            point.setId(Integer.parseInt(id));
            //initialize list
            if (tourList == null)
                tourList = new ArrayList<TourPoint>();
        } else if (qName.equalsIgnoreCase("lat")) {
            bLat1 = true;
        } else if (qName.equalsIgnoreCase("lng")) {
            bLong2 = true;
        }

    }
    /**
     * This method handles the last element of the xml file.
     * @param uri - resource identifier
     * @param localName - name of location of tag
     * @param qName - name 
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if(qName.equalsIgnoreCase("Point")){
            tourList.add(point);
        }
    }
    /**
     * This method parses the characters within the each element in the xml file.
     * @param char - array holding all the characters.
     * @param start - start of the array
     * @param length - length of the array
     */
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (bLat1) {
            point.setLatitude(Double.parseDouble(new String(ch, start, length)));
            bLat1 = false;
            bLat = false;
        } else if (bLong2) {
            point.setLongitude(Double.parseDouble(new String(ch, start, length)));
            bLong2 = false;
            bLong = false;
        }
        if (bLat == true) {
            point.setLatitude(Double.parseDouble(new String(ch, start, length)));
            bLat = false;
        } else if (bLong == true) {
            point.setLongitude(Double.parseDouble(new String(ch,start,length)));
            bLong = false;
        }
    }
}
