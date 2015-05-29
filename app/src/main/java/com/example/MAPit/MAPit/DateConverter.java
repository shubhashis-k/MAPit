package com.example.MAPit.MAPit;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


    public ArrayList<String> MobileFriendly(String time){

        ArrayList <String> formattedDate = new ArrayList<String>();
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
            final Date dateObj = sdf.parse(time);
            System.out.println(dateObj);

            String MonthDate = new SimpleDateFormat("MMMM dd").format(dateObj);
            formattedDate.add(MonthDate);

            String formTime = new SimpleDateFormat("K:mmaa").format(dateObj);
            formattedDate.add(formTime);

        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
}
