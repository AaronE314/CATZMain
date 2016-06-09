package menu.catz.aaron.catzmain;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class JSONLoader {
    //http://stackoverflow.com/questions/30232051/read-json-file-from-assets
    @Nullable
    public static String parseFileToString(Context context, String filename ) {
        try
        {
            InputStream stream = context.getAssets().open( filename );
            int size = stream.available();

            byte[] bytes = new byte[size];
            stream.read(bytes);
            stream.close();

            return new String( bytes );

        } catch ( IOException e ) {
            Log.i("GuiFormData", "IOException: " + e.getMessage() );
        }
        return null;
    }

}
