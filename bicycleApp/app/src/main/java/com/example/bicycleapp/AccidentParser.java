package com.example.bicycleapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class AccidentParser {
    private static String APIkey = "i7hUwTM8Zfmbt9KD8A%2Bgeh1N61Yl%2FY50rxRFI%2BiR0psYWixhym4%2BfpIKX0hiAB%2Fs";

    public AccidentParser(){
        try {
            apiParserSearch();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ArrayList<AccidentDTO> apiParserSearch() throws Exception {
        URL url = new URL(getURLParam(null));

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<AccidentDTO> accidentDTO = new ArrayList<AccidentDTO>();

        String accident_num = null, latitude= null, longitude=null;
        boolean baccident_num = false, blatitude= false, blongitude=false;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
                if(tag.equals("accident_num")){
                    baccident_num = true;
                }
                if(tag.equals("latitude")){
                    blatitude = true;
                }
                if(tag.equals("longitude")){
                    blongitude = true;
                }
            } else if (event_type == XmlPullParser.TEXT) {
                if(baccident_num == true){
                    accident_num = xpp.getText();
                    baccident_num = false;
                } else if(blatitude == true){
                    latitude = xpp.getText();
                    blatitude = false;
                } else if(blongitude == true){
                    longitude = xpp.getText();
                    blongitude = false;
                }
            } else if (event_type == XmlPullParser.END_TAG){
                tag = xpp.getName();
                if (tag.equals("row")){
                    AccidentDTO entity = new AccidentDTO();
                    entity.setAccident(accident_num);
                    entity.setLatitude(Double.valueOf(latitude));
                    entity.setLongitude(Double.valueOf(longitude));
                    accidentDTO.add(entity);
                    System.out.println(accidentDTO.size());
                }
            }
            event_type = xpp.next();
        }
        System.out.println(accidentDTO.size());

        return accidentDTO;
    }

    private String getURLParam(String search){
        String url = "http://taas.koroad.or.kr/data/rest/frequentzone/bicycle?authKey="+APIkey+"&searchYearCd=2018" +
                "&siDo=11" +
                "&guGun=680";
        return url;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new AccidentParser();
    }

}
