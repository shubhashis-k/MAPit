package com.mapit.backend;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shubhashis on 5/20/2015.
 */
public class DateConverter {
    public Date StringToDate(String stringDate){
        //format yyyy-MM-dd-hh-mm
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd-hh-mm");

        Date date;

        try {
            date = ft.parse(stringDate);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }


    public String DateToString(Date date){

        Format formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String s = formatter.format(date);

        return s;
    }
}
