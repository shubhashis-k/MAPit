package com.example.MAPit.Volley.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.MAPit.MAPit.R;
import com.example.MAPit.Volley.app.AppController;
import com.example.MAPit.Volley.data.Comment_Item;

import java.util.List;

/**
 * Created by SETU on 1/22/2015.
 */
public class CommentListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Comment_Item> commentItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CommentListAdapter(Activity activity, List<Comment_Item> commentItems) {
        this.activity = activity;
        this.commentItems = commentItems;
    }

    @Override
    public int getCount() {
        return commentItems.size();
    }

    @Override
    public Object getItem(int location) {
        return commentItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.comment_single_status_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.username_comment);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp_comment_status);
        TextView statuscomment = (TextView) convertView
                .findViewById(R.id.commentmsg_single_status);

        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.user_comment_pic);



        Comment_Item item = commentItems.get(position);

        name.setText(item.getUser_Name());
        profilePic.setImageUrl(item.getUser_Imge(), imageLoader);
        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getComment_TimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        statuscomment.setText(item.getUser_comment());
        return convertView;
    }

}
