package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by SETU on 1/20/2015.
 */
public class FriendsStatusFragment extends Fragment{

    private TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.friend_status,null,false);
        //text=(TextView) v.findViewById(R.id.justfr);
        return v;

    }
}
