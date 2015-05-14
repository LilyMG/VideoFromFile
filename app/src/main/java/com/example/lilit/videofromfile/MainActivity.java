package com.example.lilit.videofromfile;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;


public class MainActivity extends ActionBarActivity {
    ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
        mediaChooser.setType("video/*, images/*");
        startActivityForResult(mediaChooser, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyLog", "result is " + resultCode);
        Log.d("MyLog", "data is " + data.getData().toString());
        Bundle bundle = data.getExtras();
        Log.d("MyLog", "bundle is " + bundle.toString());

        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
//        Bitmap myBitmap = BitmapFactory.decodeFile(picturePath);
//        ImageView myImage = (ImageView) findViewById(R.id.img);
//        myImage.setImageBitmap(myBitmap);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(picturePath);

            img.setImageBitmap(retriever.getFrameAtTime(4000 * 1000,MediaMetadataRetriever.OPTION_CLOSEST));


        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }


    }
}
