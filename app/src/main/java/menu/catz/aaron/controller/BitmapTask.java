package menu.catz.aaron.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

//BitmapTask Class
public class BitmapTask {
    Bitmap bitmap;
    private boolean bLoaded = false;
    private OnLoadCallback loadCallback;

    public void setOnLoadCallback(OnLoadCallback callback){
        loadCallback = callback;
    }

    public void loadBitmap(final String url){
        new Thread(){
            @Override
            public void run() {
                Bitmap d = null;
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    d = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (Exception e) {
                }
                bLoaded = true;
                bitmap = d;
                loadCallback.onLoad(d);
            }
        }.start();

    }


    public class OnLoadCallback {
        public void onLoad(Bitmap bitmap){

        }
    }
}
