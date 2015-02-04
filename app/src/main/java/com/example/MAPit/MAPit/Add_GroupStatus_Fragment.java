package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by SETU on 1/25/2015.
 */
public class Add_GroupStatus_Fragment extends Fragment {

    private EditText mainMessage,postUrl;
    private ImageView postImage;
    private Button addPost,choosePic;
    private final int SELECT_PHOTO = 1;

    public Add_GroupStatus_Fragment(){setHasOptionsMenu(true);}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.group_addnewpost,null,false);

        mainMessage = (EditText)v.findViewById(R.id.et_group_statuspost);
        postUrl = (EditText)v.findViewById(R.id.et_grouppost_url);
        postImage = (ImageView)v.findViewById(R.id.add_post_pic);
        addPost = (Button)v.findViewById(R.id.group_post_status);
        choosePic = (Button)v.findViewById(R.id.choosepostpic);

        choosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imagepicking = new Intent(Intent.ACTION_PICK);
                imagepicking.setType("image/*");
                startActivityForResult(imagepicking, SELECT_PHOTO);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:

                try {
                    final Uri imageUri = imageReturnedIntent.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    postImage.setImageBitmap(selectedImage);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


        }
    }
}
