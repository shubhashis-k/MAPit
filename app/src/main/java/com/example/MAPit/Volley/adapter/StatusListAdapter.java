package com.example.MAPit.Volley.adapter;

/**
 * Created by SETU on 1/20/2015.
 */

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MAPit.MAPit.ImageConverter;
import com.example.MAPit.MAPit.R;
import com.example.MAPit.Volley.data.StatusListItem;

public class StatusListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<StatusListItem> statusListItems;
    ImageView feedImageView;


    public StatusListAdapter(Activity activity, List<StatusListItem> statusListItems) {
        this.activity = activity;
        this.statusListItems = statusListItems;
    }

    @Override
    public int getCount() {
        return statusListItems.size();
    }

    @Override
    public Object getItem(int location) {
        return statusListItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);


        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView location = (TextView) convertView.findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilePic);
        feedImageView = (ImageView) convertView.findViewById(R.id.feedImage1);

        StatusListItem item = statusListItems.get(position);
        if(item.getProfilePic()!=null){
            profilePic.setImageBitmap(ImageConverter.stringToimageConverter(item.getProfilePic()));
       }else{
            profilePic.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_profile));
        }
        name.setText(item.getName());
        statusMsg.setText(item.getStatus());
        location.setText(item.getLocation());

        if(item.getImge()!=null) {
            Log.v("image", "called");
            cmplx c = new cmplx();
            c.v = convertView;
            c.imageText = item.getImge();

            new ImageSetter().execute(c);
        }
        // Converting timestamp into x ago format

        /*
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);
        */


        // user profile pic and converte the item.getProfilePic() to bitmap

        //profilePic.setImageBitmap(bp);
        Log.v("call", "scrolled");
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left);
        convertView.startAnimation(animation);
        return convertView;
    }
    class cmplx{
        View v;
        String imageText;
        Bitmap b;
        int index;
    }

    public class ImageSetter extends AsyncTask <cmplx, Void, cmplx> {
        private Context context;
        @Override
        protected cmplx doInBackground(cmplx... params) {

            params[0].b = ImageConverter.stringToimageConverter(params[0].imageText);

            return params[0];
        }

        @Override
        protected void onPostExecute(cmplx c) {
            super.onPostExecute(c);

            View thisview = c.v;
            Bitmap b = c.b;

            ImageView thisImageView = (ImageView) thisview.findViewById(R.id.feedImage1);
            thisImageView.setImageBitmap(b);
        }
    }
}