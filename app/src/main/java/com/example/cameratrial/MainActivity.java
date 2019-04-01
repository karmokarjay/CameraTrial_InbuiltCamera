package com.example.cameratrial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_display_image;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private String currentImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_display_image = findViewById(R.id.iv_image_captured);
        Button bt_capture_image = findViewById(R.id.bt_take_picture);
        bt_capture_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_take_picture:
                takePicture();
                break;
        }
    }

    private void takePicture() {
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureImageIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = getImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.android.file", imageFile);
                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
            startActivityForResult(captureImageIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Normal image returned from the camera is a thumbnail i.e. suitable mostly for icons. So to get a nice high quality image we need to store it in the storage and then get it to display.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iv_display_image.setImageBitmap(null);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //  Bundle extras = data.getExtras();
            //   if (extras != null) {
            //Bitmap bitmap = (Bitmap) extras.get("data");
            Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath);
            iv_display_image.setImageBitmap(bitmap);
            //   }
        }
    }

    public File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }
}
