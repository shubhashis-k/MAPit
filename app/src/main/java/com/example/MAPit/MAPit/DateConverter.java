package com.example.MAPit.MAPit;

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


    public String MobileFriendly(String stringDate){
        DateConverter dc = new DateConverter();
        Date date = dc.StringToDate(stringDate);
        String modf = "";
        int Count = 0;
        String dateString = date.toString();
        for(int i = 0 ; i < dateString.length(); i++)
        {
            if(dateString.charAt(i) == ':')
                Count++;
            if(Count == 2)
                break;

            modf+=dateString.charAt(i);
        }

        return modf;
    }
}
