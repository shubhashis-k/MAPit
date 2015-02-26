package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.DatastoreKindNames;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.mapit.backend.statusApi.model.StatusData;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by SETU on 1/25/2015.
 */
public class AddStatus extends Fragment {

    private EditText mainMessage,postUrl;
    private ImageView postImage;
    private Button addPost,choosePic;
    private String statusImage = "";
    private final int SELECT_PHOTO = 1;

    public AddStatus(){setHasOptionsMenu(true);}
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

        Bundle data = getArguments();
        final String command = data.getString(Commands.Status_Job.getCommand());
        final String latitude = data.getString(PropertyNames.Status_latitude.getProperty());
        final String longitude = data.getString(PropertyNames.Status_longitude.getProperty());
        final String groupKey = data.getString(PropertyNames.Status_groupKey.getProperty());


        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle dataBundle = ((SlidingDrawerActivity)getActivity()).getEmail();
                String mail = dataBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
                String username = dataBundle.getString(PropertyNames.Userinfo_Username.getProperty());

                StatusData status = new StatusData();
                status.setGroupKey(groupKey);
                status.setPersonMail(mail);
                status.setLatitude(latitude);
                status.setLongitude(longitude);
                status.setStatus(mainMessage.getText().toString());
                status.setPersonName(username);

                if(statusImage.length() > 0)
                    status.setStatusPhoto(statusImage);

                if(command.equals(Commands.Status_Job_Type_Individual.getCommand())) {
                    status.setKind(DatastoreKindNames.StatusbyIndividual.getKind());
                }
                else if(command.equals(Commands.Status_Job_Type_Group.getCommand())){
                    status.setKind(DatastoreKindNames.StatusInGroup.getKind());
                }

                postStatus(status);

            }
        });

        return v;
    }

    public void postStatus(StatusData status){

        Data d = new Data();
        d.setCommand(Commands.Status_add.getCommand());

        new StatusEndpointCommunicator() {
            @Override
            protected void onPostExecute(ArrayList<StatusData> result) {

                super.onPostExecute(result);

            }
        }.execute(new Pair<Data, StatusData>(d, status));
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

                    statusImage = ImageConverter.imageToStringConverter(selectedImage);

                    postImage.setImageBitmap(selectedImage);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


        }
    }
}
