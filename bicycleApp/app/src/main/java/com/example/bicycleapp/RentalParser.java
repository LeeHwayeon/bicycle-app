package com.example.bicycleapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class RentalParser {

    private static String APIkey = "6845466350736e773331414a674246";

    public RentalParser(){
        try {
            apiParserSearch();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ArrayList<RentalDTO> apiParserSearch() throws Exception {
        URL url = new URL(getURLParam(null));

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<RentalDTO> rentalDTO = new ArrayList<RentalDTO>();

        String stationName = null, stationLatitude= null, stationLongitude=null;
        boolean bstationName = false, bstationLatitude= false, bstationLongitude=false;
        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
                if(tag.equals("stationName")){
                    bstationName = true;
                }
                if(tag.equals("stationLatitude")){
                    bstationLatitude = true;
                }
                if(tag.equals("stationLongitude")){
                    bstationLongitude = true;
                }
            } else if (event_type == XmlPullParser.TEXT) {
                if(bstationName == true){
                    stationName = xpp.getText();
                    bstationName = false;
                } else if(bstationLatitude == true){
                    stationLatitude = xpp.getText();
                    bstationLatitude = false;
                } else if(bstationLongitude == true){
                    stationLongitude = xpp.getText();
                    bstationLongitude = false;
                }
            } else if (event_type == XmlPullParser.END_TAG){
                tag = xpp.getName();
                if (tag.equals("row")){
                    RentalDTO entity = new RentalDTO();
                    entity.setStationName(stationName);
                    entity.setLatitude(Double.valueOf(stationLatitude));
                    entity.setLongitude(Double.valueOf(stationLongitude));
                    rentalDTO.add(entity);
                    System.out.println(rentalDTO.size());
                }
            }
            event_type = xpp.next();
        }
        System.out.println(rentalDTO.size());

        return rentalDTO;
    }

    private String getURLParam(String search){
        String url = "http://openapi.seoul.go.kr:8088/6845466350736e773331414a674246/json/bikeList/1/1000/";
        return url;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new RentalParser();
    }

}
