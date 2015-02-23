package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.GroupsEndpointReturnData;
import com.mapit.backend.groupApi.model.Groups;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by SETU on 1/25/2015.
 */
public class Create_New_Group_Fragment extends Fragment {
    private EditText groupName, groupDescription;
    private Button chooseGroupPic, createGroup;
    private ImageView groupImage;
    private String stringGroupImage;
    private final int SELECT_PHOTO = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_new_group, null, false);

        groupName = (EditText) v.findViewById(R.id.et_new_group_name);
        groupDescription = (EditText) v.findViewById(R.id.et_new_group_desc);
        groupImage = (ImageView) v.findViewById(R.id.add_new_group_pic);
        chooseGroupPic = (Button) v.findViewById(R.id.choosenewgrppic);
        createGroup = (Button) v.findViewById(R.id.bt_create_group);


        chooseGroupPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imagepicking = new Intent(Intent.ACTION_PICK);
                imagepicking.setType("image/*");
                startActivityForResult(imagepicking, SELECT_PHOTO);
            }
        });


        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (groupName.getText().toString().equals("") || groupImage.getDrawable() == null || groupDescription.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Fill All the Info", Toast.LENGTH_LONG).show();
                } else {
                    //getting latitude and longitude
                    String lat = String.valueOf(getArguments().getDouble("latitude"));
                    String lng = String.valueOf(getArguments().getDouble("longitude"));

                    Groups g = new Groups();
                    g.setCreatorMail(getmail());
                    g.setGroupName(groupName.getText().toString());
                    g.setLongitude(lng);
                    g.setLatitude(lat);
                    g.setGroupDescription(groupDescription.getText().toString());

                    if(stringGroupImage != null)
                        g.setGroupPic(stringGroupImage);

                    Data d = new Data();
                    d.setCommand(Commands.Group_Create.getCommand());

                    new GroupsEndpointCommunicator() {
                        @Override
                        protected void onPostExecute(GroupsEndpointReturnData result) {

                            super.onPostExecute(result);

                            String response = result.getResponseMessages();

                        }
                    }.execute(new Pair<Data, Groups>(d, g));
                }
            }
        });

        return v;
    }

    public String getmail() {
        Bundle mailBundle = ((SlidingDrawerActivity) getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
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
                    groupImage.setImageBitmap(selectedImage);

                    stringGroupImage = ImageConverter.imageToStringConverter(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
    }

}
