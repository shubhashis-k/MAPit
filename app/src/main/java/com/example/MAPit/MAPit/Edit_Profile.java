package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.mapit.backend.userinfoModelApi.model.ResponseMessages;
import com.mapit.backend.userinfoModelApi.model.UserinfoModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by SETU on 1/6/2015.
 */
public class Edit_Profile extends Fragment{
    private UserinfoModel updateProfile;
    private final int SELECT_PHOTO = 1;
    private ImageView profile_image;
    private Button Edit_profile;
    private EditText Update_name, Update_phone, Update_password;
    private String imageToString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, null, false);
        //updating profile_image

        profile_image = (ImageView) v.findViewById(R.id.update_profile_image);
        Edit_profile = (Button) v.findViewById(R.id.save_edit_profile);
        Update_name = (EditText) v.findViewById(R.id.update_signup_name);
        Update_phone = (EditText) v.findViewById(R.id.update_signup_phone);
        Update_password = (EditText) v.findViewById(R.id.update_signup_password);


        Edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData();
                updateData();
                Fragment fragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imagepicking = new Intent(Intent.ACTION_PICK);
                imagepicking.setType("image/png");
                startActivityForResult(imagepicking, SELECT_PHOTO);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                 Uri imageUri = null;
                try {
                     imageUri = imageReturnedIntent.getData();
                }catch (Exception e){
                    Toast.makeText(getActivity(),"Image Not Found",Toast.LENGTH_SHORT).show();
                    return;
                }
                //final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                //final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ShrinkBitmapConverter sh = new ShrinkBitmapConverter(getActivity());
                Bitmap selectedImage = null;
                try {
                    selectedImage = sh.shrinkBitmap(imageUri,50,50);
                } catch (Exception e) {
                   Toast.makeText(getActivity(),"Image Not Found",Toast.LENGTH_SHORT).show();
                }
                imageToString = ImageConverter.imageToStringConverter(selectedImage);
                if (imageToString.length() > 102400) {
                    Toast.makeText(getActivity(), "Image is too big", Toast.LENGTH_LONG).show();
                }
                else {
                    profile_image.setImageBitmap(selectedImage);
                }


        }
    }


    public void getData()
    {
        updateProfile = new UserinfoModel();
        updateProfile.setName(Update_name.getText().toString());
        updateProfile.setPassword(Update_password.getText().toString());
        updateProfile.setMobilephone(Update_phone.getText().toString());
        updateProfile.setMail(getMail());
        updateProfile.setImagedata(imageToString);
    }


    public String getMail()
    {
        Bundle mailBundle = getArguments();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }

    public void updateData()
    {
        new Edit_Profile_Endpoint_Communicator().execute(new Pair<Context, UserinfoModel>(getActivity(), updateProfile));
    }

}
