package com.mapit.backend;

import java.util.ArrayList;

/**
 * Created by shubhashis on 1/27/2015.
 */
public class KMP {
        public int[] prekmp(String pattern) {
            int[] next = new int[pattern.length()];
            int i=0, j=-1;
            next[0]=-1;
            while (i<pattern.length()-1) {
                while (j>=0 && pattern.charAt(i)!=pattern.charAt(j))
                    j = next[j];
                i++;
                j++;
                next[i] = j;
            }
            return next;
        }

    public int kmp(String text, String pattern) {
        int[] next = prekmp(pattern);
        int i=0, j=0;
        while (i<text.length()) {
            while (j>=0 && text.charAt(i)!=pattern.charAt(j))
                j = next[j];
            i++; j++;
            if (j==pattern.length()) {
                return ( i - pattern.length() );
            }
        }
        return -1;
    }

    public ArrayList<Search> FilterField(ArrayList<Search>inp, String pattern) {
        ArrayList <Search> result = new ArrayList<Search>();
        for(int i = 0 ; i < inp.size() ; i++)
        {
            Search s = inp.get(i);
            if(kmp(s.getData().toLowerCase(), pattern.toLowerCase()) >= 0)
            {
                result.add(s);
            }
        }

        return result;
    }
}

