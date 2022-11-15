package com.rld.app.mycampaign.models;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Image {

    private String path;
    private Bitmap blob;
    private String imageBase64;

    public Image()
    {

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBlob() {
        return blob;
    }

    public void setBlob(Bitmap blob) {
        this.blob = blob;
        this.imageBase64 = convertImageToString(blob);
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String convertImageToString(Bitmap image) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        int mb = image.getByteCount() / 8 / 1000 / 1000;
        int quality;
        if( mb >= 5 ) {
            quality = 100 / mb;
        } else{
            quality = 100;
        }
        image.compress(Bitmap.CompressFormat.JPEG, quality, array);
        byte[] bytes = array.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public void deleteImage() {
        File file = new File(this.path);
        if ( file.exists() ) {
            file.delete();
        }
    }

    @Override
    public String toString() {
        return "Image{" +
                "path='" + path + '\'' +
                ", blob=" + blob +
                ", imageBase64='" + imageBase64 + '\'' +
                '}';
    }
    
}
